
package com.parent.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast {

    @SerializedName("clouds")
    private final Clouds clouds;
    @SerializedName("dt")
    private final Long date;
    @SerializedName("dt_txt")
    private final String dateText;
    @SerializedName("main")
    private final ForecastDetails details;
    @SerializedName("snow")
    private final Snow snow;
    @SerializedName("weather")
    private final List<Weather> weather;
    @SerializedName("wind")
    private final Wind wind;

    public Forecast(Clouds clouds, Long date, String dateText, ForecastDetails details,
                    Snow snow, List<Weather> weather, Wind wind) {
        this.clouds = clouds;
        this.date = date;
        this.dateText = dateText;
        this.details = details;
        this.snow = snow;
        this.weather = weather;
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public Long getDate() {
        return date;
    }

    public String getDateText() {
        return dateText;
    }

    public ForecastDetails getDetails() {
        return details;
    }

    public Snow getSnow() {
        return snow;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public Wind getWind() {
        return wind;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "clouds=" + clouds +
                ", date=" + date +
                ", dateText='" + dateText + '\'' +
                ", details=" + details +
                ", snow=" + snow +
                ", weather=" + weather +
                ", wind=" + wind +
                '}';
    }
}
