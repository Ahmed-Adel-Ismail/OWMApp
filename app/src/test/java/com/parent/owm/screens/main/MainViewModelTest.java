package com.parent.owm.screens.main;

import android.support.annotation.NonNull;

import com.chaining.Chain;
import com.parent.entities.City;
import com.parent.entities.Coordinates;

import org.junit.Test;

import io.reactivex.schedulers.TestScheduler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MainViewModelTest {

    private final TestScheduler scheduler = new TestScheduler();
    private final MainViewModel viewModel = new MainViewModel(scheduler);

    @Test
    public void changeSearchTextWithValidValueThenShowProgress() {
        boolean[] result = {false};
        viewModel.searchCityInProgress
                .subscribeOn(scheduler)
                .observeOn(scheduler)
                .subscribe(status -> result[0] = status);

        scheduler.triggerActions();

        if (result[0]) {
            throw new AssertionError("searchCityInProgress started as true");
        }

        viewModel.searchCityText.onNext("test");
        viewModel.searchForCity.set(true);
        scheduler.triggerActions();

        assertTrue(result[0]);

    }

    @Test
    public void changeSearchTextWithInvalidValueThenDoNotShowProgress() {
        boolean[] result = {false};
        viewModel.searchCityInProgress
                .subscribeOn(scheduler)
                .observeOn(scheduler)
                .subscribe(status -> result[0] = status);

        scheduler.triggerActions();

        if (result[0]) {
            throw new AssertionError("searchCityInProgress started as true");
        }

        viewModel.searchCityText.onNext("");
        viewModel.searchForCity.set(true);
        scheduler.triggerActions();

        assertFalse(result[0]);

    }

    @Test
    public void onSearchForCityResultWithValidResultThenHideProgress() {
        boolean[] result = {false};
        viewModel.searchCityInProgress
                .subscribeOn(scheduler)
                .observeOn(scheduler)
                .subscribe(status -> result[0] = status);

        scheduler.triggerActions();

        viewModel.searchForCity.set(true);
        scheduler.triggerActions();

        viewModel.onSearchForCityResult(Chain.optional(mockCity()));
        scheduler.triggerActions();

        assertFalse(result[0]);

    }

    @Test
    public void onSearchForCityResultWithInvalidResultThenHideProgress() {
        boolean[] result = {false};
        viewModel.searchCityInProgress
                .subscribeOn(scheduler)
                .observeOn(scheduler)
                .subscribe(status -> result[0] = status);

        scheduler.triggerActions();

        viewModel.searchForCity.set(true);
        scheduler.triggerActions();

        viewModel.onSearchForCityResult(Chain.optional(null));
        scheduler.triggerActions();

        assertFalse(result[0]);

    }

    @NonNull
    private City mockCity() {
        return new City(0L, "", "", new Coordinates(0D, 0D));
    }

}