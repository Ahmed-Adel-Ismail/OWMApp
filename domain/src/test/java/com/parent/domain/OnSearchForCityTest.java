package com.parent.domain;

import com.parent.entities.City;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OnSearchForCityTest {

    @Test
    public void applyWithValidSearchForMultipleCitiesThenReturnValidCitiesList() throws Exception {
        List<City> result = new MockCitySearcher().apply(MockCitySearcher.SEARCH_FOR_MULTIPLE_RESULTS_TEXT);
        assertEquals(MockCitySearcher.SEARCH_FOR_MULTIPLE_RESULTS_COUNT, result.size());
    }

    @Test
    public void applyWithValidSearchForOneCityThenReturnValidCitiesList() throws Exception {
        List<City> result = new MockCitySearcher().apply(MockCitySearcher.SEARCH_FOR_SINGLE_RESULT_TEXT);
        assertEquals(MockCitySearcher.SEARCH_FOR_SINGLE_RESULT_COUNT, result.size());
    }

    @Test
    public void applyWithInvalidSearchThenReturnEmptyList() throws Exception {
        List<City> result = new MockCitySearcher().apply("");
        assertTrue(result.isEmpty());
    }
}