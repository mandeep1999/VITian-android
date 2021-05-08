package co.mandeep_singh.vitcomplaint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class StudentsFragementWarden extends Fragment {
    ListView list, requestsList;
    ImageButton friendsListBtn;
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
            R.drawable.student,R.drawable.student,
            R.drawable.student,R.drawable.student,
            R.drawable.student,
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_studentlist_warden, container,false);
        MyListAdapter2 adapter=new MyListAdapter2(getActivity(), maintitle, subtitle,imgid);
        friendsListBtn = rootView.findViewById(R.id.friends_btn);
        list=(ListView)rootView.findViewById(R.id.list_friends);
        list.setAdapter(adapter);

        return rootView;
    }



}
