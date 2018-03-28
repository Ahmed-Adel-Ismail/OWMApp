package com.parent.owm.system.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.actors.ActorScheduler;
import com.actors.ActorSystem;
import com.mapper.CommandsMap;
import com.parent.owm.annotations.scanners.IntentExtraScanner;
import com.parent.owm.annotations.scanners.LayoutIdScanner;

import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ActivitiesLifeCycleCallback implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        activity.setContentView(new LayoutIdScanner().apply(activity));
        ButterKnife.bind(activity);
        new IntentExtraScanner().accept(activity);
        registerActivityAsActor(activity);
    }

    private void registerActivityAsActor(Activity activity) {
        CommandsMap commandsMap = CommandsMap.of(activity);
        ActorSystem.register(activity, AndroidSchedulers.mainThread(),
                message -> commandsMap.execute(message.getId(), message.getContent()));
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
        ActorSystem.unregister(activity);
        if (activity.isFinishing()) {
            ActorScheduler.cancel(activity.getClass());
        }
    }
}
