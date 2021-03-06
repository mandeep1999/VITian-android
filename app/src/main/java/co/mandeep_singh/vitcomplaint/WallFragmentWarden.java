package co.mandeep_singh.vitcomplaint;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.mandeep_singh.vitcomplaint.Adapter.HomeListAdapterWarden;
import co.mandeep_singh.vitcomplaint.Adapter.PdfAdapter;
import co.mandeep_singh.vitcomplaint.Auth.Auth;
import co.mandeep_singh.vitcomplaint.Modal.PdfModel;

import static android.app.Activity.RESULT_OK;

public class WallFragmentWarden extends Fragment {
    ImageButton AlertBtn;
    GridView gridView;
    View rootView;
    Button uploadButton;
    Uri imageuri = null;
    FirebaseFirestore _firestore = FirebaseFirestore.getInstance();
    ArrayList<PdfModel> pdfList = new ArrayList<PdfModel>();
    private PdfAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_wall_warden, container,false);
        AlertBtn = rootView.findViewById(R.id.notifications_btn_warden);
        gridView = (GridView)rootView.findViewById(R.id.gridView_warden);
        uploadButton = (Button) rootView.findViewById(R.id.upload_file_button);
        establishGridViewForWarden();
        establishPdfs();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), ViewPdf.class);
                i.putExtra("fileUrl", pdfList.get(position).getFileUrl());
                startActivity(i);
            }
        });
        AlertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAlert();
            }
        });
        return rootView;
    }



    private void establishGridViewForWarden() {
         uploadButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent galleryIntent = new Intent();
                 galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                 // We will be redirected to choose pdf
                 galleryIntent.setType("application/pdf");
                 startActivityForResult(galleryIntent, 7);
             }
         });
    }

    private void establishPdfs() {
        pdfList = new ArrayList<PdfModel>();
        adapter = new PdfAdapter(getActivity(),WallFragmentWarden.this, pdfList);
        gridView.setAdapter(adapter);
        showData();
    }

    private void showData() {
        _firestore.collection("Circulars").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String id = documentChange.getDocument().getId();
                        PdfModel pdfModel = documentChange.getDocument().toObject(PdfModel.class);
                        pdfList.add(pdfModel);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void sendAlert(){
        final String[] m_Text = new String[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Send Alert");
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text[0] = input.getText().toString();
                if(!m_Text[0].equals("")){
                    Map<String, Object> alertObject = new HashMap<>();
                    alertObject.put("alertMessage", m_Text[0]);
                    alertObject.put("block", Auth.block);
                    alertObject.put("wardenId", Auth.id);
                    alertObject.put("time", LocalDateTime.now().toString());
                    _firestore.collection(  "alerts")
                            .document(LocalDateTime.now().toString()).set(alertObject)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(),"Alert sent",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    ProgressDialog dialog;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // Here we are initialising the progress dialog box
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Uploading");

            // this will show message uploading
            // while pdf is uploading
            dialog.show();
            imageuri = data.getData();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            File file = new File(imageuri.getPath());
            final String messagePushID  = file.getName().substring(0, file.getName().length()-4);

            // Here we are uploading the pdf in firebase storage with the name of current time
            final StorageReference filepath = storageReference.child( "circulars/" +messagePushID + "." + "pdf");
            Toast.makeText(getActivity(), filepath.getName(), Toast.LENGTH_SHORT).show();
            filepath.putFile(imageuri).continueWithTask((Continuation) task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filepath.getDownloadUrl();
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        dialog.dismiss();
                        Uri uri = task.getResult();
                        String myurl;
                        myurl = uri.toString();
                        Map<String, Object> temp = new HashMap<>();
                        temp.put("fileUrl", myurl);
                        temp.put("title", messagePushID);
                        temp.put("time", LocalDateTime.now().toString());
                        temp.put("block", Auth.block );
                        _firestore.collection("Circulars" ).document(LocalDateTime.now().toString()).set(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "UploadedFailed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


}
