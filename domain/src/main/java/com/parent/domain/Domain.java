package com.parent.domain;

import android.app.Application;
import android.support.annotation.NonNull;

import com.actors.ActorLite;
import com.chaining.Chain;
import com.chaining.Optional;
import com.functional.curry.Curry;

import java.lang.ref.WeakReference;

public class Domain {

    public static final int MSG_REQUEST_FIVE_DAY_FORECAST = 0x201;
    public static final int MSG_REQUEST_FIVE_DAY_FORECAST_SUCCESS = 0x202;
    public static final int MSG_REQUEST_FIVE_DAY_FORECAST_FAILURE = 0x203;
    public static final int MSG_SEARCH_FOR_CITY = 0x204;
    public static final int MSG_SEARCH_FOR_CITY_RESULT = 0x205;

    private static WeakReference<Application> applicationWeakReference;


    public static void integrateWith(@NonNull Application application) {
        Chain.let(application)
                .apply(ActorLite::with)
                .map(WeakReference::new)
                .apply(Domain::setApplicationWeakReference)
                .map(WeakReference::get)
                .apply(Curry.toConsumer(new RepositoryInitializer()::accept, new OfflineInterceptor()));
    }

    static Optional<Application> getApplication() {
        return Chain.optional(applicationWeakReference)
                .map(WeakReference::get);
    }

    private static void setApplicationWeakReference(WeakReference<Application> weakReference) {
        Domain.applicationWeakReference = weakReference;
    }

}
