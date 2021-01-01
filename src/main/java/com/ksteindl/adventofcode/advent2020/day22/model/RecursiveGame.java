package com.ksteindl.adventofcode.advent2020.day22.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class RecursiveGame extends AbstractGame implements Game{

    private static final Logger logger = LogManager.getLogger(RecursiveGame.class);

    private Set<Integer> roundHistory = new HashSet<>();

    public RecursiveGame(GameSetUp gameSetUp) {
        super(gameSetUp);
    }

    public RecursiveGame(LinkedList<Integer> player1deck, LinkedList<Integer> player2deck) {
        super(player1deck, player2deck);
    }

    @Override
    public void playRound() {
        if (!roundHistory.add(player1deck.hashCode() * 31 + player2deck.hashCode())) {
            return;
        };
        Integer card1 = player1deck.pop();
        Integer card2 = player2deck.pop();
        if (player1deck.size() >= card1 && player2deck.size() >= card2) {
            RecursiveGame newGame = new RecursiveGame(new LinkedList<>(player1deck), new LinkedList<>(player2deck));
            while (!newGame.calculateWinningState().isWon()) {
                newGame.playRound();
            }
            addCardsToDecks(newGame.calculateWinningState().isPlayer2won(), card1, card2);
        } else {
            addCardsToDecks(card2 > card1, card1, card2);
        }
    }

    @Override
    public WinningState calculateWinningState() {
        if (roundHistory.contains(player1deck.hashCode() * 31 + player2deck.hashCode())) {
            WinningState winningState = new WinningState();
            winningState.setWon(true);
            winningState.setWinningScore(calculateWinningScore(this.player1deck));
            return winningState;
        }
        return super.calculateWinningState();
    }



    private static RecursiveGame clone(RecursiveGame recursiveGame) {
        LinkedList<Integer> l1 = new LinkedList<Integer>(recursiveGame.getPlayer1deck());
        LinkedList<Integer> l2 = new LinkedList<Integer>(recursiveGame.getPlayer2deck());
        return new RecursiveGame(l1, l2);
    }

    private void addCardsToDecks(boolean player2Won, Integer card1, Integer card2) {
        if (player2Won) {
            player2deck.addLast(card2);
            player2deck.addLast(card1);
        } else {
            player1deck.addLast(card1);
            player1deck.addLast(card2);
        }
    }

}
