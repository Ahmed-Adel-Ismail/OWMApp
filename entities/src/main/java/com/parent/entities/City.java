package com.parent.entities;


import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("id")
    private final Long id;
    @SerializedName("name")
    private final String name;
    @SerializedName("country")
    private final String country;
    @SerializedName("coord")
    private final Coordinates coordinates;

    public City(Long id, String name, String country, Coordinates coordinates) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.coordinates = coordinates;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public Coordinates getCoordinates() {
        return coordinates;
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
