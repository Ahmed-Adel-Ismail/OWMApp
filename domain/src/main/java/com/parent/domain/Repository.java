package com.parent.domain;


import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.actors.Actor;
import com.actors.Message;
import com.annotations.Command;
import com.annotations.CommandsMapFactory;
import com.chaining.Optional;
import com.mapper.CommandsMap;
import com.parent.entities.City;

import org.javatuples.Pair;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.support.annotation.RestrictTo.Scope.TESTS;
import static com.parent.domain.Domain.MSG_REQUEST_FIVE_DAY_FORECAST;
import static com.parent.domain.Domain.MSG_SEARCH_FOR_CITY;

@CommandsMapFactory
public class Repository implements Actor {

    private final OnRequestFiveDayForecast onRequestFiveDayForecast;
    private final OnSearchForCity onSearchForCity;

    private final CommandsMap commandsMap;
    private final Scheduler scheduler;

    @SuppressLint("RestrictedApi")
    Repository(FiveDayForecastRequester fiveDayForecastRequester,
               Function<String, Optional<City>> citySearcher) {
        this(fiveDayForecastRequester, citySearcher, Schedulers.io());
    }

    @RestrictTo(TESTS)
    Repository(FiveDayForecastRequester fiveDayForecastRequester,
               Function<String, Optional<City>> citySearcher,
               Scheduler scheduler) {

        this.commandsMap = CommandsMap.of(this);
        this.scheduler = scheduler;
        this.onRequestFiveDayForecast = new OnRequestFiveDayForecast(fiveDayForecastRequester, scheduler);
        this.onSearchForCity = new OnSearchForCity(citySearcher, scheduler);
    }


    @Command(MSG_REQUEST_FIVE_DAY_FORECAST)
    void onRequestFiveDayForecast(Pair<Long, Class<?>> forecastData) {
        onRequestFiveDayForecast.accept(forecastData);
    }


    @Command(MSG_SEARCH_FOR_CITY)
    void onSearchForCity(Pair<String, Class<?>> searchData) {
        onSearchForCity.accept(searchData);
    }

    @Override
    public void onMessageReceived(Message message) {
        commandsMap.execute(message.getId(), message.getContent());
    }

    @NonNull
    @Override
    public Scheduler observeOnScheduler() {
        return scheduler;
    }
}
