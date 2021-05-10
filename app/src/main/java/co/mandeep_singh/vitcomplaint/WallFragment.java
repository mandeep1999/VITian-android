package co.mandeep_singh.vitcomplaint;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import co.mandeep_singh.vitcomplaint.Adapter.AlertAdapter;
import co.mandeep_singh.vitcomplaint.Adapter.HomeListAdapter;
import co.mandeep_singh.vitcomplaint.Adapter.PdfAdapterStudent;
import co.mandeep_singh.vitcomplaint.Auth.Auth;
import co.mandeep_singh.vitcomplaint.Modal.AlertModel;
import co.mandeep_singh.vitcomplaint.Modal.HomeModel;
import co.mandeep_singh.vitcomplaint.Modal.PdfModel;


public class WallFragment extends Fragment {
    ImageButton friendsListBtn;
    RecyclerView recyclerView;
    GridView gridView;
    View rootView;
    FirebaseFirestore _firestore = FirebaseFirestore.getInstance();
    ArrayList<PdfModel> pdfList = new ArrayList<PdfModel>();

    private PdfAdapterStudent adapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_wall_student, container,false);
        friendsListBtn = rootView.findViewById(R.id.notifications_btn);
        gridView = (GridView)rootView.findViewById(R.id.gridView_student);
        establishFriendsListBtn();
        establishPdfs();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), ViewPdf.class);
                i.putExtra("fileUrl", pdfList.get(position).getFileUrl());
                startActivity(i);
            }
        });
        return rootView;
    }

    private void establishPdfs() {
        pdfList = new ArrayList<PdfModel>();
        adapter = new PdfAdapterStudent(getActivity(),WallFragment.this, pdfList);
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

    private void establishFriendsListBtn() {
        friendsListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<AlertModel> alertsList = new ArrayList<>();
                AlertAdapter adapterAlert;
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getActivity()).inflate(R.layout.notifications_bottomsheet,(LinearLayout)rootView.findViewById(R.id.bottomSheetContainer2));
                recyclerView = (RecyclerView) bottomSheetView.findViewById(R.id.notifications_list_recycle);
                adapterAlert = new AlertAdapter(WallFragment.this, alertsList);
                recyclerView.setAdapter(adapterAlert);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                bottomSheetView.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
                showNotifications(alertsList , adapterAlert);
            }
        });
    }

    private void showNotifications(ArrayList<AlertModel> List, AlertAdapter adapterAlert) {
        _firestore.collection("alerts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String id = documentChange.getDocument().getId();
                        AlertModel alertModel = documentChange.getDocument().toObject(AlertModel.class).withId(id);
                            List.add(alertModel);
                            adapterAlert.notifyDataSetChanged();
                    }
                }

            }
        });
    }


}
