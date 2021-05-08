package co.mandeep_singh.vitcomplaint.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import java.util.List;
import co.mandeep_singh.vitcomplaint.HomeFragementWarden;
import co.mandeep_singh.vitcomplaint.Modal.StudentModel;
import co.mandeep_singh.vitcomplaint.R;
import co.mandeep_singh.vitcomplaint.StudentsFragmentWarden;
import de.hdodenhof.circleimageview.CircleImageView;

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
