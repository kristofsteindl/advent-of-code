package com.ksteindl.adventofcode.advent2020.day22.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

public class RecursiveGame extends AbstractGame implements Game{

    private static final Logger logger = LogManager.getLogger(RecursiveGame.class);

    private Set<Game> roundHistory = new HashSet<>();

    public RecursiveGame(GameSetUp gameSetUp) {
        super(gameSetUp);
    }

    public RecursiveGame(LinkedList<Integer> player1deck, LinkedList<Integer> player2deck) {
        super(player1deck, player2deck);
    }

    public void playRound() {
        if (!roundHistory.add(new RecursiveGame(new LinkedList<>(player1deck), new LinkedList<>(player2deck)))) {
            setFinalResult(new FinalResult(calculateWinningScore(this.player1deck)));
            return;
        };
        Integer card1 = player1deck.pop();
        Integer card2 = player2deck.pop();
        if (player1deck.size() >= card1 && player2deck.size() >= card2) {
            RecursiveGame newGame = new RecursiveGame(
                    player1deck.stream().limit(card1).collect(Collectors.toCollection(LinkedList::new)),
                    player2deck.stream().limit(card2).collect(Collectors.toCollection(LinkedList::new)));
            FinalResult newFinalResult = newGame.getFinalResult();
            addCardsToDecks(newFinalResult.isPlayer2won(), card1, card2);
        } else {
            addCardsToDecks(card2 > card1, card1, card2);
        }
        if (isWon()) {
            setFinalResult(new FinalResult(player1deck.isEmpty(), calculateWinningScore()));
        }
    }


}
