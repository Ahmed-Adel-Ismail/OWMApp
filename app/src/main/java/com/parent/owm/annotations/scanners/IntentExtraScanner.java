package com.parent.owm.annotations.scanners;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.chaining.Chain;
import com.functional.curry.Curry;
import com.parent.owm.annotations.IntentExtra;

import org.javatuples.Pair;

import java.io.Serializable;
import java.lang.reflect.Field;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class IntentExtraScanner implements Consumer<Activity> {

    @Override
    public void accept(Activity activity) {
        Chain.optional(activity)
                .map(Activity::getClass)
                .map(Class::getDeclaredFields)
                .defaultIfEmpty(new Field[0])
                .flatMap(Observable::fromArray)
                .filter(field -> field.isAnnotationPresent(IntentExtra.class))
                .doOnNext(field -> field.setAccessible(true))
                .map(this::toPairOfFieldAndExtraKey)
                .map(Curry.toFunction(this::toPairOfFieldAndExtraValue, intentExtrasBundle(activity)))
                .blockingSubscribe(Curry.toConsumer(this::updateFieldWithExtraValue, activity));


    }

    private Bundle intentExtrasBundle(Activity activity) {
        return Chain.optional(activity)
                .map(Activity::getIntent)
                .map(Intent::getExtras)
                .defaultIfEmpty(new Bundle())
                .call();
    }

    private void updateFieldWithExtraValue(Activity activity, Pair<Field, Serializable> pair)
            throws IllegalAccessException {
        pair.getValue0().set(activity, pair.getValue1());
    }

    private Pair<Field, Serializable> toPairOfFieldAndExtraValue(Bundle intentExtras, Pair<Field, String> pair) {
        return pair.setAt1(intentExtras.getSerializable(pair.getValue1()));
    }

    @NonNull
    private Pair<Field, String> toPairOfFieldAndExtraKey(Field field) {
        return Pair.with(field, field.getAnnotation(IntentExtra.class).value());
    }
}
