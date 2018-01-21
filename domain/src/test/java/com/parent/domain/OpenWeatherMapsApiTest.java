package com.parent.domain;

import com.chaining.Chain;
import com.parent.entities.City;
import com.parent.entities.ForecastsResponse;

import org.junit.Assert;
import org.junit.Test;

import io.reactivex.Observable;


public class OpenWeatherMapsApiTest {

    @Test
    public void requestFiveDayForecastFromTestDoubleThenReturnValidForecastResponse() {
        Chain.let(new MockOpenWeatherMapApi())
                .map(OpenWeatherMapsApi::requestFiveDayForecast)
                .map(Observable::blockingFirst)
                .map(ForecastsResponse::getCity)
                .map(City::getName)
                .map(MockOpenWeatherMapApi.MOCK_RESPONSE_CITY_NAME::equals)
                .apply(Assert::assertTrue);
    }

}