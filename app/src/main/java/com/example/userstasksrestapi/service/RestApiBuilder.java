package com.example.userstasksrestapi.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.File;

import okhttp3.Cache;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class RestApiBuilder {

    private Context mContext;

    public boolean hasNetwork(Context context) {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected())
            isConnected = true;
        return isConnected;
    }

    public static final String API_BASE_URL = "https://jsonplaceholder.typicode.com";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder().baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private Cache provideCache() {
        Cache cache = null;

        try {
            //Cache myCache = new Cache(mContext.getCacheDir(), cacheSize);
            int cacheSize = 10 * 1024 * 1024;
            cache = new Cache(new File(mContext.getCacheDir(), "http-cache"), cacheSize);
        } catch (Exception e) {
            Log.e(TAG, "Could not create Cache!");
        }

        return cache;
    }


    public RestApiService getService() {
        return retrofit.create(RestApiService.class);
    }

}
