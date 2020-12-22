package com.bigid.stringfinder.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Record {

    private final String word;
    private final List<Pointer> pointers;
}
