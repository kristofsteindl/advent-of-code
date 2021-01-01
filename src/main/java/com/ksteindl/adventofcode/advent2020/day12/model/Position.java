package com.ksteindl.adventofcode.advent2020.day12.model;

public interface Position {

    void moveDirection(Command command);

    void moveFroward(int value);

    void turn(Command command);

    double getX();

    double getY();
}
