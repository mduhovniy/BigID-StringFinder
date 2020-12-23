package com.bigid.stringfinder.utils;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Log4j2
public class Utils {

    public static void validateURL(String urlString) throws IOException {
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            conn.connect();
        } catch (MalformedURLException e) {
            log.error("URL passed {} is not valid", urlString);
            throw e;
        } catch (IOException e) {
            log.error("URL passed {} is not reachable", urlString);
            throw e;
        }
    }
}
