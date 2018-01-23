package com.parent.domain;

import android.content.Context;

import com.actors.ActorSystem;
import com.chaining.Chain;
import com.functional.curry.Curry;
import com.functional.curry.SwapCurry;

import java.io.File;

import io.reactivex.functions.BiConsumer;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

class RepositoryInitializer implements BiConsumer<Interceptor, Context> {

    private static final String OPEN_WEATHER_MAPS_BASE_URL = "http://api.openweathermap.org";
    private static final String CACHE_DIRECTORY_NAME = "responses";
    private static final long CACHE_DIRECTORY_SIZE_MB = 10 * 1024 * 1024;

    @Override
    public void accept(Interceptor offlineInterceptor, Context context) {
        Chain.let(offlineInterceptor)
                .map(Curry.toFunction(this::okHttpClient, cache(context)))
                .map(this::openWeatherMapsApi)
                .map(SwapCurry.toFunction(Repository::new, new CitySearcher()))
                .apply(ActorSystem::register);
    }

    private Cache cache(Context context) {
        return Chain.let(context)
                .map(Context::getCacheDir)
                .map(SwapCurry.toFunction(File::new, CACHE_DIRECTORY_NAME))
                .map(SwapCurry.toFunction(Cache::new, CACHE_DIRECTORY_SIZE_MB))
                .call();
    }

    private OkHttpClient okHttpClient(Cache cache, Interceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new AppKeyInterceptor())
                .cache(cache)
                .build();
    }

    private FiveDayForecastRequester openWeatherMapsApi(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(OPEN_WEATHER_MAPS_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(FiveDayForecastRequester.class);
    }


}
