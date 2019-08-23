package com.java.vbel.trashlocator.temporary;

import com.java.vbel.trashlocator.models.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JSONPlaceHolderApi {
    @GET("posts/{id}")
    public Call<Post> getPostWithID(@Path("id") int id);

    @GET("posts")
    public Call<List<Post>> getAllPosts();

    @GET("posts")
    public Call<List<Post>> getPostOfUser(@Query("userId") int id);

    @POST("posts")
    public Call<Post> postData(@Body Post data);

    @DELETE("posts/{id}")
    public Call<POST> postDelete(@Path("id") int id);
}
