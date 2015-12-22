package com.smarterparking.cmpe272.smarterpark;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static GoogleMap mMap;

    private LatLngBounds.Builder builder;

    private static Marker MyLocationMarker ;

    private static ArrayList<SmarterParkBO> smarterParkBOList;

    private static  boolean Flag = true;

    private static double radius = 0.5;

    private SeekBar seekBar;
    private TextView textView;

    private static ArrayList<Marker> markersList;

    private CallNodeServices callNodeServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LatLng sydney = new LatLng(37.322993, -121.883200);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent launchActivity1= new Intent(MapsActivity.this,DisplayListActivity.class);
                if(smarterParkBOList!=null) {
                    launchActivity1.putExtra("smarterParkBoList", smarterParkBOList);
                    launchActivity1.putExtra("myLocLat", MyLocationMarker.getPosition().latitude);
                    launchActivity1.putExtra("myLocLng", MyLocationMarker.getPosition().longitude);
                }
                startActivity(launchActivity1);
            }
        });

        //SeekBar code goes below
        initializeVariables();

        // Initialize the textview with '0'.
        textView.setText("Distance: " + (seekBar.getProgress()*0.1)/10 + "/" + seekBar.getMax()/10);

        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 5;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                radius = (seekBar.getProgress() * 1.0) / 10;
               // Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                 textView.setText("Covered: " + (seekBar.getProgress()*1.0)/10+"Miles" + "/" + (seekBar.getMax()*1.0)/10);
                if(mMap!=null && MyLocationMarker!=null){
                     callNodeServices = new CallNodeServices();
                    LatLng myloc = new LatLng(MyLocationMarker.getPosition().latitude, MyLocationMarker.getPosition().longitude);
                    callNodeServices.execute(myloc);
                    radius = (seekBar.getProgress() * 1.0) / 10;
                }
                Toast.makeText(getApplicationContext(), "Finding Parkings", Toast.LENGTH_SHORT).show();
            }
        });

        if(mMap!=null && MyLocationMarker!=null){
             callNodeServices = new CallNodeServices();
            LatLng myloc = new LatLng(MyLocationMarker.getPosition().latitude, MyLocationMarker.getPosition().longitude);
            callNodeServices.execute(myloc);
        }
    }


    // A private method to help us initialize our variables.
    private void initializeVariables() {
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        seekBar.setProgress(5);
        textView = (TextView) findViewById(R.id.textView1);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this.myMarkerClickListener);
        mMap.setOnMyLocationChangeListener(this.myLocationChangeListener);
    }

    // second part
    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if(Flag) {
                 callNodeServices = new CallNodeServices();
                LatLng myloc = new LatLng(location.getLatitude(), location.getLongitude());
                callNodeServices.execute(myloc);
                Flag=false;
            }
            if(MyLocationMarker!=null)
            MyLocationMarker.remove();
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
             MyLocationMarker = mMap.addMarker(new MarkerOptions()
                    .title("My Location")
                    .snippet("")
                    .position(loc).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_s)));;

        }
    };

    private class showRoute extends AsyncTask<LatLng, Void, ArrayList<LatLng>> {

        @Override
        protected ArrayList<LatLng> doInBackground(LatLng... params) {
            DrawDirections dd = new DrawDirections();
            ArrayList<LatLng> points = null;
            try {
              points = dd.getPolyLine(params[0], params[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return points;
        }

        @Override
        protected void onPostExecute(ArrayList<LatLng> latLngs) {
            super.onPostExecute(latLngs);
            Polyline polyline;
            polyline = mMap.addPolyline(new PolylineOptions().geodesic(true).color(Color.BLUE).width(20).zIndex(1000)
                    .addAll(latLngs));
        }
    }


    // Async task for getting data from node services
    private class CallNodeServices extends AsyncTask<LatLng, Void, String> {

        @Override
        protected String doInBackground(LatLng... params) {
            NodeCallAPI nodeCallApi = new NodeCallAPI();
            String url = "http://smarterparkingdashboardserver.mybluemix.net/getparkinginfo?radius="+"10"+"&latitude="+Double.toString(params[0].latitude)+"&longitude="+Double.toString(params[0].longitude);
            String result = nodeCallApi.requestContent(url);
            Log.d("results1", "2" + result + "1");
            return result;
        }

        @Override
        protected void onPostExecute(String str) {
            if (markersList != null) {
                for (Marker marker : markersList) {
                    marker.remove();
                }
            }
            super.onPostExecute(str);
            NodeCallAPI nodeCallApi = new NodeCallAPI();
            smarterParkBOList = nodeCallApi.parseJsonInBO(str);
            Log.d("sizeoflist",Integer.toString(smarterParkBOList.size()));
            if (smarterParkBOList.size() != 0) {
                builder = new LatLngBounds.Builder();
                mMap.setMyLocationEnabled(true);
                markersList = new ArrayList<Marker>();
                //LatLng currentLocation = new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude());
                for (SmarterParkBO smarterParkBO : smarterParkBOList) {
                    if (smarterParkBO.getDeviceType().equals("CAR_PARK_SENSOR")) {
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .title(smarterParkBO.getName())
                                .snippet("Empty Spot:" + smarterParkBO.getEmptySlots() + " Price:" + smarterParkBO.getRate() + "$" + " " + smarterParkBO.getDeviceType())
                                .position(new LatLng(smarterParkBO.getLat(), smarterParkBO.getLng())).icon(BitmapDescriptorFactory.fromResource(R.drawable.streetmarker)));
                        builder.include(new LatLng(smarterParkBO.getLat(), smarterParkBO.getLng()));
                        markersList.add(marker);
                    } else {
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .title(smarterParkBO.getName())
                                .snippet("Empty Spot:" + smarterParkBO.getEmptySlots() + " Price:" + smarterParkBO.getRate() + "$" + " " + smarterParkBO.getDeviceType())
                                .position(new LatLng(smarterParkBO.getLat(), smarterParkBO.getLng())).icon(BitmapDescriptorFactory.fromResource(R.drawable.garagemarker)));
                        builder.include(new LatLng(smarterParkBO.getLat(), smarterParkBO.getLng()));
                        markersList.add(marker);
                    }

                }


                // You can customize the marker image using images bundled with
                // your app, or dynamically generated bitmaps.
                LatLngBounds bounds;
                bounds = builder.build();
                int padding = 80; // offset from edges of the map in pixels


                //Adding current Location code
                if (mMap != null) {
                    builder.include(MyLocationMarker.getPosition());
                    bounds = builder.build();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mMap.animateCamera(cu);
                    mMap.setTrafficEnabled(false);
                }

            }
        }
    }

    private GoogleMap.OnMarkerClickListener myMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(final Marker marker) {
            marker.showInfoWindow();
            showRoute task = new showRoute();
            LatLng[] points = new LatLng[2];
            points[0] =MyLocationMarker.getPosition();
            points[1] = marker.getPosition();
            task.execute(points);
           return true;
        };
    };
}
