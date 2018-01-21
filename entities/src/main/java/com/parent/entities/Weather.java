
package com.parent.entities;

import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("description")
    private final String description;
    @SerializedName("icon")
    private final String icon;
    @SerializedName("id")
    private final Long id;
    @SerializedName("main")
    private String state;

    public Weather(String description, String icon, Long id, String state) {
        this.description = description;
        this.icon = icon;
        this.id = id;
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public Long getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", id=" + id +
                ", state='" + state + '\'' +
                '}';
    }
}
