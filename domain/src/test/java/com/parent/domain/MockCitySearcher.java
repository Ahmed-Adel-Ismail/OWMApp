package com.parent.domain;


import com.chaining.Chain;
import com.functional.curry.SwapCurry;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


class MockCitySearcher extends CitySearcher {

    static final String SEARCH_FOR_MULTIPLE_RESULTS_TEXT = "n";
    static final int SEARCH_FOR_MULTIPLE_RESULTS_COUNT = 3;

    static final String SEARCH_FOR_SINGLE_RESULT_TEXT = "hu";
    static final int SEARCH_FOR_SINGLE_RESULT_COUNT = 1;

    private static final String JSON_FILE = "[{" +
            "    \"id\": 707860," +
            "    \"name\": \"Hurzuf\"," +
            "    \"country\": \"UA\"," +
            "    \"coord\": {" +
            "      \"lon\": 34.283333," +
            "      \"lat\": 44.549999" +
            "    }" +
            "  }," +
            "  {" +
            "    \"id\": 519188," +
            "    \"name\": \"Novinki\"," +
            "    \"country\": \"RU\"," +
            "    \"coord\": {" +
            "      \"lon\": 37.666668," +
            "      \"lat\": 55.683334" +
            "    }" +
            "  }," +
            "  {" +
            "    \"id\": 1283378," +
            "    \"name\": \"Gorkhā\"," +
            "    \"country\": \"NP\"," +
            "    \"coord\": {" +
            "      \"lon\": 84.633331," +
            "      \"lat\": 28" +
            "    }" +
            "  }," +
            "  {" +
            "    \"id\": 1270260," +
            "    \"name\": \"State of Haryāna\"," +
            "    \"country\": \"IN\"," +
            "    \"coord\": {" +
            "      \"lon\": 76," +
            "      \"lat\": 29" +
            "    }" +
            "  }," +
            "  {" +
            "    \"id\": 708546," +
            "    \"name\": \"Holubynka\"," +
            "    \"country\": \"UA\"," +
            "    \"coord\": {" +
            "      \"lon\": 33.900002," +
            "      \"lat\": 44.599998" +
            "    }" +
            "  }]";

    @Override
    protected JsonReader createJsonReader() {
        return Chain.let(StandardCharsets.UTF_8)
                .map(JSON_FILE::getBytes)
                .map(ByteArrayInputStream::new)
                .map(SwapCurry.toFunction(InputStreamReader::new, "UTF-8"))
                .map(JsonReader::new)
                .call();
    }
}
