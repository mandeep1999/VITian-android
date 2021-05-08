package co.mandeep_singh.vitcomplaint;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import co.mandeep_singh.vitcomplaint.Adapter.HomeListAdapter;
import co.mandeep_singh.vitcomplaint.Adapter.HomeListAdapterWarden;
import co.mandeep_singh.vitcomplaint.Modal.HomeModel;

public class HomeFragementWarden extends Fragment implements OnDialogCloseListener {
    private RecyclerView recyclerView;
    ImageButton plusButton;
    View rootView;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private HomeListAdapterWarden adapter;
    private List<HomeModel> mList;
    private Query query;
    private ListenerRegistration listenerRegistration;
    private String block ="L", roomNo = "202";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_warden, container,false);
        recyclerView = rootView.findViewById(R.id.home_list);
        plusButton = (ImageButton)rootView.findViewById(R.id.plusButton);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        establishComplaints();
        return rootView;
    }

    private void establishComplaints() {
        mList = new ArrayList<>();
        adapter = new HomeListAdapterWarden(HomeFragementWarden.this, mList);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelperWarden(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        showData();
    }
    private void showData() {
        query = firestore.collection("complaints/" + block + "/" + roomNo);
        listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String id = documentChange.getDocument().getId();
                        HomeModel homeModel = documentChange.getDocument().toObject(HomeModel.class).withId(id);
                        mList.add(homeModel);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        listenerRegistration.remove();
        mList.clear();
        showData();
        adapter.notifyDataSetChanged();

    }
}
