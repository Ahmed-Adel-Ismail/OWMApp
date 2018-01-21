
package com.parent.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastsResponse {

    @SerializedName("city")
    private final City city;
    @SerializedName("cnt")
    private final Long count;
    @SerializedName("list")
    private final List<Forecast> forecasts;

    public ForecastsResponse(City city, Long count, List<Forecast> forecasts) {
        this.city = city;
        this.count = count;
        this.forecasts = forecasts;
    }

    public City getCity() {
        return city;
    }

    public Long getCount() {
        return count;
    }

    public List<Forecast> getForecasts() {
        return forecasts;
    }

    @Override
    public String toString() {
        return "ForecastsResponse{" +
                "city=" + city +
                ", count=" + count +
                ", forecasts=" + forecasts +
                '}';
    }
}
