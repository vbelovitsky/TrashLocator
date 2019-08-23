package com.java.vbel.trashlocator.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("message")
    @Expose
    private String message;


    //region getters and setters
    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }
    //endregion
}
