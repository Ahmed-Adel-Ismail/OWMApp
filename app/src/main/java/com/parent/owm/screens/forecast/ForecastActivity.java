package com.parent.owm.screens.forecast;

import com.annotations.CommandsMapFactory;
import com.chaining.Chain;
import com.parent.owm.R;
import com.parent.owm.abstraction.BaseActivity;
import com.parent.owm.annotations.IntentExtra;
import com.parent.owm.annotations.LayoutId;
import com.vodafone.binding.annotations.SubscribeTo;
import com.vodafone.binding.annotations.SubscriptionsFactory;

import io.reactivex.subjects.BehaviorSubject;


@LayoutId(R.layout.activity_forecast)
@SubscriptionsFactory(ForecastViewModel.class)
@CommandsMapFactory
public class ForecastActivity extends BaseActivity<ForecastViewModel> {

    public static final String EXTRA_KEY_CITY_ID = "com.parent.owm.screens.forecast.EXTRA_KEY_CITY_ID";

    @IntentExtra(EXTRA_KEY_CITY_ID)
    private Long cityId;

    @SubscribeTo("cityId")
    void onCityIdChanged(BehaviorSubject<Long> cityId) {
        Chain.optional(this.cityId).apply(cityId::onNext);
    }

}
