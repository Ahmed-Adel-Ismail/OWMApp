package com.parent.domain;


import android.support.annotation.NonNull;

import com.actors.Actor;
import com.actors.ActorSystem;
import com.actors.Message;
import com.annotations.Command;
import com.annotations.CommandsMapFactory;
import com.functional.curry.Curry;
import com.mapper.CommandsMap;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import static com.parent.domain.Domain.MSG_REQUEST_FIVE_DAY_FORECAST;
import static com.parent.domain.Domain.MSG_REQUEST_FIVE_DAY_FORECAST_FAILURE;
import static com.parent.domain.Domain.MSG_REQUEST_FIVE_DAY_FORECAST_SUCCESS;

@CommandsMapFactory
public class Repository implements Actor {

    private final CommandsMap commandsMap = CommandsMap.of(this);
    private final OpenWeatherMapsApi api;

    public Repository(OpenWeatherMapsApi api) {
        this.api = api;
    }


    @Command(MSG_REQUEST_FIVE_DAY_FORECAST)
    void onRequestFiveDayForecast(Class<?> requester) {
        api.requestFiveDayForecast()
                .map(Curry.toFunction(Message::new, MSG_REQUEST_FIVE_DAY_FORECAST_SUCCESS))
                .onErrorResumeNext(this::failureMessage)
                .blockingSubscribe(message -> ActorSystem.send(message, requester));
    }

    private Observable<Message> failureMessage(Throwable throwable) {
        return Observable.just(new Message(MSG_REQUEST_FIVE_DAY_FORECAST_FAILURE, throwable));
    }

    @Override
    public void onMessageReceived(Message message) {
        commandsMap.execute(message.getId(), message.getContent());
    }

    @NonNull
    @Override
    public Scheduler observeOnScheduler() {
        return Schedulers.io();
    }
}
