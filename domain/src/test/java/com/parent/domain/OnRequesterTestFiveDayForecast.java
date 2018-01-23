package com.parent.domain;

import com.chaining.Chain;
import com.parent.entities.City;
import com.parent.entities.ForecastsResponse;

import org.junit.Assert;
import org.junit.Test;

import io.reactivex.Observable;


public class OnRequesterTestFiveDayForecast {

    @Test
    public void applyFromTestDoubleThenReturnValidForecastResponse() {
        Chain.let(new MockFiveDayForecastRequester())
                .map(api -> api.apply(0L))
                .map(Observable::blockingFirst)
                .map(ForecastsResponse::getCity)
                .map(City::getName)
                .map(MockFiveDayForecastRequester.MOCK_RESPONSE_CITY_NAME::equals)
                .apply(Assert::assertTrue);
    }

}