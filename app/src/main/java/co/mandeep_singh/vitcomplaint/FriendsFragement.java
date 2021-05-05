package co.mandeep_singh.vitcomplaint;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FriendsFragement extends Fragment {
    ListView list;
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
        rootView = inflater.inflate(R.layout.fragment_friends, container,false);
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
