package com.parent.domain;


import com.parent.entities.ForecastsResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface OpenWeatherMapsApi {

    String APP_ID = "cc8bf0ef9fefd3794a362f69e9b0721d";
    String BASE_URL = "api.openweathermap.org/";


    @GET("/data/2.5/forecast?id=524901&appid=" + APP_ID)
    Observable<ForecastsResponse> requestFiveDayForecast();

}
