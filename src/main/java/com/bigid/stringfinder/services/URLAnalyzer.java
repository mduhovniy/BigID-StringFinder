package com.bigid.stringfinder.services;

import com.bigid.stringfinder.dto.Record;
import com.bigid.stringfinder.interfaces.IAggregator;
import com.bigid.stringfinder.interfaces.IAnalyzer;
import com.bigid.stringfinder.interfaces.IMatcher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class URLAnalyzer implements IAnalyzer {

    private final IMatcher matcher;
    private final IAggregator aggregator;

    @Override
    public List<Record> getRecordCounts(String url) {
        return null;
    }
}
