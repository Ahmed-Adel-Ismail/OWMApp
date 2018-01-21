package com.parent.entities;


import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("id")
    public final Long id;
    @SerializedName("name")
    public final String name;
    @SerializedName("country")
    public final String country;
    @SerializedName("coord")
    public final Coordinates coordinates;

    public City(Long id, String name, String country, Coordinates coordinates) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }
}
