package com.bigid.stringfinder.dto;

import lombok.Value;

@Value
public class Pointer {

    private final long lineOffset;
    private final long charOffset;
}
