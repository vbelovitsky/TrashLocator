package com.java.vbel.trashlocator.api;

import com.java.vbel.trashlocator.dto.CategoryItem;
import com.java.vbel.trashlocator.dto.PointInfo;
import com.java.vbel.trashlocator.dto.PointMarker;
import com.java.vbel.trashlocator.dto.PointSend;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TestServerApi {

    @GET("point/{pk}")
    Call<PointInfo> getPoint(@Path("pk") long pk);

    @GET("points")
    Call<List<PointMarker>> getAllPoints();

    @POST("point")
    Call<Void> postPoint(@Body PointSend point);

    @GET("categories")
    Call<List<CategoryItem>> getAllCategories();



}
