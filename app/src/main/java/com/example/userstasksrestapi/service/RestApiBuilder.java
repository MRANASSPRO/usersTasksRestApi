package com.example.userstasksrestapi.service;

import android.content.Context;

import com.example.userstasksrestapi.interceptor.CachingControlInterceptor;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiBuilder {
    public static final String API_BASE_URL = "https://jsonplaceholder.typicode.com";
    public static final int CACHE_SIZE = 10 * 1024 * 1024;
    private static Retrofit retrofit;
    private static Context mContext;

    public RestApiBuilder(Context context) {
        mContext = context;
    }

    public static Retrofit getRetrofitInstance(Context mContext) {
        Cache cache = new Cache(new File(mContext.getCacheDir(), "http"), CACHE_SIZE);
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.cache();

        //add an interceptor to access cached data
        httpClient.newBuilder().addInterceptor(new CachingControlInterceptor());

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public RestApiService getService() {
        return retrofit.create(RestApiService.class);
    }
}
