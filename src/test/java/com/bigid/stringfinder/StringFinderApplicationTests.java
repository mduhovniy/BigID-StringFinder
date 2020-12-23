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

    @Autowired
    Analyzer analyzer;

    @Test
    void contextLoads() {
    }

    @Test
    void loadFromSite() throws IOException {
        String url = "http://norvig.com/big.txt";
        assertThat(analyzer.getRecordCountsFromUrl(new URL(url))).isNotNull();
    }
}
