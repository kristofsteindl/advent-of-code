package com.ksteindl.adventofcode.advent2020.day11.model;

public enum Seat {

    FLOOR('.'),
    OCCUPIED('#'),
    EMPTY('L');

    public final char symbol;

    Seat(char symbol) {
        this.symbol = symbol;
    }
}
