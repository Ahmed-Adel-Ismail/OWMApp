package com.parent.domain;

import android.content.ContextWrapper;
import android.support.annotation.NonNull;

import com.functional.curry.SwapCurry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.parent.entities.City;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.functions.Function;


class CitySearcher implements Function<String, List<City>> {

    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    @Override
    public List<City> apply(@NonNull String cityName) throws Exception {
        List<City> results = new LinkedList<>();
        JsonReader reader = createJsonReader();
        try {
            searchInFile(cityName, results, reader);
        } finally {
            reader.close();
        }
        return results;
    }

    private void searchInFile(@NonNull String cityName, List<City> results, JsonReader reader)
            throws IOException {

        Gson gson = new GsonBuilder().create();
        reader.beginArray();
        while (reader.hasNext()) {
            searchByCityName(cityName, results, reader, gson);
        }
    }

    private void searchByCityName(@NonNull String cityName, List<City> results,
                                  JsonReader reader, Gson gson) {
        City city = gson.fromJson(reader, City.class);
        if (isCityNameMatching(cityName, city)) {
            results.add(city);
        }
    }

    private boolean isCityNameMatching(@NonNull String cityName, City city) {
        return !cityName.trim().isEmpty()
                && city.getName().toLowerCase().contains(cityName.trim().toLowerCase());
    }

    protected JsonReader createJsonReader() {
        return Domain.getApplication()
                .map(ContextWrapper::getResources)
                .map(resources -> resources.openRawResource(R.raw.city_list))
                .map(SwapCurry.toFunction(InputStreamReader::new, "UTF-8"))
                .map(JsonReader::new)
                .defaultIfEmpty(null)
                .call();
    }
}
