package com.ksteindl.adventofcode.advent2020.day12.model;

public enum Action {



    N(true),
    S(true),
    E(true),
    W(true),
    L(false),
    R(false),
    F(true);

    public boolean isMove;

    Action(boolean isMove) {
        this.isMove = isMove;
    }
}
