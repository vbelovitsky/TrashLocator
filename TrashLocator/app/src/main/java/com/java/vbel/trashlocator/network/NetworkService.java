package com.java.vbel.trashlocator.network;


import com.java.vbel.trashlocator.api.TestServerApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    private Retrofit mRetrofit;

    private NetworkService(String BASE_URL) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkService getInstance(String BASE_URL) {
        if (mInstance == null) {
            mInstance = new NetworkService(BASE_URL);
        }
        return mInstance;
    }


    public TestServerApi getTestApi(){return mRetrofit.create(TestServerApi.class);}

}
