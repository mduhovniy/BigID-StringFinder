package com.bigid.stringfinder;

import com.bigid.stringfinder.interfaces.Analyzer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StringFinderApplicationTests {

    private static final String TEST_URL = "http://norvig.com/big.txt";

    @Autowired
    Analyzer analyzer;

    @Test
    void loadFromSiteAndAggregateOnNestedForLoops() throws IOException {
        assertThat(analyzer.getRecordCountsFromUrl(new URL(TEST_URL))).isNotNull();
    }
    @Test
    void loadFromSiteAndAggregateOnSingleStream() throws IOException {
        assertThat(analyzer.getRecordCountsFromUrlStreamAggregation(new URL(TEST_URL))).isNotNull();
    }
    @Test
    void loadFromSiteAndAggregateOnParallelStream() throws IOException {
        assertThat(analyzer.getRecordCountsFromUrlParallelStreamAggregation(new URL(TEST_URL))).isNotNull();
    }
}
