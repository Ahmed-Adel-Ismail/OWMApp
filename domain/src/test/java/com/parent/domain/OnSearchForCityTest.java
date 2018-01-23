package com.parent.domain;

import com.parent.entities.City;

import org.junit.Assert;
import org.junit.Test;

import static com.parent.domain.MockCitySearcher.SEARCH_FOR_VALID_RESULTS_ID;
import static org.junit.Assert.assertEquals;

public class OnSearchForCityTest {

    @Test
    public void applyWithValidSearchForThenReturnCity() throws Exception {
        new MockCitySearcher().apply(MockCitySearcher.SEARCH_FOR_VALID_RESULTS_TEXT)
                .map(City::getId)
                .apply(id -> assertEquals(SEARCH_FOR_VALID_RESULTS_ID, id));
    }

    @Test
    public void applyWithInvalidSearchThenReturnEmptyList() throws Exception {
        new MockCitySearcher().apply(MockCitySearcher.SEARCH_FOR_INVALID_RESULTS_TEXT)
                .defaultIfEmpty(null)
                .apply(Assert::assertNull);
    }
}