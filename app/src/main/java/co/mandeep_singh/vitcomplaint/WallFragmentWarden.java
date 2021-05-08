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

public class WallFragmentWarden extends Fragment {
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
        rootView = inflater.inflate(R.layout.fragment_wall_warden, container,false);
        MyListAdapter2 adapter=new MyListAdapter2(getActivity(), maintitle, subtitle,imgid);
        friendsListBtn = rootView.findViewById(R.id.friends_btn);
        list=(ListView)rootView.findViewById(R.id.list_friends);
        list.setAdapter(adapter);
        establishList();
        establishFriendsListBtn();
        return rootView;
    }

    private void establishFriendsListBtn() {
        friendsListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getActivity()).inflate(R.layout.notifications_bottomsheet,(LinearLayout)rootView.findViewById(R.id.bottomSheetContainer2));
                MyListAdapter3 adapter =new MyListAdapter3(getActivity(), maintitle, subtitle,imgid);
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

    private void establishList() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // TODO Auto-generated method stub


            }
        });
    }


}
