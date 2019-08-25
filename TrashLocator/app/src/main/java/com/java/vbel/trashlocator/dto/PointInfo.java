package com.java.vbel.trashlocator.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class PointInfo {

    @SerializedName("userName")
    @Expose
    private String userName;

    @SerializedName("categoryTitle")
    @Expose
    private String categoryTitle;

    @SerializedName("date")
    private LocalDateTime date;



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String  getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String  categoryId) {
        this.categoryTitle = categoryId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
