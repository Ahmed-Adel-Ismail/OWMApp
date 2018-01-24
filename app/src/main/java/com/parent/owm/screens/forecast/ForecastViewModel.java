package com.parent.owm.screens.forecast;

import android.annotation.SuppressLint;
import android.support.annotation.RestrictTo;

import com.annotations.CommandsMapFactory;
import com.parent.owm.abstraction.BaseViewModel;
import com.vodafone.binding.annotations.SubscriptionName;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

import static android.support.annotation.RestrictTo.Scope.TESTS;

@CommandsMapFactory
public class ForecastViewModel extends BaseViewModel {

    @SubscriptionName("cityId")
    final BehaviorSubject<Long> cityId = BehaviorSubject.create();

    @SuppressLint("RestrictedApi")
    public ForecastViewModel() {
        this(Schedulers.computation());
    }

    @RestrictTo(TESTS)
    ForecastViewModel(Scheduler scheduler) {
        super(scheduler);
    }

    @Override
    public void clear() {
        cityId.onComplete();
        super.clear();
    }
}
