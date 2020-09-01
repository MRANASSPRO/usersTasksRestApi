package com.example.userstasksrestapi.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiBuilder {
    public static final String API_BASE_URL = "https://jsonplaceholder.typicode.com";
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_PRAGMA = "Pragma";
    public static final String TAG = "RetrofitManager";
    public static final int CACHE_SIZE = 10 * 1024 * 1024;
    private static Retrofit retrofit, mCachedRetrofit;
    private static OkHttpClient mCachedOkHttpClient;
    private static Cache mCache;
    private static Context mContext;


    public RestApiBuilder(Context context) {
        mContext = context;
    }

    public static Retrofit getRetrofitInstance(Context mContext) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getHttpClient(mContext))
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getCachedRetrofit() {
        if (mCachedRetrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .addInterceptor(provideForcedOfflineCacheInterceptor())
                    .cache(allocateCache());

            mCachedOkHttpClient = httpClient.build();

            mCachedRetrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .client(mCachedOkHttpClient)
                    .build();
        }

        return mCachedRetrofit;
    }

    private static OkHttpClient getHttpClient(Context context) {
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder()
                .addInterceptor(getCacheInterceptor(context))
                .cache(getCache(context));

        return httpBuilder.build();
    }

    private static Interceptor provideForcedOfflineCacheInterceptor() {
        return chain -> {
            Request request = chain.request();

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .build();

            request = request.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build();

            return chain.proceed(request);
        };
    }

    private static Interceptor getCacheInterceptor(final Context context) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                if (!RestApiBuilder.hasNetwork(context)) {
                    Request request = chain.request();

                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(1, TimeUnit.DAYS)
                            .build();

                    request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build();

                    return chain.proceed(request);
                } else {
                    Response response = chain.proceed(chain.request());

                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxAge(1, TimeUnit.HOURS)
                            .build();

                    return response.newBuilder()
                            .header("Cache-Control", cacheControl.toString())
                            .build();
                }
            }
        };
    }

    public RestApiService getService() {
        return retrofit.create(RestApiService.class);
    }

    public static boolean hasNetwork(Context context) {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected())
            isConnected = true;
        return isConnected;
    }

    private static Cache allocateCache() {
        if (mCache == null) {
            try {
                mCache = new Cache(new File(mContext.getCacheDir(), "http-cache"), CACHE_SIZE
                );
            } catch (Exception e) {
                Log.e(TAG, "Could not create Cache!");
            }
        }

        return mCache;
    }

    private static Cache getCache(Context context) {
        Cache cache = null;
        try {
            return new Cache(context.getCacheDir(), CACHE_SIZE);
        } catch (Exception e) {
            Log.e(TAG, "Could not create Cache!");
        }
        return cache;
    }

}
