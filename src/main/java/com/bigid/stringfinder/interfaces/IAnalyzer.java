package com.bigid.stringfinder.interfaces;

import com.bigid.stringfinder.dto.Record;

import java.util.List;

public interface IAnalyzer {

    List<Record> getRecordCounts(String url);
}
