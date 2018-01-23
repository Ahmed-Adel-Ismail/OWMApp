package com.parent.domain;


import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.actors.Actor;
import com.actors.Message;
import com.annotations.Command;
import com.annotations.CommandsMapFactory;
import com.mapper.CommandsMap;
import com.parent.entities.City;
import com.parent.entities.ForecastsResponse;

import org.javatuples.Pair;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.support.annotation.RestrictTo.Scope.TESTS;
import static com.parent.domain.Domain.MSG_REQUEST_FIVE_DAY_FORECAST;
import static com.parent.domain.Domain.MSG_SEARCH_FOR_CITY;

@CommandsMapFactory
public class Repository implements Actor {

    private final Function<Long, Observable<ForecastsResponse>> fiveDayForecastRequester;
    private final Function<String, List<City>> citySearcher;
    private final CommandsMap commandsMap;
    private final Scheduler scheduler;

    @SuppressLint("RestrictedApi")
    Repository(Function<Long, Observable<ForecastsResponse>> fiveDayForecastRequester,
               Function<String, List<City>> citySearcher) {
        this(fiveDayForecastRequester, citySearcher, Schedulers.io());
    }

    @RestrictTo(TESTS)
    Repository(Function<Long, Observable<ForecastsResponse>> fiveDayForecastRequester,
                      Function<String, List<City>> citySearcher,
                      Scheduler scheduler) {

        this.scheduler = scheduler;
        this.citySearcher = citySearcher;
        this.fiveDayForecastRequester = fiveDayForecastRequester;
        this.commandsMap = CommandsMap.of(this);
    }


    @Command(MSG_REQUEST_FIVE_DAY_FORECAST)
    void onRequestFiveDayForecast(Pair<Long, Class<?>> forecastData) {
        new OnRequestFiveDayForecast(fiveDayForecastRequester, scheduler).accept(forecastData);
    }


    @Command(MSG_SEARCH_FOR_CITY)
    void onSearchForCity(Pair<String, Class<?>> searchData) {
        new OnSearchForCity(citySearcher, scheduler).accept(searchData);
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
