package com.ksteindl.adventofcode.advent2020.day12.model;

public class Command {

    private final Action action;
    private final int value;

    public Command(Action action, int value) {
        this.action = action;
        this.value = value;
    }

    public Action getAction() {
        return action;
    }

    public int getValue() {
        return value;
    }
}
