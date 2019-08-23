package com.java.vbel.trashlocator.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Point {

    @SerializedName("userId")
    @Expose
    private long userId;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("coordinates")
    @Expose
    private double[] coordinates;

    @SerializedName("category")
    @Expose
    private String category;





    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
