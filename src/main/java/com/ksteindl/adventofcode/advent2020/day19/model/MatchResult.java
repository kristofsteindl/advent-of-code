package com.ksteindl.adventofcode.advent2020.day19.model;

public class MatchResult {

    private final boolean valid;
    private final String leftOver;

    public MatchResult(boolean valid, String leftOver) {
        this.valid = valid;
        this.leftOver = leftOver;
    }

    public boolean isValid() {
        return valid;
    }

    public String getLeftOver() {
        return leftOver;
    }

    @Override
    public String toString() {
        return "MatchResult{" +
                "valid=" + valid +
                ", leftOver='" + leftOver + '\'' +
                '}';
    }
}
