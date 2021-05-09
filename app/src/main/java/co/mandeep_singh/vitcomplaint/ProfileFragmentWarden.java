package co.mandeep_singh.vitcomplaint;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import co.mandeep_singh.vitcomplaint.Auth.Auth;
import co.mandeep_singh.vitcomplaint.Modal.ProfileModel;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragmentWarden extends Fragment {
    String[] Blocks = { "A Block", "B Block", "C Block", "D Block", "E Block"};
    String name, phoneNo, block, profileUrl, wardenId = "mandeep warden";
    Spinner spinner;
    EditText nameEditText, phoneNoEditText, roomNoEditText;
    CircleImageView profilePhotoStudent;
    ImageButton penStudent, logOut;
    Button updateButton;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    private Context context;
    UploadTask uploadTask;
    private Uri filePath;
    ArrayAdapter<String> blockArray;
    private final int PICK_IMAGE_REQUEST = 22;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Map<String, Object> ProfileMap = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_warden, container,false);
        spinner = rootView.findViewById(R.id.block_spinner_warden);
        nameEditText = rootView.findViewById(R.id.person_name_warden);
        phoneNoEditText = rootView.findViewById(R.id.phone_no_warden);
        profilePhotoStudent = rootView.findViewById(R.id.profile_photo_warden);
        penStudent = rootView.findViewById(R.id.editPen_warden);
        updateButton = rootView.findViewById(R.id.update_warden);
        logOut = rootView.findViewById(R.id.logout_warden);

        establishSpinner();
        fetchProfile();
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        penStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth auth = new Auth();
                auth.signOut();
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        return rootView;
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
    private void uploadImage()
    {
        if (filePath != null) {
            StorageReference ref
                    = storageReference
                    .child(
                            "images/profiles/warden"
                                    + wardenId);
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
                        profileUrl = downloadUri.toString();
                        ProfileMap.put("imageUrl", profileUrl);
                        SaveToFirebase();
                    } else {
                        Toast.makeText(context,"Update Failed", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }


    private void fetchProfile() {
        DocumentReference docRef = firestore.collection("profiles/warden/profile").document(wardenId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.toObject(ProfileModel.class) != null){
                    ProfileModel profileModel = documentSnapshot.toObject(ProfileModel.class).withId(wardenId);
                    name = profileModel.getName();
                    profileUrl = profileModel.getImageUrl();
                    block = profileModel.getBlock();
                    phoneNo = profileModel.getPhoneNo();
                    nameEditText.setText(name);
                    phoneNoEditText.setText(phoneNo);

                    if(profileUrl!= null){
                        Picasso.with(context).load(profileUrl).into(profilePhotoStudent);
                    }
                    if(block != null){
                        int spinnerPosition = blockArray.getPosition(block);
                        spinner.setSelection(spinnerPosition);
                    }
                }
            }
        });
    }


    private void updateProfile() {
        if(!nameEditText.getText().toString().equals("") && !phoneNoEditText.getText().toString().equals("") ){
            name = nameEditText.getText().toString();
            phoneNo = phoneNoEditText.getText().toString();
            block = spinner.getSelectedItem().toString();
            ProfileMap.put("name", name);
            ProfileMap.put("phoneNo", phoneNo);
            ProfileMap.put("block", block);
            ProfileMap.put("imageUrl", profileUrl);
            ProfileMap.put("time", FieldValue.serverTimestamp());
            if(filePath == null)
                SaveToFirebase();
            else{
                uploadImage();
            }
        }
    }

    private void SaveToFirebase() {
        firestore.collection(  "profiles/warden/profile")
                .document(wardenId).set(ProfileMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,"Profile Updated",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void establishSpinner(){
        blockArray = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Blocks);
        spinner.setAdapter(blockArray);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                block = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
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