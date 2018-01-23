package com.parent.owm.system.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.parent.owm.annotations.scanners.LayoutIdScanner;

import butterknife.ButterKnife;

/**
 * Created by Ahmed Adel Ismail on 1/22/2018.
 */

public class ActivitiesLifeCycleCallback implements Application.ActivityLifecycleCallbacks {


    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        activity.setContentView(new LayoutIdScanner().apply(activity));
        ButterKnife.bind(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
