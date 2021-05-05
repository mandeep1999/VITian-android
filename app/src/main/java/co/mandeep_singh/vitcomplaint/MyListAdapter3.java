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
}