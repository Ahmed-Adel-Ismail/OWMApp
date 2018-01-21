
package com.parent.entities;

import com.google.gson.annotations.SerializedName;

public class Snow {

    @SerializedName("3h")
    private final Double volumeForLastThreeHours;

    public Snow(Double volumeForLastThreeHours) {
        this.volumeForLastThreeHours = volumeForLastThreeHours;
    }

    public Double getVolumeForLastThreeHours() {
        return volumeForLastThreeHours;
    }

    @Override
    public String toString() {
        return "Snow{" +
                "volumeForLastThreeHours=" + volumeForLastThreeHours +
                '}';
    }
}
