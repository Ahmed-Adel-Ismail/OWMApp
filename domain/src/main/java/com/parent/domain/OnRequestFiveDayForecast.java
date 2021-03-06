package com.parent.domain;

import com.actors.ActorSystem;
import com.actors.Message;
import com.functional.curry.Curry;
import com.functional.curry.SwapCurry;
import com.parent.entities.ForecastsResponse;

import org.javatuples.Pair;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;

import static com.parent.domain.Domain.MSG_REQUEST_FIVE_DAY_FORECAST_FAILURE;
import static com.parent.domain.Domain.MSG_REQUEST_FIVE_DAY_FORECAST_SUCCESS;

class OnRequestFiveDayForecast implements Consumer<Pair<Long, Class<?>>> {

    private final FiveDayForecastRequester fiveDayForecastRequester;
    private final Scheduler scheduler;


    OnRequestFiveDayForecast(FiveDayForecastRequester fiveDayForecastRequester,
                             Scheduler scheduler) {
        this.fiveDayForecastRequester = fiveDayForecastRequester;
        this.scheduler = scheduler;
    }

    @Override
    public void accept(Pair<Long, Class<?>> forecastData) {
        fiveDayForecastRequester.apply(forecastData.getValue0())
                .map(ForecastsResponse::getForecasts)
                .flatMap(Observable::fromIterable)
                .map(new ForecastSummeryParser())
                .toList()
                .subscribeOn(scheduler)
                .observeOn(scheduler)
                .map(Curry.toFunction(Message::new, MSG_REQUEST_FIVE_DAY_FORECAST_SUCCESS))
                .doOnError(Throwable::printStackTrace)
                .onErrorResumeNext(this::failureMessage)
                .subscribe(SwapCurry.toConsumer(ActorSystem::send, forecastData.getValue1()));
    }

    private Single<Message> failureMessage(Throwable throwable) {
        return Single.just(new Message(MSG_REQUEST_FIVE_DAY_FORECAST_FAILURE, throwable));
    }
}
