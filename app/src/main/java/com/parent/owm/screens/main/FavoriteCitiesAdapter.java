package com.parent.owm.screens.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chaining.Chain;
import com.parent.entities.City;

import java.util.LinkedList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

import static com.parent.owm.R.layout.view_favorite_city;

class FavoriteCitiesAdapter extends RecyclerView.Adapter<FavoriteCitiesViewHolder> {

    private final BehaviorSubject<LinkedList<City>> favoriteCities;
    private Disposable disposable;

    private FavoriteCitiesAdapter(BehaviorSubject<LinkedList<City>> favoriteCities) {
        this.favoriteCities = favoriteCities;
        this.disposable = notifyDataSetChangedOnFavoritesChange(favoriteCities);
    }

    @NonNull
    private Disposable notifyDataSetChangedOnFavoritesChange(
            BehaviorSubject<LinkedList<City>> favoriteCities) {

        return favoriteCities.share()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> notifyDataSetChanged());
    }

    static Factory withView(RecyclerView recyclerView) {
        return new Factory(recyclerView);
    }

    @Override
    public FavoriteCitiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return Chain.let(parent)
                .map(ViewGroup::getContext)
                .map(LayoutInflater::from)
                .map(inflater -> inflater.inflate(view_favorite_city, parent, false))
                .map(FavoriteCitiesViewHolder::new)
                .call();
    }

    @Override
    public void onBindViewHolder(FavoriteCitiesViewHolder holder, int position) {
        Chain.let(favoriteCities)
                .map(BehaviorSubject::getValue)
                .map(list -> list.get(position))
                .apply(holder::invalidate);
    }

    @Override
    public int getItemCount() {
        return favoriteCities.getValue().size();
    }


    static class Factory {

        private final RecyclerView recyclerView;

        private Factory(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        Disposable bindTo(BehaviorSubject<LinkedList<City>> favoriteCities) {
            FavoriteCitiesAdapter adapter = new FavoriteCitiesAdapter(favoriteCities);
            recyclerView.setAdapter(adapter);
            return adapter.disposable;
        }

    }
}
