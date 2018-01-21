
package com.parent.entities;

import com.google.gson.annotations.SerializedName;


public class Wind {

    @SerializedName("deg")
    private final Double degree;
    @SerializedName("speed")
    private final Double speed;

    public Wind(Double degree, Double speed) {
        this.degree = degree;
        this.speed = speed;
    }

    public Double getDegree() {
        return degree;
    }

    public Double getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        return "Wind{" +
                "degree=" + degree +
                ", speed=" + speed +
                '}';
    }
}
