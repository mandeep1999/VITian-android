package co.mandeep_singh.vitcomplaint.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import co.mandeep_singh.vitcomplaint.Modal.AlertModel;
import co.mandeep_singh.vitcomplaint.R;
import co.mandeep_singh.vitcomplaint.WallFragment;

public class AlertAdapter  extends RecyclerView.Adapter<AlertAdapter.MyViewHolder>{

    private List<AlertModel> alertList;
    private WallFragment activity;

    public AlertAdapter(WallFragment wallFragmentActivity, List<AlertModel> alertList){
        this.alertList = alertList;
        activity = wallFragmentActivity;
    }
    @NonNull
    @Override
    public AlertAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity.getActivity()).inflate(R.layout.notifications_list,parent,false);
        return new MyViewHolder(view);
    }


    public Context getContext(){
        return activity.getActivity();
    }


    @Override
    public void onBindViewHolder(@NonNull AlertAdapter.MyViewHolder holder, int position) {
        AlertModel alertModel = alertList.get(position);
        String alert = alertModel.getAlertMessage();
        holder.alertMessageTextView.setText(alert);
    }



    @Override
    public int getItemCount() {
        return alertList.size();
    }

public class MyViewHolder extends RecyclerView.ViewHolder{
    TextView alertMessageTextView;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        alertMessageTextView = (TextView) itemView.findViewById(R.id.notification_message);
    }
}
}
