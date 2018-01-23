package com.parent.domain;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class AppKeyInterceptor implements Interceptor {

    private static final String APP_ID_KEY = "appid";
    private static final String APP_ID_VALUE = "cc8bf0ef9fefd3794a362f69e9b0721d";

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        return com.chaining.Chain.let(chain.request())
                .pair(Request::url)
                .map(pair -> requestWithAppKey(pair.getValue0(), pair.getValue1()))
                .map(chain::proceed)
                .call();
    }

    private Request requestWithAppKey(Request original, HttpUrl originalHttpUrl) {
        return original.newBuilder()
                .url(httpUrl(originalHttpUrl))
                .build();
    }

    @NonNull
    private HttpUrl httpUrl(HttpUrl originalHttpUrl) {
        return originalHttpUrl.newBuilder()
                .addQueryParameter(APP_ID_KEY, APP_ID_VALUE)
                .build();
    }
}
