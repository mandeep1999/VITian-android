package co.mandeep_singh.vitcomplaint;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.type.DateTime;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class AddComplaint extends BottomSheetDialogFragment {
    public static final String TAG = "AddComplaint";
    private ImageView complaintImage;
    private Spinner complaints_type, statusType, urgentType;
    private EditText remark;
    private Button submitComplaint;
    private FirebaseFirestore firestore;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    private final int PICK_IMAGE_REQUEST = 22;
    UploadTask uploadTask;
    private  Uri filePath;
    private Context context;
    private String  block = "L", roomNo = "202", studentId = "mandeep";
    Map<String, Object> taskMap = new HashMap<>();

    public static AddComplaint newInstance(){
        return new AddComplaint();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_complaint,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        complaintImage = view.findViewById(R.id.complaint_image);
        complaints_type = view.findViewById(R.id.complaints_type);
        statusType = view.findViewById(R.id.status_type2);
        urgentType = view.findViewById(R.id.urgent_type2);
        remark = view.findViewById(R.id.remark);
        submitComplaint = view.findViewById(R.id.submit_complaint);
        firestore = FirebaseFirestore.getInstance();


        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(context,
                R.array.complaint_type, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        complaints_type.setAdapter(adapter1);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(context,
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusType.setAdapter(adapter2);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(context,
                R.array.urgent_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        urgentType.setAdapter(adapter3);

        complaintImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        submitComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String complaint = complaints_type.getSelectedItem().toString();
                String status = statusType.getSelectedItem().toString();
                String urgent = urgentType.getSelectedItem().toString();
                String remarkk = remark.getText().toString();

                    if(complaint.isEmpty()){
                        Toast.makeText(context, "Please select a complaint", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        taskMap.put("complaintType" , complaint);
                        taskMap.put("status", status);
                        taskMap.put("urgent", urgent);
                        taskMap.put("remark", remarkk);
                        taskMap.put("block", block);
                        taskMap.put("roomNo", roomNo);
                        taskMap.put("studentId", studentId);
                        taskMap.put("time", FieldValue.serverTimestamp());
                        if(filePath == null) {
                            taskMap.put("image", "");
                            SaveToFirebase();
                        }
                        else{
                            uploadImage();
                        }
                    }

                dismiss();
            }
        });
    }

    private void SaveToFirebase() {
        firestore.collection(  "complaints/" + block +"/complaints")
                .document(LocalDateTime.now().toString()).set(taskMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,"Complaint Saved",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }


    private void uploadImage()
    {
        if (filePath != null) {
            StorageReference ref
                    = storageReference
                    .child(
                            "images/complaints/"
                                    + LocalDateTime.now().toString());
            //error
            uploadTask = ref.putFile(filePath);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String url = downloadUri.toString();
                        taskMap.put("image", url);
                        SaveToFirebase();
                    } else {
                        Toast.makeText(context,"Update Failed", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();

        }
    }
}
