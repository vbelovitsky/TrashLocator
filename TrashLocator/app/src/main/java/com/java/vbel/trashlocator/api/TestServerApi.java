package com.java.vbel.trashlocator.api;

import com.java.vbel.trashlocator.dto.CategoryItem;
import com.java.vbel.trashlocator.dto.PointImageSend;
import com.java.vbel.trashlocator.dto.PointInfo;
import com.java.vbel.trashlocator.dto.PointMarker;
import com.java.vbel.trashlocator.dto.PointSend;
import com.java.vbel.trashlocator.models.Image;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface TestServerApi {

    @GET("point/{pk}")
    Call<PointInfo> getPoint(@Path("pk") long pk);

    @GET("points")
    Call<List<PointMarker>> getAllPoints();

    @POST("point")
    Call<Void> postPoint(@Body PointSend point);

    @POST("point-with-image")
    Call<Void> postPointWithImage(@Body PointImageSend point);

    @GET("categories")
    Call<List<CategoryItem>> getAllCategories();



}
