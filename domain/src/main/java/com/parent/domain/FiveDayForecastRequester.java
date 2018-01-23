package com.parent.domain;


import com.parent.entities.ForecastsResponse;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface FiveDayForecastRequester extends Function<Long, Observable<ForecastsResponse>> {

    @GET("/data/2.5/forecast")
    Observable<ForecastsResponse> apply(@Query("id") Long id);

}
