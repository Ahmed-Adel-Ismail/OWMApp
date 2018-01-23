package com.parent.domain;

import com.actors.ActorSystem;
import com.actors.Message;
import com.functional.curry.Curry;
import com.functional.curry.SwapCurry;
import com.parent.entities.City;

import org.javatuples.Pair;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.parent.domain.Domain.MSG_SEARCH_FOR_CITY_RESULT;

class OnSearchForCity implements Consumer<Pair<String, Class<?>>> {

    private final Function<String, List<City>> citySearcher;
    private final Scheduler scheduler;

    OnSearchForCity(Function<String, List<City>> citySearcher, Scheduler scheduler) {
        this.citySearcher = citySearcher;
        this.scheduler = scheduler;
    }

    @Override
    public void accept(Pair<String, Class<?>> searchData) {
        Observable.fromCallable(Curry.toCallable(citySearcher, searchData.getValue0()))
                .subscribeOn(scheduler)
                .observeOn(scheduler)
                .doOnError(Throwable::printStackTrace)
                .onErrorResumeNext(Observable.just(new LinkedList<>()))
                .map(Curry.toFunction(Message::new, MSG_SEARCH_FOR_CITY_RESULT))
                .subscribe(SwapCurry.toConsumer(ActorSystem::send, searchData.getValue1()));
    }
}
