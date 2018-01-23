package com.parent.owm.screens.main;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.actors.Actor;
import com.actors.ActorSystem;
import com.actors.Message;
import com.annotations.Command;
import com.annotations.CommandsMapFactory;
import com.functional.curry.Curry;
import com.functional.curry.SwapCurry;
import com.mapper.CommandsMap;
import com.parent.domain.Domain;
import com.parent.domain.Repository;
import com.parent.entities.City;
import com.vodafone.binding.annotations.SubscriptionName;

import org.javatuples.Pair;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.properties.BooleanProperty;
import io.reactivex.properties.Property;
import io.reactivex.schedulers.Schedulers;

import static android.support.annotation.RestrictTo.Scope.TESTS;

@CommandsMapFactory
public class MainViewModel extends ViewModel implements Actor {

    @SubscriptionName("storedCities")
    final MutableLiveData<List<City>> storedCities = new MutableLiveData<>();
    @SubscriptionName("searchCityText")
    final Property<String> searchCityText = new Property<>();
    @SubscriptionName("searchCityInProgress")
    final BooleanProperty searchCityInProgress = new BooleanProperty(false);
    @SubscriptionName("citiesFoundFromSearch")
    final Property<List<City>> citiesFoundFromSearch = new Property<>(new LinkedList<>());

    private final CommandsMap commandsMap = CommandsMap.of(this);
    private final Scheduler scheduler;

    @SuppressLint("RestrictedApi")
    public MainViewModel() {
        this(Schedulers.computation());
    }

    @RestrictTo(TESTS)
    MainViewModel(Scheduler scheduler) {
        this.scheduler = scheduler;
        sendRepositoryMessageOnSearchCityTextChange();
        showProgressOnSearchCityTextChange();
        hideProgressOnCitiesFoundFromSearchChange();
    }

    private void sendRepositoryMessageOnSearchCityTextChange() {
        searchCityText.asObservable()
                .subscribeOn(scheduler)
                .observeOn(scheduler)
                .filter(text -> !text.isEmpty())
                .map(text -> Pair.with(text, MainViewModel.class))
                .map(Curry.toFunction(Message::new, Domain.MSG_SEARCH_FOR_CITY))
                .subscribe(SwapCurry.toConsumer(ActorSystem::send, Repository.class));
    }

    private void showProgressOnSearchCityTextChange() {
        searchCityText.asObservable()
                .subscribeOn(scheduler)
                .observeOn(scheduler)
                .map(text -> true)
                .subscribe(searchCityInProgress);
    }

    private void hideProgressOnCitiesFoundFromSearchChange() {
        citiesFoundFromSearch.asObservable()
                .subscribeOn(scheduler)
                .observeOn(scheduler)
                .map(result -> false)
                .subscribe(searchCityInProgress);
    }

    @Command(Domain.MSG_SEARCH_FOR_CITY_RESULT)
    void onSearchForCityResult(List<City> citiesFoundFromSearch) {
        this.citiesFoundFromSearch.set(citiesFoundFromSearch);
    }

    @Override
    public void onMessageReceived(Message message) {
        commandsMap.execute(message.getId(), message.getContent());
    }

    @NonNull
    @Override
    public Scheduler observeOnScheduler() {
        return Schedulers.computation();
    }
}
