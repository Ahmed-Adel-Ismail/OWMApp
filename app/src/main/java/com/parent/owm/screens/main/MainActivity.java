package com.parent.owm.screens.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actors.Actor;
import com.actors.Message;
import com.annotations.CommandsMapFactory;
import com.chaining.Chain;
import com.functional.curry.Curry;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mapper.CommandsMap;
import com.parent.entities.City;
import com.parent.owm.R;
import com.parent.owm.annotations.LayoutId;
import com.vodafone.binding.Binder;
import com.vodafone.binding.annotations.SubscribeTo;
import com.vodafone.binding.annotations.SubscriptionsFactory;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

@LayoutId(R.layout.activity_main)
@SubscriptionsFactory(MainViewModel.class)
@CommandsMapFactory
public class MainActivity extends AppCompatActivity implements Actor {

    private final CommandsMap commandsMap = CommandsMap.of(this);
    @BindView(R.id.main_activity_search_city_text_view)
    EditText searchTextView;
    @BindView(R.id.main_activity_search_city_progress)
    ProgressBar searchProgress;
    @BindView(R.id.main_activity_add_city_button)
    Button addButton;
    private Binder<MainViewModel> binder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = Chain.let(MainViewModel.class)
                .map(ViewModelProviders.of(this)::get)
                .map(Binder.bind(this)::<MainViewModel>to)
                .call();
    }


    @SubscribeTo("searchCityInProgress")
    Disposable onSearchCityInProgressChanged(Subject<Boolean> searchCityInProgress) {
        return searchCityInProgress
                .share()
                .map(searching -> searching ? VISIBLE : INVISIBLE)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchProgress::setVisibility);
    }


    @SubscribeTo("searchForCity")
    void onAddButtonClicked(PublishSubject<Boolean> searchForCity) {
        addButton.setOnClickListener(view -> searchForCity.onNext(true));
    }

    @SubscribeTo("searchForCityFailure")
    Disposable onSearchForCityFailure(PublishSubject<Boolean> searchForCityFailure) {
        return searchForCityFailure.share()
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .map(trigger -> R.string.activity_main_search_failed_toast)
                .map(this::getString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(text -> Toast.makeText(this, text, Toast.LENGTH_LONG).show());
    }

    @SubscribeTo("searchCityText")
    Disposable onSearchCityTextChanged(BehaviorSubject<String> searchCityText) {
        searchTextView.setText(searchCityText.getValue());
        return RxTextView.textChanges(searchTextView)
                .map(Object::toString)
                .onErrorResumeNext(Observable.just(""))
                .subscribe(searchCityText::onNext);
    }

    @SubscribeTo("favoriteCities")
    void onSavedCitiesChanged(BehaviorSubject<LinkedList<City>> favoriteCities) {
        favoriteCities.share()
                .map(List::toString)
                .subscribe(Curry.toConsumer(Log::e, "MainActivity"));

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
                .apply(Binder::unbind)
                .map(Binder::getSubscriptionsFactory)
                .when(binder -> isFinishing())
                .then(MainViewModel::clear);
    }
}