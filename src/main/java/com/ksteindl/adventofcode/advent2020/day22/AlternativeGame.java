package com.ksteindl.adventofcode.advent2020.day22;

import com.ksteindl.adventofcode.advent2020.day22.model.GameSetUp;

import java.util.*;
import java.util.stream.Collectors;

public class AlternativeGame {
    private Deque<Integer> player1;
    private Deque<Integer> player2;
    

    public Object part2(GameSetUp gameSetUp) {
        player1 = gameSetUp.getPlayer1deck();
        player2 = gameSetUp.getPlayer2deck();
        return getScore();
    }

    private int getScore() {
        Deque<Integer> winner = playGame(new LinkedList<>(player1), new LinkedList<>(player2)).winner;
        // System.out.println(winner);

        int score = 0;
        int i = 1;
        while (!winner.isEmpty()) {
            score += i * winner.pollLast();
            i++;
        }
        return score;
    }

    private Winner playGame(Deque<Integer> player1, Deque<Integer> player2) {
        Set<Integer> previous = new HashSet<>();

        while (true) {
            if (!previous.add(player1.hashCode() * 31 + player2.hashCode())) {
                return new Winner(player1, true);
            }
            int first = player1.pop();
            int second = player2.pop();

            if (player1.size() >= first && player2.size() >= second) {
                Deque<Integer> copy1 = player1.stream().limit(first).collect(Collectors.toCollection(LinkedList::new));
                Deque<Integer> copy2 = player2.stream().limit(second).collect(Collectors.toCollection(LinkedList::new));
                Winner subGameWinner = playGame(copy1, copy2);
                // System.out.println(subGameWinner);
                if (subGameWinner.player1Won) {
                    player1.addLast(first);
                    player1.addLast(second);
                } else {
                    player2.addLast(second);
                    player2.addLast(first);
                }
            } else if (first > second) {
                player1.addLast(first);
                player1.addLast(second);
            } else if (first < second) {
                player2.addLast(second);
                player2.addLast(first);
            }

            if (player1.isEmpty() || player2.isEmpty()) {
                boolean player1Won = player2.isEmpty();
                return new Winner(player1Won ? player1 : player2, player1Won);
            }
        }
    }

    private static class Winner {
        Deque<Integer> winner;
        boolean player1Won;


        public Winner(Deque<Integer> player1, boolean player1Won) {
            this.winner = player1;
            this.player1Won = player1Won;
        }
    }
}
