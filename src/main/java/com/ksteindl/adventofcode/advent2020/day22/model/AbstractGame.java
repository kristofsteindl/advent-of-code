package com.ksteindl.adventofcode.advent2020.day22.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

public abstract class AbstractGame implements Game{

    private static final Logger logger = LogManager.getLogger(AbstractGame.class);

    protected LinkedList<Integer> player1deck;
    protected LinkedList<Integer> player2deck;
    protected FinalResult finalResult;

    public AbstractGame(GameSetUp gameSetUp) {
        this.player1deck = new LinkedList<>(gameSetUp.getPlayer1deck());
        this.player2deck = new LinkedList<>(gameSetUp.getPlayer2deck());
    }

    public AbstractGame(LinkedList<Integer> player1deck, LinkedList<Integer> player2deck) {
        this.player1deck = player1deck;
        this.player2deck = player2deck;
    }

    public LinkedList<Integer> getPlayer1deck() {
        return player1deck;
    }

    public LinkedList<Integer> getPlayer2deck() {
        return player2deck;
    }

    @Override
    public FinalResult getFinalResult() {
        while (finalResult == null) {
            playRound();
        }
        return finalResult;
    }

    abstract void playRound();

    protected void addCardsToDecks(boolean player2Won, Integer card1, Integer card2) {
        if (player2Won) {
            player2deck.addLast(card2);
            player2deck.addLast(card1);
        } else {
            player1deck.addLast(card1);
            player1deck.addLast(card2);
        }
    }

    protected boolean isWon() {
        return player1deck.isEmpty() || player2deck.isEmpty();
    }

    protected int calculateWinningScore() {
        LinkedList<Integer> deck = player1deck.isEmpty() ? player2deck : player1deck;
        return calculateWinningScore(deck);
    }

    protected int calculateWinningScore(LinkedList<Integer> deck) {
        Iterator deckIterator = deck.descendingIterator();
        int i = 1;
        int score = 0;
        while (deckIterator.hasNext()) {
            score += (int) deckIterator.next() * i;
            i++;
        }
        return score;
    }

    protected void setFinalResult(FinalResult finalResult) {
        this.finalResult = finalResult;
    }

    public void logGame() {
        logger.debug("Player 1:");
        this.getPlayer1deck().forEach(card -> logger.debug(card));
        logger.debug("Player 2:");
        this.getPlayer2deck().forEach(card -> logger.debug(card));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractGame)) return false;
        AbstractGame that = (AbstractGame) o;
        return getPlayer1deck().equals(that.getPlayer1deck()) &&
                getPlayer2deck().equals(that.getPlayer2deck());
    }


    @Override
    public int hashCode() {
        return Objects.hash(getPlayer1deck(), getPlayer2deck());
    }

}
