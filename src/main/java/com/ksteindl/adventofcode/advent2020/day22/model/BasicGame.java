package com.ksteindl.adventofcode.advent2020.day22.model;

public class BasicGame extends AbstractGame implements Game{


    public BasicGame(GameSetUp gameSetUp) {
        super(gameSetUp);
    }

    public void playRound() {
        Integer card1 = player1deck.pollFirst();
        Integer card2 = player2deck.pollFirst();
        if (card1 > card2) {
            player1deck.addLast(card1);
            player1deck.addLast(card2);
        } else {
            player2deck.addLast(card2);
            player2deck.addLast(card1);
        }
    }


}
