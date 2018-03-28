package com.parent.owm.system;

import android.app.Application;

import com.android.binding.Binding;
import com.chaining.Chain;
import com.parent.domain.Domain;
import com.parent.owm.system.lifecycle.ActivitiesLifeCycleCallback;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Chain.let(this)
                .apply(Binding::integrate)
                .apply(Domain::integrateWith)
                .apply(new FirstRunInitializer()::accept)
                .apply(app -> app.registerActivityLifecycleCallbacks(new ActivitiesLifeCycleCallback()));
    }
}
