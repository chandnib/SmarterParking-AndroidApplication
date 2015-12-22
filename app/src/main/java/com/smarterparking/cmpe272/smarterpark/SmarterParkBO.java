package com.smarterparking.cmpe272.smarterpark;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Parteek on 11/18/2015.
 */
public class SmarterParkBO implements Comparable<SmarterParkBO>, Serializable{
    public static int radius =1;

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    private String deviceType;
    int capacity;

    private String name;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    private int distance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getEmptySlots() {
        return emptySlots;
    }

    public void setEmptySlots(int emptySlots) {
        this.emptySlots = emptySlots;
    }
    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return Lng;
    }

    public void setLng(Double lng) {
        Lng = lng;
    }

    public static int getRadius() {
        return radius;
    }

    public static void setRadius(int radius) {
        SmarterParkBO.radius = radius;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    int emptySlots ;
    private Double lat ;
    private Double Lng ;
    int rate;

    @Override
    public int compareTo(SmarterParkBO another) {
        return ( this.getDistance() < another.getDistance() ? +1 : this.getDistance() < another.getDistance() ? -1 : 0);
    }

    @Override
    public String toString() {
        return "SmarterParkBO{" +
                "deviceType='" + deviceType + '\'' +
                ", capacity=" + capacity +
                ", name='" + name + '\'' +
                ", distance=" + distance +
                ", emptySlots=" + emptySlots +
                ", lat=" + lat +
                ", Lng=" + Lng +
                ", rate=" + rate +
                '}';
    }
}