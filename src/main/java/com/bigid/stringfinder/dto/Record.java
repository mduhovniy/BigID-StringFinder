package com.bigid.stringfinder.dto;


import lombok.Value;


@Value
public class Record {

    long lineOffset;
    long charOffset;

    @Override
    public String toString() {
        return "lineOffset=" + lineOffset + ", charOffset=" + charOffset;
    }
}
