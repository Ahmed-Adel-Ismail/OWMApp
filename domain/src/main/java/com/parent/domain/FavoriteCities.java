package com.parent.domain;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.chaining.Chain;
import com.functional.curry.SwapCurry;
import com.google.gson.Gson;
import com.parent.entities.City;
import com.parent.entities.Coordinates;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;

import static android.content.Context.MODE_PRIVATE;

public class FavoriteCities {

    private static final String KEY_FAVORITE_CITIES = "com.parent.domain.KEY_FAVORITE_CITIES";
    private static final String PREFERENCES_NAME = "CitiesPreferences";

    private FavoriteCities() {

    }

    public static LinkedList<City> defaultList() {
        return Chain.let(new Coordinates(-0.12574D, 51.50853D))
                .map(coordinates -> new City(2643743L, "London", "GB", coordinates))
                .collect(City.class)
                .flatMap(Observable::fromIterable)
                .toList(LinkedList::new)
                .blockingGet();
    }

    public static Saver save(LinkedList<City> cities) {
        return new Saver(cities);
    }

    public static Loader load() {
        return new Loader();
    }

    public static class Saver extends Completable {

        private final LinkedList<City> cities = new LinkedList<>();

        private Saver(LinkedList<City> cities) {
            this.cities.addAll(cities);
        }

        @Override
        protected void subscribeActual(CompletableObserver observer) {
            Domain.getApplication()
                    .defaultIfEmpty(null)
                    .map(context -> context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE))
                    .map(SharedPreferences::edit)
                    .apply(editor -> editor.putString(KEY_FAVORITE_CITIES, new Gson().toJson(cities)))
                    .apply(Editor::apply)
                    .invoke(observer::onComplete);
        }

    }

    public static class Loader extends Single<LinkedList<City>> {

        private static final String EMPTY_VALUE = "";

        private Loader() {
        }

        @Override
        protected void subscribeActual(SingleObserver<? super LinkedList<City>> observer) {
            Domain.getApplication()
                    .defaultIfEmpty(null)
                    .map(context -> context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE))
                    .map(preferences -> preferences.getString(KEY_FAVORITE_CITIES, EMPTY_VALUE))
                    .whenNot(EMPTY_VALUE::equals)
                    .thenMap(SwapCurry.toFunction(new Gson()::fromJson, FavoriteCitiesList.class))
                    .whenNot(List::isEmpty)
                    .then(observer::onSuccess)
                    .defaultIfEmpty(new FavoriteCitiesList())
                    .when(List::isEmpty)
                    .thenTo(new NoSuchElementException("no favorite cities saved"))
                    .apply(observer::onError);
        }
    }

    static class FavoriteCitiesList extends LinkedList<City> {
    }
}
