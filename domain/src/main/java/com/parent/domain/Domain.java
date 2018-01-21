package com.parent.domain;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.actors.ActorLite;
import com.chaining.Chain;
import com.chaining.Optional;

import java.lang.ref.WeakReference;

import io.reactivex.functions.Action;

public class Domain {

    public static final int MSG_REQUEST_FIVE_DAY_FORECAST = 0x201;
    public static final int MSG_REQUEST_FIVE_DAY_FORECAST_SUCCESS = 0x202;
    public static final int MSG_REQUEST_FIVE_DAY_FORECAST_FAILURE = 0x203;

    private static WeakReference<Application> applicationWeakReference;


    public static void integrateWith(@NonNull Application application) {
        Chain.let(application)
                .apply(ActorLite::with)
                .map(WeakReference::new)
                .apply(Domain::setApplicationWeakReference)
                .to(new RepositoryInitializer())
                .apply(Action::run);
    }

    static Optional<Context> getApplication() {
        return Chain.optional(applicationWeakReference)
                .map(WeakReference::get);
    }

    private static void setApplicationWeakReference(WeakReference<Application> weakReference) {
        Domain.applicationWeakReference = weakReference;
    }

}
