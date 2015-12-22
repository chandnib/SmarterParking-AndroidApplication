package com.smarterparking.cmpe272.smarterpark;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smarterparking.cmpe272.smarterpark.R;
import com.smarterparking.cmpe272.smarterpark.SmarterParkBO;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<SmarterParkBO> {

    private final Activity context;
    private final ArrayList<SmarterParkBO> smarterParkBOList;


    public MyAdapter(Activity context, ArrayList<SmarterParkBO> smarterParkBOList) {
        super(context, R.layout.row_layout, smarterParkBOList);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.smarterParkBOList=smarterParkBOList;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.row_layout, null,true);

        TextView distance = (TextView) rowView.findViewById(R.id.distance);
        TextView name = (TextView) rowView.findViewById(R.id.name);

        distance.setText(Integer.toString(smarterParkBOList.get(position).getDistance()));
        name.setText(smarterParkBOList.get(position).getName());
        return rowView;

    };
}