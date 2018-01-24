package com.parent.domain;

import com.chaining.Chain;
import com.parent.entities.Clouds;
import com.parent.entities.Forecast;
import com.parent.entities.ForecastDetails;
import com.parent.entities.ForecastSummery;
import com.parent.entities.Weather;
import com.parent.entities.Wind;

import java.util.List;

import io.reactivex.functions.Function;


public class ForecastSummeryParser implements Function<Forecast, ForecastSummery> {

    @Override
    public ForecastSummery apply(Forecast forecast) {
        return new ForecastSummery.Builder()
                .dateText(dateText(forecast))
                .cloudiness(cloudiness(forecast))
                .humidity(humidity(forecast))
                .temperature(temperature(forecast))
                .weather(weather(forecast))
                .windSpeed(windSpeed(forecast))
                .build();
    }

    private String dateText(Forecast forecast) {
        return Chain.optional(forecast)
                .map(Forecast::getDateText)
                .defaultIfEmpty("")
                .call();
    }

    private String cloudiness(Forecast forecast) {
        return Chain.optional(forecast)
                .map(Forecast::getClouds)
                .map(Clouds::getCloudiness)
                .map(value -> value + "%")
                .defaultIfEmpty("")
                .call();
    }

    private String humidity(Forecast forecast) {
        return Chain.optional(forecast)
                .map(Forecast::getDetails)
                .map(ForecastDetails::getHumidity)
                .map(value -> value + "%")
                .defaultIfEmpty(null)
                .call();
    }

    private String temperature(Forecast forecast) {
        return Chain.optional(forecast)
                .map(Forecast::getDetails)
                .map(ForecastDetails::getTemperature)
                .map(Double::intValue)
                .map(value -> value + "K")
                .defaultIfEmpty("")
                .call();
    }

    private String weather(Forecast forecast) {
        return Chain.optional(forecast)
                .map(Forecast::getWeather)
                .whenNot(List::isEmpty)
                .thenMap(list -> list.get(0))
                .map(Weather::getDescription)
                .defaultIfEmpty("")
                .call();
    }

    private String windSpeed(Forecast forecast) {
        return Chain.optional(forecast)
                .map(Forecast::getWind)
                .map(Wind::getSpeed)
                .map(value -> value + " Km")
                .defaultIfEmpty("")
                .call();
    }
}
