package com.parent.owm.system;

import android.content.Context;
import android.content.SharedPreferences;

import com.chaining.Chain;
import com.parent.domain.FavoriteCities;

import io.reactivex.Completable;
import io.reactivex.functions.Consumer;

import static android.content.Context.MODE_PRIVATE;


class FirstRuninitializer implements Consumer<Context> {

    private static final String PREFERENCES_NAME = "SystemPreferences";
    private static final String KEY_INITIALIZED = "com.parent.owm.system.KEY_INITIALIZED";


    @Override
    public void accept(Context context) {
        Chain.let(context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE))
                .whenNot(preferences -> preferences.getBoolean(KEY_INITIALIZED, false))
                .thenMap(SharedPreferences::edit)
                .apply(editor -> editor.putBoolean(KEY_INITIALIZED, true))
                .map(editor -> FavoriteCities.save(FavoriteCities.defaultList()))
                .apply(Completable::blockingAwait);
    }
}
