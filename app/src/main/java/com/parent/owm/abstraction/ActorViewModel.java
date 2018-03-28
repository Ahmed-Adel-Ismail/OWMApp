package com.parent.owm.abstraction;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.actors.Actor;
import com.actors.ActorScheduler;
import com.actors.ActorSystem;
import com.actors.Message;
import com.mapper.CommandsMap;

import io.reactivex.Scheduler;


public abstract class ActorViewModel extends ViewModel implements Actor {

    private final Scheduler scheduler;
    private final CommandsMap commandsMap = CommandsMap.of(this);

    protected ActorViewModel(Scheduler scheduler) {
        this.scheduler = scheduler;
        ActorSystem.register(this);
    }

    @Override
    public final void onMessageReceived(Message message) {
        commandsMap.execute(message.getId(), message.getContent());
    }

    @NonNull
    @Override
    public final Scheduler observeOnScheduler() {
        return scheduler;
    }

    @Override
    @CallSuper
    public void onCleared() {
        commandsMap.clear();
        ActorSystem.unregister(this);
        ActorScheduler.cancel(getClass());
    }
}
