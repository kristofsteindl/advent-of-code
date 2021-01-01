package com.ksteindl.adventofcode.advent2020.day16.model;

public class Pair {

    private final Integer lower;
    private final Integer upper;

    public Pair(Integer lower, Integer upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public Integer getLower() {
        return lower;
    }

    public Integer getUpper() {
        return upper;
    }
}
