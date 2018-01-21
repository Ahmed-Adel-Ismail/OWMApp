package com.parent.entities;

import com.google.gson.annotations.SerializedName;

public class Coordinates {

    @SerializedName("lat")
    private final Double latitude;
    @SerializedName("lon")
    private final Double longitude;

    public Coordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}



