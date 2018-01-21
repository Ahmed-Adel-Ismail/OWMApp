package com.parent.entities;

import com.google.gson.annotations.SerializedName;

public class Coordinates {

    @SerializedName("lat")
    public final Double latitude;
    @SerializedName("lon")
    public final Double longitude;

    public Coordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}



