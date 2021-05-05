package co.mandeep_singh.vitcomplaint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class HomeFragement extends Fragment {
    ListView list;
    ImageButton plusButton;
    View rootView;
    String[] maintitle ={
            "Title 1","Title 2",
            "Title 3","Title 4",
            "Title 5",
    };

    String[] subtitle ={
            "Sub Title 1","Sub Title 2",
            "Sub Title 3","Sub Title 4",
            "Sub Title 5",
    };

    Integer[] imgid={
            R.drawable.task,R.drawable.task,
            R.drawable.task,R.drawable.task,
            R.drawable.task,
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container,false);
        MyListAdapter adapter=new MyListAdapter(getActivity(), maintitle, subtitle,imgid);
        list=(ListView)rootView.findViewById(R.id.list);
        plusButton = (ImageButton)rootView.findViewById(R.id.plusButton);
        list.setAdapter(adapter);
        establishList();
        establishAddComplaint();
        return rootView;
    }

    private void establishAddComplaint() {


        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_bottom_sheet,(LinearLayout)rootView.findViewById(R.id.bottomSheetContainer));
                Spinner spinner1 = (Spinner) bottomSheetView.findViewById(R.id.complaints_type);
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(rootView.getContext(),
                        R.array.complaint_type, android.R.layout.simple_spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(adapter1);
                Spinner spinner2 = (Spinner) bottomSheetView.findViewById(R.id.status_type2);
                ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(rootView.getContext(),
                        R.array.status_array, android.R.layout.simple_spinner_item);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);
                Spinner spinner3 = (Spinner) bottomSheetView.findViewById(R.id.urgent_type2);
                ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(rootView.getContext(),
                        R.array.urgent_array, android.R.layout.simple_spinner_item);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner3.setAdapter(adapter3);

                bottomSheetView.findViewById(R.id.submit_complaint).setOnClickListener(new View.OnClickListener() {
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

    private void establishList() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // TODO Auto-generated method stub


            }
        });
    }
}
