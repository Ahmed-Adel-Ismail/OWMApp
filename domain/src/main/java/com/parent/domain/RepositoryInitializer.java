package com.parent.domain;

import com.actors.ActorSystem;
import com.chaining.Chain;

import io.reactivex.functions.Action;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

class RepositoryInitializer implements Action {

    @Override
    public void run() {
        Chain.let(openWeatherMapsApi())
                .map(Repository::new)
                .apply(ActorSystem::register)
                .call();
    }

    private OpenWeatherMapsApi openWeatherMapsApi() {
        return new Retrofit.Builder()
                .baseUrl(OpenWeatherMapsApi.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherMapsApi.class);
    }
}
