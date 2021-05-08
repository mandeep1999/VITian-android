package co.mandeep_singh.vitcomplaint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;


public class WallFragment extends Fragment {
    ImageButton friendsListBtn;
    ListView  requestsList;
    View rootView;

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
        rootView = inflater.inflate(R.layout.fragment_wall_warden, container,false);
        friendsListBtn = rootView.findViewById(R.id.notifications_btn);
        establishFriendsListBtn();
        return rootView;
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
