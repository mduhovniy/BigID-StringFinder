package com.bigid.stringfinder.interfaces;

import com.bigid.stringfinder.dto.Record;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public interface Analyzer {

    Map<String, List<Record>> getRecordCountsFromUrl(URL url) throws IOException;
    Map<String, List<Record>> getRecordCountsFromUrlStreamAggregation(URL url) throws IOException;
    Map<String, List<Record>> getRecordCountsFromUrlParallelStreamAggregation(URL url) throws IOException;
}
