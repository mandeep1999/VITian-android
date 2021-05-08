package co.mandeep_singh.vitcomplaint.Adapter;
import co.mandeep_singh.vitcomplaint.HomeFragement;
import co.mandeep_singh.vitcomplaint.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;
import co.mandeep_singh.vitcomplaint.Modal.HomeModel;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.MyViewHolder> {

    private List<HomeModel> homeList;
    private HomeFragement activity;
    private FirebaseFirestore firestore;

    public HomeListAdapter(HomeFragement homeActivity, List<HomeModel> homeList){
        this.homeList = homeList;
        activity = homeActivity;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity.getActivity()).inflate(R.layout.complaints_list,parent,false);
        firestore = FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }

    public void deleteTask(int position){
        HomeModel homeModel  = homeList.get(position);
        String temp = "complaints/" + homeModel.getBlock() + "/complaints";
        firestore.collection(temp).document(homeModel.HomeId).delete();
        homeList.remove(position);
        notifyItemRemoved(position);
    }

    public Context getContext(){
        return activity.getActivity();
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HomeModel homeModel = homeList.get(position);
        holder.complaint_type.setText(homeModel.getComplaintType());
        String temp = homeModel.getBlock() + " - " + homeModel.getRoomNo();
        holder.roomNo.setText(temp);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(activity.getActivity(),
                R.array.status_array, R.layout.spinner_list);
        adapter2.setDropDownViewResource(android.R.layout.simple_list_item_1);
        holder.status.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(activity.getActivity(),
                R.array.urgent_array, R.layout.spinner_list );
        adapter3.setDropDownViewResource(android.R.layout.simple_list_item_1);
        holder.urgent.setAdapter(adapter3);

        if(homeModel.getStatus() != null){
            int spinnerPosition = adapter2.getPosition(homeModel.getStatus());
                holder.status.setSelection(spinnerPosition);
        }

        if(homeModel.getUrgent() != null){
            int spinnerPosition = adapter3.getPosition(homeModel.getUrgent());
            holder.urgent.setSelection(spinnerPosition);
        }
        if(homeModel.getComplaintPhoto() != null){
            Picasso.with(activity.getActivity()).load(homeModel.getComplaintPhoto()).into(holder.complaint_image);
        }

        String block = homeModel.getBlock();
        String roomNo = homeModel.getRoomNo();

        holder.urgent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String temp = parent.getItemAtPosition(position).toString();
                    firestore.collection("complaints/" + block +"/complaints").document(homeModel.HomeId).update("urgent",temp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String temp = parent.getItemAtPosition(position).toString();
                    firestore.collection("complaints/" + block +"/complaints").document(homeModel.HomeId).update("status",temp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    @Override
    public int getItemCount() {
        return homeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        Spinner status, urgent;
        TextView complaint_type, roomNo, remark;
        ImageView complaint_image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            complaint_image = itemView.findViewById(R.id.complaint_photo_item);
            complaint_type = itemView.findViewById(R.id.complaints_type_item);
            roomNo = itemView.findViewById(R.id.room_no_item);
            status = itemView.findViewById(R.id.status_type_item);
            urgent = itemView.findViewById(R.id.urgent_type_item);
        }
    }
}
