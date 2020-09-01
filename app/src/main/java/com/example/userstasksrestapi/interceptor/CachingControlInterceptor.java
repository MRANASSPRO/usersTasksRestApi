package com.example.userstasksrestapi.interceptor;

import android.content.Context;

import com.example.userstasksrestapi.utils.Utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CachingControlInterceptor implements Interceptor {
    private Context theContext;

    public CachingControlInterceptor(Context theContext) {
        this.theContext = theContext;
    }

    public CachingControlInterceptor() {
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        if (request.method().equals("GET")) {
            if (Utils.isConnected(this.theContext)) {
                request = request.newBuilder()
                        .header("Cache-Control", "only-if-cached")
                        .build();
            } else {
                //Cache Control to retrieve data when offline
                request = request.newBuilder()
                        .header("Cache-Control", "public, max-stale=2419200")
                        .build();
            }
        }

        Response originalResponse = chain.proceed(request);
        return originalResponse.newBuilder()
                .header("Cache-Control", "max-age=600")
                .build();
    }
}
