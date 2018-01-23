package com.parent.owm.screens.main;

import org.junit.Test;

import java.util.LinkedList;

import io.reactivex.schedulers.TestScheduler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MainViewModelTest {

    private final TestScheduler scheduler = new TestScheduler();
    private final MainViewModel viewModel = new MainViewModel(scheduler);


    @Test
    public void changeSearchTextThenShowProgress() {
        boolean[] result = {false};
        viewModel.searchCityInProgress.asObservable()
                .subscribeOn(scheduler)
                .observeOn(scheduler)
                .subscribe(status -> result[0] = status);

        scheduler.triggerActions();
        viewModel.searchCityText.set("test");
        scheduler.triggerActions();

        assertTrue(result[0]);

    }

    @Test
    public void changeCitiesFromSearchThenHideProgress() {
        boolean[] result = {false};
        viewModel.searchCityInProgress.asObservable()
                .subscribeOn(scheduler)
                .observeOn(scheduler)
                .subscribe(status -> result[0] = status);

        scheduler.triggerActions();
        viewModel.onSearchForCityResult(new LinkedList<>());
        scheduler.triggerActions();

        assertFalse(result[0]);

    }

}