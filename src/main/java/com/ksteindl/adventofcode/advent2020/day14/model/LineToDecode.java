package com.ksteindl.adventofcode.advent2020.day14.model;

import java.util.Map;

public class LineToDecode {

    private final Long address;
    private final Long originalValue;
    private final Map<Long, Long> memory;
    private final String mask;

    public LineToDecode(Long address, Long originalValue, Map<Long, Long> memory, String mask) {
        this.address = address;
        this.originalValue = originalValue;
        this.memory = memory;
        this.mask = mask;
    }

    public Long getAddress() {
        return address;
    }

    public Long getOriginalValue() {
        return originalValue;
    }

    public Map<Long, Long> getMemory() {
        return memory;
    }

    public String getMask() {
        return mask;
    }
}
