package com.parent.owm.screens.main;

import android.annotation.SuppressLint;
import android.support.annotation.RestrictTo;

import com.actors.ActorSystem;
import com.actors.Message;
import com.annotations.Command;
import com.annotations.CommandsMapFactory;
import com.binding.annotations.SubscriptionName;
import com.chaining.Chain;
import com.chaining.Optional;
import com.functional.curry.Curry;
import com.functional.curry.SwapCurry;
import com.parent.domain.Domain;
import com.parent.domain.FavoriteCities;
import com.parent.domain.Repository;
import com.parent.entities.City;
import com.parent.owm.abstraction.BaseViewModel;

import org.javatuples.Pair;

import java.util.LinkedList;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

import static android.support.annotation.RestrictTo.Scope.TESTS;
import static com.parent.domain.Domain.MSG_SEARCH_FOR_CITY_RESULT;

@CommandsMapFactory
public class MainViewModel extends BaseViewModel {

    static final int MSG_REMOVE_CITY = 0x101;

    @SubscriptionName("searchCityText")
    final BehaviorSubject<String> searchCityText = BehaviorSubject.createDefault("");
    @SubscriptionName("searchCityInProgress")
    final BehaviorSubject<Boolean> searchCityInProgress = BehaviorSubject.createDefault(false);
    @SubscriptionName("searchForCity")
    final PublishSubject<Boolean> searchForCity = PublishSubject.create();
    @SubscriptionName("searchForCityFailure")
    final PublishSubject<Boolean> searchForCityFailure = PublishSubject.create();
    @SubscriptionName("favoriteCities")
    final BehaviorSubject<LinkedList<City>> favoriteCities = BehaviorSubject.createDefault(new LinkedList<>());

    @SuppressLint("RestrictedApi")
    public MainViewModel() {
        this(Schedulers.computation());
    }

    @RestrictTo(TESTS)
    MainViewModel(Scheduler scheduler) {
        super(scheduler);
        loadFavoriteCitiesFromPreferences();
        sendRepositoryMessageAndShowProgressOnSearchForCity(scheduler);
    }

    private void loadFavoriteCitiesFromPreferences() {
        FavoriteCities.load()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnError(Throwable::printStackTrace)
                .onErrorReturnItem(new LinkedList<>())
                .subscribe(favoriteCities::onNext);
    }

    private void sendRepositoryMessageAndShowProgressOnSearchForCity(Scheduler scheduler) {
        searchForCity.share()
                .map(trigger -> searchCityText.getValue())
                .filter(text -> !text.trim().isEmpty())
                .subscribeOn(scheduler)
                .observeOn(scheduler)
                .map(text -> Pair.with(text, MainViewModel.class))
                .map(Curry.toFunction(Message::new, Domain.MSG_SEARCH_FOR_CITY))
                .doOnNext(SwapCurry.toConsumer(ActorSystem::send, Repository.class))
                .map(message -> true)
                .subscribe(searchCityInProgress::onNext);
    }


    @Command(MSG_REMOVE_CITY)
    void removeCityAndSaveToPreferences(City city) {
        Chain.let(favoriteCities)
                .map(BehaviorSubject::getValue)
                .apply(list -> list.remove(city))
                .apply(favoriteCities::onNext)
                .apply(this::saveToPreferences);
    }

    @Command(MSG_SEARCH_FOR_CITY_RESULT)
    void onSearchForCityResult(Optional<City> foundCity) {
        foundCity.map(this::addNewFavoriteCityAndSaveToPreferences)
                .defaultIfEmpty(false)
                .whenNot(Boolean::booleanValue)
                .then(searchForCityFailure::onNext)
                .invoke(() -> searchCityInProgress.onNext(false));
    }

    private boolean addNewFavoriteCityAndSaveToPreferences(City city) {
        return Chain.let(favoriteCities.getValue())
                .apply(list -> list.addFirst(city))
                .flatMap(Observable::fromIterable)
                .distinct(City::getId)
                .take(5)
                .toList(LinkedList::new)
                .map(Chain::let)
                .blockingGet()
                .apply(favoriteCities::onNext)
                .map(this::saveToPreferences)
                .call();
    }

    private boolean saveToPreferences(LinkedList<City> cities) {
        FavoriteCities.save(cities)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();
        return true;
    }


    public void onCleared() {
        searchCityText.onComplete();
        searchCityInProgress.onComplete();
        searchForCity.onComplete();
        searchForCityFailure.onComplete();
        favoriteCities.onComplete();
        super.onCleared();
    }

}

