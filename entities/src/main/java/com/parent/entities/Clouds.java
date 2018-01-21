
package com.parent.entities;

import com.google.gson.annotations.SerializedName;

public class Clouds {

    @SerializedName("all")
    private final Long cloudiness;

    public Clouds(Long cloudiness) {
        this.cloudiness = cloudiness;
    }

    public Long getCloudiness() {
        return cloudiness;
    }

    @Override
    public String toString() {
        return "Clouds{" +
                "cloudiness=" + cloudiness +
                '}';
    }
}
