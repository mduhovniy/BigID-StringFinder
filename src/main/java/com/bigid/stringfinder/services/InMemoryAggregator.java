package com.bigid.stringfinder.services;

import com.bigid.stringfinder.dto.Record;
import com.bigid.stringfinder.interfaces.Aggregator;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class InMemoryAggregator implements Aggregator {

    @Override
    public Map<String, List<Record>> aggregateResults(List<Map<String, List<Record>>> resultsForAggregation) {
        Map<String, List<Record>> aggregatedRecords = new HashMap<>();

        for(Map<String, List<Record>> res : resultsForAggregation) {
            for(Map.Entry<String, List<Record>> entry : res.entrySet()) {
                aggregatedRecords.putIfAbsent(entry.getKey(), new ArrayList<>());
                aggregatedRecords.get(entry.getKey()).addAll(entry.getValue());
            }
        }
        printResults(aggregatedRecords);
        return aggregatedRecords;
    }

    private void printResults(Map<String, List<Record>> aggregatedRecords) {

        for(Map.Entry<String, List<Record>> entry : aggregatedRecords.entrySet()) {
            log.info("{} --> [{}]", entry.getKey(), entry.getValue());
        }
    }
}
