package com.parent.domain;

import android.content.ContextWrapper;
import android.support.annotation.NonNull;

import com.chaining.Chain;
import com.chaining.Optional;
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


class CitySearcher implements Function<String, Optional<City>> {

    private boolean searching;

    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    @Override
    public Optional<City> apply(@NonNull String cityName) throws Exception {
        searching = false;
        LinkedList<City> results = new LinkedList<>();
        JsonReader reader = createJsonReader();
        try {
            searchInFile(cityName, results, reader);
        } finally {
            reader.close();
        }
        return Chain.optional(results)
                .whenNot(List::isEmpty)
                .thenMap(LinkedList::pop);
    }

    private void searchInFile(@NonNull String cityName, List<City> results, JsonReader reader)
            throws IOException {

        Gson gson = new GsonBuilder().create();
        reader.beginArray();
        searching = true;
        while (reader.hasNext()) {
            if (!searching) throw new StopCitySearchException();
            if (searchByCityName(cityName, results, reader, gson)) return;
        }
    }

    private boolean searchByCityName(@NonNull String cityName, List<City> results,
                                     JsonReader reader, Gson gson) {
        City city = gson.fromJson(reader, City.class);
        if (isSameCityName(cityName, city)) {
            results.add(city);
            return true;
        } else {
            return false;
        }

    }

    private boolean isSameCityName(@NonNull String cityName, City city) {
        return !cityName.trim().isEmpty()
                && city.getName().trim().equalsIgnoreCase(cityName.trim());
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
