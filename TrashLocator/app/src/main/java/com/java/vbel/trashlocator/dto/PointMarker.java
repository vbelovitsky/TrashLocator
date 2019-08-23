package com.java.vbel.trashlocator.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PointMarker {

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("lat")
    @Expose
    private double lat;

    @SerializedName("lng")
    @Expose
    private double lng;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
