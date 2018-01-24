package com.parent.owm.abstraction;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.actors.Actor;
import com.actors.Message;
import com.chaining.Chain;
import com.mapper.CommandsMap;
import com.vodafone.binding.Binder;
import com.vodafone.binding.annotations.SubscriptionsFactory;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

public abstract class BaseActivity<M extends ViewModel & Clearable>
        extends AppCompatActivity implements Actor {

    private final CommandsMap commandsMap = CommandsMap.of(this);
    private Binder<M> binder;

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = createBinder();
    }


    @SuppressWarnings("unchecked")
    private Binder<M> createBinder() {
        return Chain.let(this)
                .map(Object::getClass)
                .map(clazz -> clazz.getAnnotation(SubscriptionsFactory.class))
                .map(SubscriptionsFactory::value)
                .map(clazz -> (Class<M>) clazz)
                .map(ViewModelProviders.of(this)::get)
                .map(Binder.bind(this)::<M>to)
                .call();
    }

    @Override
    public void onMessageReceived(Message message) {
        commandsMap.execute(message.getId(), message.getContent());
    }

    @NonNull
    @Override
    public Scheduler observeOnScheduler() {
        return AndroidSchedulers.mainThread();
    }


    @Override
    @CallSuper
    @SuppressWarnings("Convert2MethodRef")
    protected void onDestroy() {
        super.onDestroy();
        Chain.optional(binder)
                .apply(Binder::unbind)
                .map(Binder::getSubscriptionsFactory)
                .when(viewModel -> isFinishing())
                .then(viewModel -> viewModel.clear());
    }
}
