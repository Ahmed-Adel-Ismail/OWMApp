package com.parent.owm.screens.forecast;

import com.actors.ActorScheduler;
import com.actors.ActorSystem;
import com.actors.Cancellable;
import com.actors.Message;
import com.annotations.Command;
import com.annotations.CommandsMapFactory;
import com.chaining.Chain;
import com.chaining.Guard;
import com.functional.curry.Curry;
import com.functional.curry.SwapCurry;
import com.parent.domain.NetworkAvailable;
import com.parent.domain.Repository;
import com.parent.entities.ForecastSummery;
import com.parent.owm.abstraction.BaseViewModel;
import com.vodafone.binding.annotations.SubscriptionName;

import org.javatuples.Pair;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

import static com.parent.domain.Domain.MSG_REQUEST_FIVE_DAY_FORECAST;
import static com.parent.domain.Domain.MSG_REQUEST_FIVE_DAY_FORECAST_FAILURE;
import static com.parent.domain.Domain.MSG_REQUEST_FIVE_DAY_FORECAST_SUCCESS;

@CommandsMapFactory
public class ForecastViewModel extends BaseViewModel {

    private static final long RETRY_AFTER_MILLIS = 8000;
    private static final String NETWORK_ERROR = "No network available";

    @SubscriptionName("cityId")
    final BehaviorSubject<Long> cityId = BehaviorSubject.create();
    @SubscriptionName("forecastsResponse")
    final BehaviorSubject<List<ForecastSummery>> forecastsResponse = BehaviorSubject.create();
    @SubscriptionName("requestInProgress")
    final BehaviorSubject<Boolean> requestInProgress = BehaviorSubject.createDefault(false);
    @SubscriptionName("retryAfterErrorMessage")
    final PublishSubject<String> retryAfterErrorMessage = PublishSubject.create();
    private Cancellable retryCancellable;

    public ForecastViewModel() {
        super(Schedulers.computation());
        requestCityForecastIfNotRequesting();
    }


    private void requestCityForecastIfNotRequesting() {
        cityId.share()
                .filter(id -> forecastsResponse.getValue() == null)
                .filter(id -> !requestInProgress.getValue())
                .map(id -> requestForecastMessage())
                .doOnNext(SwapCurry.toConsumer(ActorSystem::send, Repository.class))
                .map(message -> true)
                .subscribe(requestInProgress::onNext);
    }

    private Message requestForecastMessage() {
        return Chain.let(Pair.with(cityId.getValue(), ForecastViewModel.class))
                .map(Curry.toFunction(Message::new, MSG_REQUEST_FIVE_DAY_FORECAST))
                .call();
    }

    @Command(MSG_REQUEST_FIVE_DAY_FORECAST_SUCCESS)
    void onRequestForecastSuccess(List<ForecastSummery> response) {
        forecastsResponse.onNext(response);
        requestInProgress.onNext(false);
    }

    @Command(MSG_REQUEST_FIVE_DAY_FORECAST_FAILURE)
    void onRequestForecastFailure(Throwable throwable) {
        retryCancellable = Guard.call(new NetworkAvailable())
                .onErrorReturnItem(true)
                .whenNot(Boolean::booleanValue)
                .invoke(() -> retryAfterErrorMessage.onNext(NETWORK_ERROR))
                .when(Boolean::booleanValue)
                .invoke(() -> retryAfterErrorMessage.onNext(throwable.getMessage()))
                .to(RETRY_AFTER_MILLIS)
                .map(ActorScheduler::after)
                .map(scheduler -> scheduler.send(requestForecastMessage(), Repository.class))
                .call();

    }

    @Override
    public void clear() {
        cityId.onComplete();
        requestInProgress.onComplete();
        forecastsResponse.onComplete();
        Chain.optional(retryCancellable)
                .map(Cancellable::cancel)
                .whenNot(Disposable::isDisposed)
                .then(Disposable::dispose);

        super.clear();
    }
}
