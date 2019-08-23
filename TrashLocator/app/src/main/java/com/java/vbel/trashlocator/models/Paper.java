package com.java.vbel.trashlocator.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Paper {

    @SerializedName("title")
    @Expose
    private String title;


    //region getters and setters
    public String  getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    //endregion
}
