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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;

        City city = (City) o;

        return (id != null ? id.equals(city.id) : city.id == null)
                && (name != null ? name.equals(city.name) : city.name == null)
                && (country != null ? country.equals(city.country) : city.country == null)
                && (coordinates != null ? coordinates.equals(city.coordinates) : city.coordinates == null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
        return result;
    }
}
