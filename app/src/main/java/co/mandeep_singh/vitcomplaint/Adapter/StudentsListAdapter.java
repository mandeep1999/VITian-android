package co.mandeep_singh.vitcomplaint.Adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import java.util.List;
import co.mandeep_singh.vitcomplaint.HomeFragementWarden;
import co.mandeep_singh.vitcomplaint.Modal.StudentModel;
import co.mandeep_singh.vitcomplaint.R;
import co.mandeep_singh.vitcomplaint.StudentsFragmentWarden;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CALL_PHONE;

public class StudentsListAdapter extends RecyclerView.Adapter<StudentsListAdapter.MyViewHolder>{


    private List<StudentModel> studentsList;
    private StudentsFragmentWarden activity;
    private FirebaseFirestore firestore;

    public StudentsListAdapter(StudentsFragmentWarden homeActivity, List<StudentModel> homeList){
        this.studentsList = homeList;
        activity = homeActivity;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity.getActivity()).inflate(R.layout.students_list_item,parent,false);
        firestore = FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }

    public void callNumber(int position) {
        StudentModel studentModel = studentsList.get(position);
        String phoneNo = studentModel.getPhoneNo();
        String finalNumber = "+91" + phoneNo;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + finalNumber));

        if (ContextCompat.checkSelfPermission(activity.getContext(),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity.getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    1);

        } else {

            try {
                activity.getActivity().startActivity(callIntent);

            } catch(SecurityException e) {
                e.printStackTrace();
            }
        }


    }
    public void sendMessage(int position){
        final String[] m_Text = new String[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(activity.getContext());
        builder.setTitle("Send Message");
        final EditText input = new EditText(activity.getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text[0] = input.getText().toString();
                if(!m_Text[0].equals(""))
                sendingMessage(m_Text[0], position, dialog);
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
    public void  sendingMessage(String message, int position, DialogInterface dialog){
        StudentModel studentModel = studentsList.get(position);
        String phoneNo = studentModel.getPhoneNo();
        String finalNumber = "+91" + phoneNo;
        Intent intent=new Intent(activity.getContext(),HomeFragementWarden.class);
        PendingIntent pi = PendingIntent.getActivity(activity.getActivity(), 0, intent,0);
        SmsManager sms = SmsManager.getDefault();

        if (ContextCompat.checkSelfPermission(activity.getContext(),
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity.getActivity(),
                    new String[]{Manifest.permission.SEND_SMS},
                    2);
            dialog.cancel();
            Toast.makeText(activity.getContext(), "Failed to send retry", Toast.LENGTH_LONG).show();
        } else {

            try {
                sms.sendTextMessage(finalNumber, null, message, pi,null);
                Toast.makeText(activity.getContext(), "Sent Successfully", Toast.LENGTH_LONG).show();

            } catch(SecurityException e) {
                e.printStackTrace();
            }
        }


    }


    public Context getContext(){
        return activity.getActivity();
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StudentModel studentModel = studentsList.get(position);

        if(!studentModel.getName().equals("")){
            holder.studentName.setText(studentModel.getName());
        }
        if(!studentModel.getRoomNo().equals("")){
            String temp = studentModel.getBlock().charAt(0) + " - "+studentModel.getRoomNo();
            holder.studentRoomNo.setText(temp);
        }

        if(!studentModel.getImageUrl().equals("")){
            Picasso.with(activity.getActivity()).load(studentModel.getImageUrl()).into(holder.studentPhoto);
        }

    }


    @Override
    public int getItemCount() {
        return studentsList.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView studentPhoto;
        TextView studentName, studentRoomNo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            studentPhoto = itemView.findViewById(R.id.student_item_photo);
            studentName = itemView.findViewById(R.id.student_item_name);
            studentRoomNo = itemView.findViewById(R.id.student_item_roomNo);
        }
    }
}
