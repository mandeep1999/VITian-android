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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import co.mandeep_singh.vitcomplaint.Adapter.PdfAdapterStudent;
import co.mandeep_singh.vitcomplaint.Modal.PdfModel;


public class WallFragment extends Fragment {
    ImageButton friendsListBtn;
    ListView  requestsList;
    GridView gridView;
    View rootView;
    FirebaseFirestore _firestore = FirebaseFirestore.getInstance();
    ArrayList<PdfModel> pdfList = new ArrayList<PdfModel>();
    private PdfAdapterStudent adapter;

    String[] subtitle ={
            "Sub Title 1","Sub Title 2",
            "Sub Title 3","Sub Title 4",
            "Sub Title 5",
    };
    Integer[] imgid1={
            R.drawable.ic_baseline_circle_notifications_24,R.drawable.ic_baseline_circle_notifications_24,
            R.drawable.ic_baseline_circle_notifications_24,R.drawable.ic_baseline_circle_notifications_24,
            R.drawable.ic_baseline_circle_notifications_24
    };

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
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getActivity()).inflate(R.layout.notifications_bottomsheet,(LinearLayout)rootView.findViewById(R.id.bottomSheetContainer2));
                MyListAdapter3 adapter =new MyListAdapter3(getActivity(),subtitle,imgid1);
                requestsList=(ListView)bottomSheetView.findViewById(R.id.requests_list);
                requestsList.setAdapter(adapter);

                bottomSheetView.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
    }

}
