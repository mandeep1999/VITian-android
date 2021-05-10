package co.mandeep_singh.vitcomplaint.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import co.mandeep_singh.vitcomplaint.Modal.PdfModel;
import co.mandeep_singh.vitcomplaint.R;
import co.mandeep_singh.vitcomplaint.WallFragment;
import co.mandeep_singh.vitcomplaint.WallFragmentWarden;

public class PdfAdapterStudent extends BaseAdapter {
    private Context mContext;
    private WallFragment activity;
    private  ArrayList<PdfModel> pdfList;

    // Constructor
    public PdfAdapterStudent(Context c, WallFragment activity, ArrayList<PdfModel> pdfList) {
        mContext = c;
        this.activity = activity;
        this.pdfList = pdfList;
    }

    public int getCount() {
        return pdfList.size();
    }

    public Object getItem(int position) {
        return pdfList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            View myLayout = inflater.inflate(R.layout.pdf_tile, parent, false);
            textView = myLayout.findViewById(R.id.pdf_title);
            String temp = pdfList.get(position).getTitle().trim();
            if(temp.length() > 11){
                temp = temp.substring(0,11) + "...";
            }
            textView.setText(temp);
            return myLayout;
        }
        return convertView;
    }


}