package co.mandeep_singh.vitcomplaint;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class MyListAdapter3 extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] subtitle;
    private final Integer[] imgid;

    public MyListAdapter3(Activity context,  String[] subtitle, Integer[] imgid) {
        super(context, R.layout.notifications_list, subtitle);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.subtitle=subtitle;
        this.imgid=imgid;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.notifications_list, null,true);


        ImageView imageView = (ImageView) rowView.findViewById(R.id.request_photo);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.request_room_no);



        imageView.setImageResource(imgid[position]);
        subtitleText.setText(subtitle[position]);


        return rowView;

    };
}