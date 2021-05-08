package co.mandeep_singh.vitcomplaint;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class AddComplaint extends BottomSheetDialogFragment {
    public static final String TAG = "AddComplaint";
    private ImageView complaintImage;
    private Spinner complaints_type, statusType, urgentType;
    private EditText remark;
    private Button submitComplaint;
    private FirebaseFirestore firestore;
    private Context context;
    private String  block = "L", roomNo = "202";

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


        submitComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String complaint = complaints_type.getSelectedItem().toString();
                String status = statusType.getSelectedItem().toString();
                String urgent = urgentType.getSelectedItem().toString();
                String remarkk = remark.getText().toString();
                String image = getImageUrl();


                    if(complaint.isEmpty()){
                        Toast.makeText(context, "Please select a complaint", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Map<String, Object> taskMap = new HashMap<>();
                        taskMap.put("complaintType" , complaint);
                        taskMap.put("status", status);
                        taskMap.put("urgent", urgent);
                        taskMap.put("image", image);
                        taskMap.put("remark", remarkk);
                        taskMap.put("block", block);
                        taskMap.put("roomNo", roomNo);
                        taskMap.put("time", FieldValue.serverTimestamp());
                        firestore.collection(  "complaints/" + block + "/"+ roomNo )
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

                dismiss();
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

    public String getImageUrl(){
        return "test";
    }
}
