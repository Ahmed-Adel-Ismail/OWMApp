
package com.parent.entities;

import com.google.gson.annotations.SerializedName;

public class ForecastDetails {

    @SerializedName("grnd_level")
    private final Double grandLevel;
    @SerializedName("humidity")
    private final Long humidity;
    @SerializedName("pressure")
    private final Double pressure;
    @SerializedName("sea_level")
    private final Double seaLevel;
    @SerializedName("temp")
    private final Double temperature;
    @SerializedName("temp_max")
    private final Double maximumTemperature;
    @SerializedName("temp_min")
    private final Double minimumTemperature;

    public ForecastDetails(Double grandLevel, Long humidity, Double pressure, Double seaLevel,
                           Double temperature, Double maximumTemperature, Double minimumTemperature) {
        this.grandLevel = grandLevel;
        this.humidity = humidity;
        this.pressure = pressure;
        this.seaLevel = seaLevel;
        this.temperature = temperature;
        this.maximumTemperature = maximumTemperature;
        this.minimumTemperature = minimumTemperature;
    }

    public Double getGrandLevel() {
        return grandLevel;
    }

    public Long getHumidity() {
        return humidity;
    }

    public Double getPressure() {
        return pressure;
    }

    public Double getSeaLevel() {
        return seaLevel;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Double getMaximumTemperature() {
        return maximumTemperature;
    }

    public Double getMinimumTemperature() {
        return minimumTemperature;
    }

    @Override
    public String toString() {
        return "ForecastDetails{" +
                "grandLevel=" + grandLevel +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", seaLevel=" + seaLevel +
                ", temperature=" + temperature +
                ", maximumTemperature=" + maximumTemperature +
                ", minimumTemperature=" + minimumTemperature +
                '}';
    }
}
