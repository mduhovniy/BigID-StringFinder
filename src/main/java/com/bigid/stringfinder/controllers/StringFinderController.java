package com.bigid.stringfinder.controllers;

import com.bigid.stringfinder.dto.Record;
import com.bigid.stringfinder.interfaces.Analyzer;
import com.bigid.stringfinder.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Log4j2
@AllArgsConstructor
@RestController
public class StringFinderController {

    private final Analyzer analyzer;

    @RequestMapping(value = "/getRecordCountsForURL", method = RequestMethod.POST)
    public @ResponseBody Map<String, List<Record>> getRecordCountsForURL(@RequestBody String sourceUrl) throws IOException {
        Utils.validateURL(sourceUrl);
        URL url = new URL(sourceUrl);
        return analyzer.getRecordCountsFromUrl(url);
    }
}
