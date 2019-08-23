package com.java.vbel.trashlocator.temporary;

import com.java.vbel.trashlocator.models.Message;
import com.java.vbel.trashlocator.models.Paper;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface VbelServerApi {
    @GET("papers")
    public Call<List<Paper>> getAllPapers();

    @GET("paper/{pk}")
    public Call<Paper> getPaper(@Path("pk") int pk);

    @POST("paper/create")
    public Call<Message> createPaper(@Body Paper data);

    @POST("paper/update/{pk}")
    public Call<Message> updatePaper(@Path("pk") int pk, @Body Paper data);

    @POST("paper/delete/{pk}")
    public Call<Message> deletePaper(@Path("pk") int pk);
}
