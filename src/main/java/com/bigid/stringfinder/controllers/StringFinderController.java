package com.bigid.stringfinder.controllers;

import com.bigid.stringfinder.dto.Record;
import com.bigid.stringfinder.interfaces.IAnalyzer;
import com.bigid.stringfinder.utils.IOUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Log4j2
@AllArgsConstructor
@RestController
public class StringFinderController {

    private final IAnalyzer analyzer;

    @RequestMapping(value = "/getRecordCountsForURL", method = RequestMethod.POST)
    public @ResponseBody
    List<Record> getRecordCountsForURL(@RequestBody String url) throws IOException {
        IOUtils.validateURL(url);
        return analyzer.getRecordCounts(url);
    }
}
