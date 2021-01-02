package com.ksteindl.adventofcode.advent2020.day22.model;

public class BasicGame extends AbstractGame implements Game{


    public BasicGame(GameSetUp gameSetUp) {
        super(gameSetUp);
    }

    public void playRound() {
        Integer card1 = player1deck.pollFirst();
        Integer card2 = player2deck.pollFirst();
        addCardsToDecks(card2 > card1, card1, card2);
        if (isWon()) {
            setFinalResult(new FinalResult(player2deck.isEmpty(), calculateWinningScore()));
        }
    }

}
