package com.bigid.stringfinder.utils;

import com.bigid.stringfinder.dto.Record;

import java.util.*;
import java.util.concurrent.Callable;

import static com.bigid.stringfinder.services.URLAnalyzer.BATCH_SIZE;

public class MatcherTask implements Callable<Map<String, List<Record>>> {

    private final String[] lines;
    private final int batchNum;
    private final Set<String> names;

    private final Map<String, List<Record>> records = new HashMap<>();

    public MatcherTask(String batchString, int batchNum, Set<String> names) {
        lines = batchString.split("\\r?\\n");
        this.batchNum = batchNum;
        this.names = names;
    }

    @Override
    public Map<String, List<Record>> call() throws Exception {

        for(int rowInBatch = 0; rowInBatch < lines.length; rowInBatch++) {
            String line = lines[rowInBatch];
            String[] words = line.split("[^a-zA-Z0-9']");
            int charOffset = 0;
            for (String word : words) {
                if (names.contains(word)) {
                    Record record = new Record(rowInBatch + (long) batchNum * BATCH_SIZE, charOffset);
                    records.computeIfAbsent(word, k -> new ArrayList<>());
                    records.get(word).add(record);
                }
                charOffset += word.length() + 1;
            }
        }
        return records;
    }
}