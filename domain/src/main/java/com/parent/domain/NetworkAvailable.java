package com.parent.domain;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.concurrent.Callable;

class NetworkAvailable implements Callable<Boolean> {

    @Override
    public Boolean call() {
        return Domain.getApplication()
                .map(this::toConnectivityManager)
                .map(ConnectivityManager::getActiveNetworkInfo)
                .map(NetworkInfo::isConnected)
                .defaultIfEmpty(false)
                .call();
    }

    private ConnectivityManager toConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }


}
