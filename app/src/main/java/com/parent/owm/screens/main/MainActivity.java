package com.parent.owm.screens.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.actors.Actor;
import com.actors.Message;
import com.annotations.CommandsMapFactory;
import com.chaining.Chain;
import com.functional.curry.Curry;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;
import com.mapper.CommandsMap;
import com.parent.entities.City;
import com.parent.owm.R;
import com.parent.owm.annotations.LayoutId;
import com.vodafone.binding.Binder;
import com.vodafone.binding.annotations.SubscribeTo;
import com.vodafone.binding.annotations.SubscriptionsFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.properties.BooleanProperty;
import io.reactivex.properties.Property;
import io.reactivex.schedulers.Schedulers;

import static android.R.layout.select_dialog_item;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

@LayoutId(R.layout.activity_main)
@SubscriptionsFactory(MainViewModel.class)
@CommandsMapFactory
public class MainActivity extends AppCompatActivity implements Actor {

    private final CommandsMap commandsMap = CommandsMap.of(this);

    @BindView(R.id.main_activity_search_city_text_view)
    AutoCompleteTextView searchTextView;
    @BindView(R.id.main_activity_search_city_progress)
    AutoCompleteTextView searchProgress;
    private Binder<MainViewModel> binder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = Chain.let(MainViewModel.class)
                .map(ViewModelProviders.of(this)::get)
                .map(Binder.bind(this)::<MainViewModel>to)
                .call();
    }


    @SubscribeTo("citiesFoundFromSearch")
    Disposable onCitiesFoundFromSearchChanged(Property<List<City>> cities) {
        return cities.asObservable()
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .flatMap(Observable::fromIterable)
                .map(City::getName)
                .toList()
                .map(names -> names.toArray(new String[0]))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateSearchAutoCompleteAdapter);
    }

    private void updateSearchAutoCompleteAdapter(String[] namesArray) {
        Chain.let(namesArray)
                .map(Curry.toFunction(ArrayAdapter<String>::new, this, select_dialog_item))
                .apply(searchTextView::setAdapter)
                .to(1)
                .apply(searchTextView::setThreshold)
                .invoke(searchTextView::invalidate);
    }

    @SubscribeTo("searchCityInProgress")
    Disposable onSearchCityInProgressChanged(BooleanProperty searchCityInProgress) {
        return searchCityInProgress
                .asObservable()
                .map(booleanFlag -> booleanFlag ? VISIBLE : INVISIBLE)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchProgress::setVisibility);
    }

    @SubscribeTo("searchCityText")
    Disposable onSearchCityTextChanged(Property<String> searchCityText) {
        return RxTextView.afterTextChangeEvents(searchTextView)
                .throttleLast(3, TimeUnit.SECONDS)
                .map(TextViewAfterTextChangeEvent::editable)
                .map(Object::toString)
                .onErrorResumeNext(Observable.just(""))
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe(searchCityText);
    }


    @Override
    public void onMessageReceived(Message message) {
        commandsMap.execute(message.getId(), message.getContent());
    }

    @NonNull
    @Override
    public Scheduler observeOnScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Chain.optional(binder)
                .apply(Binder::unbind);
    }
}