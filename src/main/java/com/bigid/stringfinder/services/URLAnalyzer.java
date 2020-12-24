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

import javax.annotation.PreDestroy;
import java.io.*;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
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

        var resultsForAggregation = getResultsForAggregation(url);
        Duration duration;
        Instant start = Instant.now();
        Map<String, List<Record>> records = aggregator.aggregateResults(resultsForAggregation);
        duration = Duration.between(start, Instant.now());
        log.info("Aggregation on Nested For loops finished in {} nanos", duration.getNano());

        printResults(records);

        return records;
    }

    @Override
    @SneakyThrows
    public Map<String, List<Record>> getRecordCountsFromUrlStreamAggregation(URL url) {

        var resultsForAggregation = getResultsForAggregation(url);
        Duration duration;
        Instant start = Instant.now();
        Map<String, List<Record>> records = aggregator.aggregateResultsWithStream(resultsForAggregation);
        duration = Duration.between(start, Instant.now());
        log.info("Aggregation on Single Stream finished in {} nanos", duration.getNano());

        printResults(records);

        return records;
    }

    @Override
    @SneakyThrows
    public Map<String, List<Record>> getRecordCountsFromUrlParallelStreamAggregation(URL url) {

        var resultsForAggregation = getResultsForAggregation(url);
        Duration duration;
        Instant start = Instant.now();
        Map<String, List<Record>> records = aggregator.aggregateResultsWithParallelStream(resultsForAggregation);
        duration = Duration.between(start, Instant.now());
        log.info("Aggregation on Parallel Stream finished in {} nanos", duration.getNano());

        printResults(records);

        return records;
    }

    private List<Map<String, List<Record>>> getResultsForAggregation(URL url) throws InterruptedException, ExecutionException, TimeoutException, IOException {
        List<Map<String, List<Record>>> resultsForAggregation = new ArrayList<>();
        Instant start = Instant.now();
        Duration duration;

        try (InputStream in = url.openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(in)))
        {
            final List<Future<Map<String, List<Record>>>> taskList = new ArrayList<>();
            var line = "";
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

            duration = Duration.between(start, Instant.now());
            log.info("File fetched from URL {} and submitted for matching in {} batches in {} nanos",
                    url.toString(), batchNum, duration.getNano());

            start = Instant.now();

            for(Future<Map<String, List<Record>>> future : taskList) {
                resultsForAggregation.add(future.get(MATCHER_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS));
            }

            duration = Duration.between(start, Instant.now());
            log.info("All matchers finished in {} nanos", duration.getNano());


        } catch (IOException e) {
            log.error(e);
            throw e;
        }
        return resultsForAggregation;
    }

    private void printResults(Map<String, List<Record>> records) {

        records.forEach((key, value) -> log.info("{} --> [{}]", key, value));
        log.info("Aggregation finished for {} names", records.size());
    }

    @PreDestroy
    public void sutDown() {
        executor.shutdown();
    }
}
