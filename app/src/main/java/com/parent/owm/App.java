package com.parent.owm;

import android.app.Application;

import com.parent.domain.Domain;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Domain.integrateWith(this);
    }
}
