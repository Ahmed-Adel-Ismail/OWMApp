package com.parent.domain;


import com.parent.entities.ForecastsResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface FiveDayForecastRequester {

    @GET("/data/2.5/forecast")
    Observable<ForecastsResponse> apply(@Query("id") Long id);

}
