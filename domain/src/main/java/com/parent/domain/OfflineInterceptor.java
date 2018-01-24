package com.parent.domain;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class OfflineInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if (new NetworkAvailable().call()) {
            request = readFromCacheForFifteenMinutes(request);
        } else {
            request = readFromCacheForFiveDays(request);
        }
        return chain.proceed(request);
    }

    private Request readFromCacheForFifteenMinutes(Request request) {
        int maxAgeFifteenMinutes = 60 * 15;
        return request.newBuilder()
                .header("Cache-Control", "public, max-age=" + maxAgeFifteenMinutes)
                .build();
    }

    private Request readFromCacheForFiveDays(Request request) {
        int maxStaleFiveDays = 60 * 60 * 24 * 5;
        return request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStaleFiveDays)
                .build();
    }


}
