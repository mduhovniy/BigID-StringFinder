package com.bigid.stringfinder.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class StringFinderConfiguration {

    public Set<String> names;

    @Value("${names}")
    private void setNames(String[] names) {
        this.names = new HashSet<>(Arrays.asList(names));
    }

}
