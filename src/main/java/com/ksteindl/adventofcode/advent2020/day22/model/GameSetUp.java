package com.ksteindl.adventofcode.advent2020.day22.model;

import java.util.LinkedList;
import java.util.List;

public class GameSetUp {

    private final LinkedList<Integer> player1deck;
    private final LinkedList<Integer> player2deck;

    public GameSetUp(List<String> lines) {
        this.player1deck = new LinkedList<>();
        this.player2deck = new LinkedList<>();
        int i = 1;
        while (!lines.get(i).isEmpty()) {
            this.player1deck.add(Integer.parseInt(lines.get(i)));
            i++;
        }
        i += 2;
        while (i < lines.size()) {
            this.player2deck.add(Integer.parseInt(lines.get(i)));
            i++;
        }
    }

    public LinkedList<Integer> getPlayer1deck() {
        return player1deck;
    }

    public LinkedList<Integer> getPlayer2deck() {
        return player2deck;
    }
}
