package co.mandeep_singh.vitcomplaint.Adapter;


import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
        import android.view.ViewGroup;

        import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
        import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.zip.Inflater;

import co.mandeep_singh.vitcomplaint.Modal.PdfModel;
import co.mandeep_singh.vitcomplaint.Modal.StudentModel;
import co.mandeep_singh.vitcomplaint.R;
import co.mandeep_singh.vitcomplaint.WallFragment;
import co.mandeep_singh.vitcomplaint.WallFragmentWarden;

public class PdfAdapter extends BaseAdapter {
    private Context mContext;
    private WallFragmentWarden activity;
    private  ArrayList<PdfModel> pdfList;

    // Constructor
    public PdfAdapter(Context c, WallFragmentWarden activity,  ArrayList<PdfModel> pdfList) {
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