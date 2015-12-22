package com.smarterparking.cmpe272.smarterpark;


import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.smarterparking.cmpe272.smarterpark.SmarterParkBO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class DisplayListActivity extends ListActivity {

    ArrayList<SmarterParkBO> SmarterParkerBoList = new ArrayList<SmarterParkBO>();
    public static double myLocLat;
    public static double myLocLng;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SmarterParkerBoList=(ArrayList<SmarterParkBO>)getIntent().getSerializableExtra("smarterParkBoList");
        myLocLat=(Double)getIntent().getSerializableExtra("myLocLat");
        myLocLng=(Double)getIntent().getSerializableExtra("myLocLng");
        setContentView(R.layout.activity_display_list1);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent launchActivity1 = new Intent(DisplayListActivity.this, MapsActivity.class);
                startActivity(launchActivity1);
            }
        });



        for(int i = 0; i < SmarterParkerBoList.size(); i++)
        {
            for (int j = 0; j< i; j++)
            {
                if(SmarterParkerBoList.get(i).compareTo(SmarterParkerBoList.get(j)) == 1)
                {
                    SmarterParkBO temp = SmarterParkerBoList.get(i);
                    SmarterParkerBoList.set(i, SmarterParkerBoList.get(j));
                    SmarterParkerBoList.set(j,temp);
                }
            }
        }

        // initiate the listadapter
        MyAdapter myAdapter = new MyAdapter(this, SmarterParkerBoList);
        ListView listView = getListView();
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SmarterParkBO entry= (SmarterParkBO) parent.getAdapter().getItem(position);
               String url = "http://maps.google.com/maps?saddr="+myLocLat+","+myLocLng+"&daddr="+entry.getLat()+","+entry.getLng()+"&mode=driving";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });
    }




}
