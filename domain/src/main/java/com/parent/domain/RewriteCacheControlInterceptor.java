package com.parent.domain;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

class RewriteCacheControlInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (new NetworkAvailable().call()) {
            return readFromCacheForFifteenMinutes(originalResponse);
        } else {
            return readFromCacheForFiveDays(originalResponse);
        }
    }

    private Response readFromCacheForFifteenMinutes(Response originalResponse) {
        int maxAgeFifteenMinutes = 60 * 15;
        return originalResponse.newBuilder()
                .header("Cache-Control", "public, max-age=" + maxAgeFifteenMinutes)
                .build();
    }

    private Response readFromCacheForFiveDays(Response originalResponse) {
        int maxStaleFiveDays = 60 * 60 * 24 * 5;
        return originalResponse.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStaleFiveDays)
                .build();
    }
}
