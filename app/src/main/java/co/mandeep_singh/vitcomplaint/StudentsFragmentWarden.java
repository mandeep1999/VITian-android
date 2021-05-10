package co.mandeep_singh.vitcomplaint;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import co.mandeep_singh.vitcomplaint.Adapter.HomeListAdapter;
import co.mandeep_singh.vitcomplaint.Adapter.StudentsListAdapter;
import co.mandeep_singh.vitcomplaint.Modal.HomeModel;
import co.mandeep_singh.vitcomplaint.Modal.StudentModel;

public class StudentsFragmentWarden extends Fragment implements OnDialogCloseListener {

    private RecyclerView recyclerView;
    View rootView;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private StudentsListAdapter adapter;
    private List<StudentModel> mList;
    private EditText studentSearch;
    private Query query;
    private ListenerRegistration listenerRegistration;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_studentlist_warden, container,false);
        recyclerView = rootView.findViewById(R.id.student_list_list);
        studentSearch = rootView.findViewById(R.id.students_list_search);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        establishAddStudents();
        addSearchFunction();
        return rootView;
    }

    private void addSearchFunction() {
        studentSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search_field = studentSearch.getText().toString().toLowerCase();

                if(!search_field.isEmpty() && !search_field.equals("")){
                    List<StudentModel> tempList = Optional.ofNullable(mList)
                        .map(List::stream)
                        .orElseGet(Stream::empty)
                        .filter(f -> (f.getName().toLowerCase().startsWith(search_field) || f.getRoomNo().toLowerCase().startsWith(search_field) || f.getBlock().toLowerCase().charAt(0) == search_field.charAt(0)))
                        .collect(Collectors.toList());
                StudentsListAdapter adapter1 = new StudentsListAdapter(StudentsFragmentWarden.this, tempList);
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelperStudentList(adapter1));
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


    private void establishAddStudents() {
        mList = new ArrayList<>();
        adapter = new StudentsListAdapter(StudentsFragmentWarden.this, mList);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelperStudentList(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        showData();
    }
    private void showData() {
        query = firestore.collection("profiles/students/profile");
        listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String id = documentChange.getDocument().getId();
                        StudentModel studentModel = documentChange.getDocument().toObject(StudentModel.class).withId(id);
                        mList.add(studentModel);
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
