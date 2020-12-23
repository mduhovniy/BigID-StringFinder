package com.bigid.stringfinder.services;

import com.bigid.stringfinder.configuration.StringFinderConfiguration;
import com.bigid.stringfinder.dto.Record;
import com.bigid.stringfinder.interfaces.Aggregator;
import com.bigid.stringfinder.interfaces.Analyzer;
import com.bigid.stringfinder.utils.MatcherTask;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;


@Log4j2
@Service
@AllArgsConstructor
public class URLAnalyzer implements Analyzer {

    private final Aggregator aggregator;
    private final StringFinderConfiguration config;

    private final ExecutorService executor = initMatcherExecutor();

    public static final int BATCH_SIZE = 1000;
    public static final int MATCHER_TIMEOUT_IN_SECONDS = 30;

    private ExecutorService initMatcherExecutor() {
        int poolSize = Runtime.getRuntime().availableProcessors() - 1;
        log.info("Matchers Thread Pool initialized with size {}", poolSize);
        return Executors.newFixedThreadPool(poolSize);
    }

    @Override
    @SneakyThrows
    public Map<String, List<Record>> getRecordCountsFromUrl(URL url) {

        List<Map<String, List<Record>>> resultsForAggregation = new ArrayList<>();

        try (InputStream in = url.openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(in)))
        {
            final List<Future<Map<String, List<Record>>>> taskList = new ArrayList<>();
            String line = "";
            int batchNum = 0;

            while(line != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < BATCH_SIZE; i++) {
                    line = reader.readLine();
                    if (line != null) {
                        stringBuilder.append(line);
                    } else {
                        break;
                    }
                }
                taskList.add(executor.submit(new MatcherTask(stringBuilder.toString(), batchNum++, config.names)));
            }

            for(Future<Map<String, List<Record>>> future : taskList) {
                Map<String, List<Record>> batchResult = future.get(MATCHER_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
                resultsForAggregation.add(batchResult);
            }
        } catch (IOException e) {
            log.error(e);
            throw e;
        } finally {
            executor.shutdown();
        }

        return aggregator.aggregateResults(resultsForAggregation);
    }
}
