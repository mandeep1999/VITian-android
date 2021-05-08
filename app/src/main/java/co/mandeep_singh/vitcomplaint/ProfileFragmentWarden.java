package co.mandeep_singh.vitcomplaint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragmentWarden extends Fragment {
    String[] Blocks = { "A Block", "B Block", "C Block", "D Block", "E Block"};
    Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_warden, container,false);
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