package com.parent.owm.screens.forecast;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.annotations.CommandsMapFactory;
import com.chaining.Chain;
import com.parent.entities.ForecastSummery;
import com.parent.owm.R;
import com.parent.owm.abstraction.BaseActivity;
import com.parent.owm.annotations.IntentExtra;
import com.parent.owm.annotations.LayoutId;
import com.vodafone.binding.annotations.SubscribeTo;
import com.vodafone.binding.annotations.SubscriptionsFactory;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


@LayoutId(R.layout.activity_forecast)
@SubscriptionsFactory(ForecastViewModel.class)
@CommandsMapFactory
public class ForecastActivity extends BaseActivity<ForecastViewModel> {

    public static final String EXTRA_KEY_CITY_ID = "com.parent.owm.screens.forecast.EXTRA_KEY_CITY_ID";

    @BindView(R.id.activity_forecast_recycler_view)
    RecyclerView forecastsRecyclerView;
    @BindView(R.id.activity_forecast_progress_bar)
    ProgressBar requestProgress;

    @IntentExtra(EXTRA_KEY_CITY_ID)
    private Long cityId;

    @SubscribeTo("cityId")
    void updateCityId(BehaviorSubject<Long> cityIdSubject) {
        Chain.optional(cityId).apply(cityIdSubject::onNext);
    }

    @SubscribeTo("forecastsResponse")
    Disposable onForecastResponseUpdated(BehaviorSubject<List<ForecastSummery>> forecastsResponse) {
        return Chain.let(forecastsRecyclerView)
                .apply(view -> view.setLayoutManager(new LinearLayoutManager(this)))
                .map(ForecastAdapter::withView)
                .map(adapter -> adapter.bindTo(forecastsResponse))
                .call();
    }


    @SubscribeTo("retryAfterErrorMessage")
    Disposable onRetryAfterErrorMessageUpdated(PublishSubject<String> retryAfterErrorMessage) {
        return retryAfterErrorMessage.share()
                .observeOn(Schedulers.computation())
                .map(message -> getString(R.string.activity_forecast_retry_error_message) + message)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

    @SubscribeTo("requestInProgress")
    Disposable onRequestInProgressUpdated(BehaviorSubject<Boolean> requestInProgress) {
        return requestInProgress
                .share()
                .map(searching -> searching ? VISIBLE : INVISIBLE)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(requestProgress::setVisibility);
    }

}
