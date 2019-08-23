package com.java.vbel.trashlocator.api;

import com.java.vbel.trashlocator.models.Message;
import com.java.vbel.trashlocator.models.Paper;
import com.java.vbel.trashlocator.models.Point;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TestServerApi {
//    @GET("test")
//    public Call<Paper> getPaper();

    @GET("point/{pk}")
    public Call<Point> getPoint(@Path("pk") int pk);

    @GET("points")
    public Call<List<Point>> getAllPoints();

    @POST("point/create")
    public Call<Void> postPoint(@Body Point point);


}
