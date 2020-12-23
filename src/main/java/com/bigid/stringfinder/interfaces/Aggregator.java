package com.bigid.stringfinder.interfaces;

import com.bigid.stringfinder.dto.Record;

import java.util.List;
import java.util.Map;

public interface Aggregator {

    Map<String, List<Record>> aggregateResults(List<Map<String, List<Record>>> resultsForAggregation);
}
