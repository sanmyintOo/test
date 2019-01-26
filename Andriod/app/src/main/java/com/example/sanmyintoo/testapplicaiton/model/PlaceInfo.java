package com.example.sanmyintoo.testapplicaiton.model;

import com.google.android.gms.maps.model.LatLng;

public class PlaceInfo {
    private String name;
    private String address;
    private String phoneni;
    private String id;
    private LatLng latLng;
    private float rating;

    public PlaceInfo(String name, String address, String phoneni, String id, LatLng latLng, float rating) {
        this.name = name;
        this.address = address;
        this.phoneni = phoneni;
        this.id = id;
        this.latLng = latLng;
        this.rating = rating;
    }
    public PlaceInfo() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneni() {
        return phoneni;
    }

    public void setPhoneni(String phoneni) {
        this.phoneni = phoneni;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneni='" + phoneni + '\'' +
                ", id='" + id + '\'' +
                ", latLng=" + latLng +
                ", rating=" + rating +
                '}';
    }
}
