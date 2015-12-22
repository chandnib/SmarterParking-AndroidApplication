package com.smarterparking.cmpe272.smarterpark;


import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Parteek on 11/18/2015.
 */

public class DrawDirections {

    public ArrayList<LatLng> getPolyLine(LatLng origin, LatLng destination) throws JSONException {
        String url=
                "http://maps.googleapis.com/maps/api/directions/json?origin="
                        + origin.latitude + "," + origin.longitude +"&destination="
                        + destination.latitude + "," + destination.longitude + "&sensor=false";
        HttpResponse response;
        HttpGet request;
        AndroidHttpClient client = AndroidHttpClient.newInstance("direct");

        request = new HttpGet(url);
        String returnValue ="";
        try {
            response = client.execute(request);
            InputStream source = response.getEntity().getContent();
            returnValue  = buildStringIOutils(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            client.close();
        }

        JSONObject result = new JSONObject(returnValue);
        JSONArray routes = result.getJSONArray("routes");

        long distanceForSegment = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getInt("value");

        JSONArray steps = routes.getJSONObject(0).getJSONArray("legs")
                .getJSONObject(0).getJSONArray("steps");

        ArrayList<LatLng> lines = new ArrayList<LatLng>();

        for(int i=0; i < steps.length(); i++) {
            String polyline = steps.getJSONObject(i).getJSONObject("polyline").getString("points");

            for(LatLng p : decodePolyline(polyline)) {
                lines.add(p);
            }
        }



        return lines;
    }


    private String buildStringIOutils(InputStream is) {
        try {
            return org.apache.commons.io.IOUtils.toString(is, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** POLYLINE DECODER -  **/
    private List<LatLng> decodePolyline(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();

        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(p);
        }

        return poly;
    }





}
