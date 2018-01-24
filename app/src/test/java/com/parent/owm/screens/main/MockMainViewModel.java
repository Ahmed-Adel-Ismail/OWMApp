package com.parent.owm.screens.main;

import com.parent.entities.City;

import java.util.LinkedList;

public class MockMainViewModel extends MainViewModel {

    @Override
    protected void loadFavoriteCitiesFromPreferences() {
        // do nothing
    }

    protected boolean saveToPreferences(LinkedList<City> cities) {
        return true;
    }
}
