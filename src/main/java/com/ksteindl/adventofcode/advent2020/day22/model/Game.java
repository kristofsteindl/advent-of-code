package com.ksteindl.adventofcode.advent2020.day22.model;

import java.util.LinkedList;

public interface Game {

    void playRound();
    WinningState calculateWinningState();
    void logGame();

    LinkedList<Integer> getPlayer1deck();
    LinkedList<Integer> getPlayer2deck();
}
