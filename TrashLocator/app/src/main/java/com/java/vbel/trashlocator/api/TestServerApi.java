package com.java.vbel.trashlocator.api;

import com.java.vbel.trashlocator.dto.PointInfo;
import com.java.vbel.trashlocator.dto.PointMarker;
import com.java.vbel.trashlocator.dto.PointSend;
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

    @GET("point/{pk}")
    public Call<PointInfo> getPoint(@Path("pk") long pk);

    @GET("points")
    public Call<List<PointMarker>> getAllPoints();

    @POST("point/create")
    public Call<Void> postPoint(@Body PointSend point);


}
