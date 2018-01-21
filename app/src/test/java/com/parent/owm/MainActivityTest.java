package com.parent.owm;

import com.google.gson.Gson;
import com.parent.entities.City;

import org.junit.Test;

public class MainActivityTest {

    @Test
    public void main() {
        String json = "{" +
                "        \"id\": 1283378," +
                "            \"name\": \"Gorkhā\"," +
                "            \"country\": \"NP\"," +
                "            \"coord\": {" +
                "        \"lon\": 84.633331," +
                "                \"lat\": 28" +
                "    }" +
                "    }";

        City city = new Gson().fromJson(json, City.class);
        System.out.println(city);
    }

}