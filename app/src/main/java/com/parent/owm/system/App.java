package com.parent.owm.system;

import android.app.Application;

import com.parent.domain.Domain;
import com.parent.owm.system.lifecycle.ActivitiesLifeCycleCallback;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivitiesLifeCycleCallback());
        Domain.integrateWith(this);
    }
}
