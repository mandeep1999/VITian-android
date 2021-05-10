package co.mandeep_singh.vitcomplaint;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import co.mandeep_singh.vitcomplaint.Adapter.HomeListAdapter;
import co.mandeep_singh.vitcomplaint.Adapter.HomeListAdapterWarden;
import co.mandeep_singh.vitcomplaint.Adapter.StudentsListAdapter;
import co.mandeep_singh.vitcomplaint.Auth.Auth;
import co.mandeep_singh.vitcomplaint.Modal.HomeModel;
import co.mandeep_singh.vitcomplaint.Modal.ProfileModel;
import co.mandeep_singh.vitcomplaint.Modal.StudentModel;

public class HomeFragementWarden extends Fragment implements OnDialogCloseListener {
    private RecyclerView recyclerView;
    ImageButton plusButton;
    View rootView;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private HomeListAdapterWarden adapter;
    private List<HomeModel> mList;
    private Query query;
    private EditText searchComplaint;
    private ListenerRegistration listenerRegistration;
    private String block = Auth.block;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_warden, container,false);
        recyclerView = rootView.findViewById(R.id.home_list);
        plusButton = (ImageButton)rootView.findViewById(R.id.plusButton);
//        searchComplaint = (EditText) rootView.findViewById(R.id.search_complaint_warden);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        establishComplaints();
//        addSearchFunction();
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

    private void addSearchFunction() {
        searchComplaint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search_field = searchComplaint.getText().toString().toLowerCase();
                if(!search_field.isEmpty() && !search_field.equals("")){
                    List<HomeModel> tempList = Optional.ofNullable(mList)
                            .map(List::stream)
                            .orElseGet(Stream::empty)
                            .filter(f -> (f.getComplaintType().toLowerCase().startsWith(search_field) ||
                                    f.getRoomNo().toLowerCase().startsWith(search_field) ||
                                    f.getStatus().toLowerCase().startsWith(search_field) ||
                                    f.getUrgent().toLowerCase().startsWith(search_field) ))
                            .collect(Collectors.toList());
                    HomeListAdapterWarden adapter1 = new HomeListAdapterWarden(HomeFragementWarden.this,tempList);
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelperWarden(adapter1));
                    itemTouchHelper.attachToRecyclerView(recyclerView);
                    recyclerView.swapAdapter(adapter1,true);
                }
                else {
                    recyclerView.swapAdapter(adapter,true);
                }
            }



            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showData() {
        DocumentReference docRef = firestore.collection("profiles/warden/profile").document(Auth.id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.toObject(ProfileModel.class) != null){
                    ProfileModel profileModel = documentSnapshot.toObject(ProfileModel.class).withId(Auth.id);
                    Auth.block = profileModel.getBlock();
                    query = firestore.collection("complaints/" + Auth.block +"/complaints");
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
