package com.ksteindl.adventofcode.advent2020.day10.model;

public class Adapter {

    private final int index;
    private final int value;
    private final boolean isValidToRemove;

    public Adapter(int index, int value, boolean isValidToRemove) {
        this.index = index;
        this.value = value;
        this.isValidToRemove = isValidToRemove;
    }

    public int getIndex() {
        return index;
    }

    public int getValue() {
        return value;
    }

    public boolean isValidToRemove() {
        return isValidToRemove;
    }
}
