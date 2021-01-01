package com.ksteindl.adventofcode.advent2020.day14.model;

import java.util.Map;

public class LineToDecodeBuilder {
    private Long address;
    private Long originalValue;
    private Map<Long, Long> memory;
    private String mask;

    public LineToDecodeBuilder setAddress(Long address) {
        this.address = address;
        return this;
    }

    public LineToDecodeBuilder setOriginalValue(Long originalValue) {
        this.originalValue = originalValue;
        return this;
    }

    public LineToDecodeBuilder setMemory(Map<Long, Long> memory) {
        this.memory = memory;
        return this;
    }

    public LineToDecodeBuilder setMask(String mask) {
        this.mask = mask;
        return this;
    }

    public LineToDecode createMemoryModifierWrapper() {
        return new LineToDecode(address, originalValue, memory, mask);
    }
}