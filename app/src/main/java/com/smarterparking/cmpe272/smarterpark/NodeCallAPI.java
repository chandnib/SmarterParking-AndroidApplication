package com.smarterparking.cmpe272.smarterpark;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Parteek on 11/18/2015.
 */
public class NodeCallAPI {

    public String requestContent(String url) {
        Log.d("urld",url);
        HttpClient httpclient = new DefaultHttpClient();
        String result = null;
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = null;
        InputStream instream = null;

        try {

            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                instream = entity.getContent();

                result = convertStreamToString(instream);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (instream != null) {
                try {
                    instream.close();
                } catch (Exception exc) {
                      exc.printStackTrace();
                }
            }
        }

        return result;
    }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

   public ArrayList<SmarterParkBO> parseJsonInBO(String result){

        ArrayList<SmarterParkBO> smarterParkBoList = new ArrayList<SmarterParkBO>();

        try {
            //JSONObject json = new JSONObject (result);
            Log.d("result12", result);
            JSONArray jsonArr = new JSONArray(result);
            Log.d("jsonarraylenght",Integer.toString(jsonArr.length()));
            for(int i=0;i<jsonArr.length();i++) {
                SmarterParkBO smarterParkBO = new SmarterParkBO();
                 JSONObject jsonOBJ = jsonArr.getJSONObject(i);
                if(jsonOBJ.has("device_type")) {
                    smarterParkBO.setDeviceType(jsonOBJ.get("device_type").toString());
                }
                if(jsonOBJ.has("capacity")) {
                    smarterParkBO.setCapacity(Integer.parseInt(jsonOBJ.get("capacity").toString()));
                }
                if(jsonOBJ.has("empty_spots")) {
                        smarterParkBO.setEmptySlots(Integer.parseInt(jsonOBJ.get("empty_spots").toString()));
                }
                if(jsonOBJ.has("latitude"))
                    smarterParkBO.setLat(Double.parseDouble(jsonOBJ.get("latitude").toString()));
                if(jsonOBJ.has("longitude"))
                    smarterParkBO.setLng(Double.parseDouble(jsonOBJ.get("longitude").toString()));
                if(jsonOBJ.has("rate")) {
                    //smarterParkBO.setRate(Integer.parseInt(jsonOBJ.get("rate").toString()));
                    smarterParkBO.setRate(10);
                }
                else{
                    smarterParkBO.setRate(0);
                }
                if(jsonOBJ.has("id_no")) {
                    smarterParkBO.setName(jsonOBJ.get("id_no").toString());
                }
                else{
                    smarterParkBO.setName("");
                }
                smarterParkBoList.add(smarterParkBO);
                Log.d("call12", "1");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
       return smarterParkBoList;
   }
}
