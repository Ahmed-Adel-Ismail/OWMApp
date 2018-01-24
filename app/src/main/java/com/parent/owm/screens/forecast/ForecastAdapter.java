package com.parent.owm.screens.forecast;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chaining.Chain;
import com.parent.entities.ForecastSummery;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

import static com.parent.owm.R.layout.view_forecast_item;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastViewHolder> {

    private final BehaviorSubject<List<ForecastSummery>> forecastSummaries;
    private Disposable disposable;

    private ForecastAdapter(BehaviorSubject<List<ForecastSummery>> forecastSummaries) {
        this.forecastSummaries = forecastSummaries;
        this.disposable = notifyDataSetChangedOnFavoritesChange(forecastSummaries);
    }

    @NonNull
    private Disposable notifyDataSetChangedOnFavoritesChange(
            BehaviorSubject<List<ForecastSummery>> forecastSummaries) {

        return forecastSummaries.share()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> notifyDataSetChanged());
    }

    static Factory withView(RecyclerView recyclerView) {
        return new Factory(recyclerView);
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return Chain.let(parent)
                .map(ViewGroup::getContext)
                .map(LayoutInflater::from)
                .map(inflater -> inflater.inflate(view_forecast_item, parent, false))
                .map(ForecastViewHolder::new)
                .call();
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        Chain.let(forecastSummaries)
                .map(BehaviorSubject::getValue)
                .map(list -> list.get(position))
                .apply(holder::invalidate);
    }

    @Override
    public int getItemCount() {
        return Chain.optional(forecastSummaries)
                .map(BehaviorSubject::getValue)
                .map(List::size)
                .defaultIfEmpty(0)
                .call();
    }


    static class Factory {

        private final RecyclerView recyclerView;

        private Factory(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        Disposable bindTo(BehaviorSubject<List<ForecastSummery>> forecastSummaries) {
            ForecastAdapter adapter = new ForecastAdapter(forecastSummaries);
            recyclerView.setAdapter(adapter);
            return adapter.disposable;
        }

    }
}
