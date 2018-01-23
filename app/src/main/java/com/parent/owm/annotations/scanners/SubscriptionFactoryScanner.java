package com.parent.owm.annotations.scanners;

import android.arch.lifecycle.ViewModel;

import com.chaining.Chain;
import com.vodafone.binding.annotations.SubscriptionsFactory;

import io.reactivex.functions.Function;


public class SubscriptionFactoryScanner<T extends ViewModel>
        implements Function<Object, Class<T>> {

    @SuppressWarnings("unchecked")
    @Override
    public Class<T> apply(Object o) {
        return (Class<T>) Chain.optional(o)
                .map(Object::getClass)
                .map(clazz -> clazz.getAnnotation(SubscriptionsFactory.class))
                .map(SubscriptionsFactory::value)
                .defaultIfEmpty(null)
                .call();
    }
}
