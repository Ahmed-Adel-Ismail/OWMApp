package com.parent.owm.screens.main;

import android.app.Activity;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.annotations.Command;
import com.annotations.CommandsMapFactory;
import com.chaining.Chain;
import com.functional.curry.Curry;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.parent.entities.City;
import com.parent.owm.R;
import com.parent.owm.abstraction.BaseActivity;
import com.parent.owm.annotations.LayoutId;
import com.parent.owm.screens.forecast.ForecastActivity;
import com.vodafone.binding.annotations.SubscribeTo;
import com.vodafone.binding.annotations.SubscriptionsFactory;

import java.util.LinkedList;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.parent.owm.screens.forecast.ForecastActivity.EXTRA_KEY_CITY_ID;

@CommandsMapFactory
@LayoutId(R.layout.activity_main)
@SubscriptionsFactory(MainViewModel.class)
public class MainActivity extends BaseActivity<MainViewModel> {

    static final int MSG_SHOW_FORECAST = 0x301;

    @BindView(R.id.main_activity_search_city_text_view)
    EditText searchTextView;
    @BindView(R.id.main_activity_search_city_progress)
    ProgressBar searchProgress;
    @BindView(R.id.main_activity_add_city_button)
    Button addButton;
    @BindView(R.id.main_activity_favorite_cities_recycler_view)
    RecyclerView favoriteCitiesRecyclerView;


    @SubscribeTo("searchCityInProgress")
    Disposable onSearchCityInProgressUpdated(Subject<Boolean> searchCityInProgress) {
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

    @SubscribeTo("searchForCity")
    Disposable hideKeyboardOnSearchForCity(PublishSubject<Boolean> searchForCity) {
        return searchForCity.share()
                .observeOn(AndroidSchedulers.mainThread())
                .map(trigger -> this)
                .subscribe(this::hideKeyboard);
    }

    private void hideKeyboard(Activity activity) {
        Chain.let(INPUT_METHOD_SERVICE)
                .map(activity::getSystemService)
                .map(object -> (InputMethodManager) object)
                .apply(manager -> manager.hideSoftInputFromWindow(windowToken(activity), 0));
    }

    private IBinder windowToken(Activity activity) {
        return Chain.optional(activity.getCurrentFocus())
                .defaultIfEmpty(new View(activity))
                .map(View::getWindowToken)
                .call();
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
    Disposable onSearchCityTextUpdated(BehaviorSubject<String> searchCityText) {
        searchTextView.setText(searchCityText.getValue());
        return RxTextView.textChanges(searchTextView)
                .map(Object::toString)
                .onErrorResumeNext(Observable.just(""))
                .subscribe(searchCityText::onNext);
    }

    @SubscribeTo("favoriteCities")
    Disposable onFavoriteCitiesUpdated(BehaviorSubject<LinkedList<City>> favoriteCities) {
        return Chain.let(favoriteCitiesRecyclerView)
                .apply(view -> view.setLayoutManager(new LinearLayoutManager(this)))
                .map(FavoriteCitiesAdapter::withView)
                .map(adapter -> adapter.bindTo(favoriteCities))
                .call();
    }

    @Command(MSG_SHOW_FORECAST)
    void showForecast(City city) {
        Chain.let(ForecastActivity.class)
                .map(Curry.toFunction(Intent::new, this))
                .apply(intent -> intent.putExtra(EXTRA_KEY_CITY_ID, city.getId()))
                .apply(this::startActivity);
    }


}