package co.mandeep_singh.vitcomplaint;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

class MyListAdapter3 extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] maintitle;
    private final String[] subtitle;
    private final Integer[] imgid;

    public MyListAdapter3(Activity context, String[] maintitle, String[] subtitle, Integer[] imgid) {
        super(context, R.layout.requests_list, maintitle);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.maintitle=maintitle;
        this.subtitle=subtitle;
        this.imgid=imgid;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.requests_list, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.request_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.request_photo);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.request_room_no);
        

        titleText.setText(maintitle[position]);
        imageView.setImageResource(imgid[position]);
        subtitleText.setText(subtitle[position]);


        return rowView;

    };

    public static class ProfileFragment extends Fragment {
        String[] Blocks = { "A Block", "B Block", "C Block", "D Block", "E Block"};
        Spinner spinner;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_profile, container,false);
            spinner = rootView.findViewById(R.id.spinner);
            establishSpinner();
            return rootView;
        }

        private void establishSpinner(){
            ArrayAdapter<String> blockArray = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Blocks);
            spinner.setAdapter(blockArray);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
}