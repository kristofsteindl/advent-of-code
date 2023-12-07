package com.ksteindl.adventofcode.advent2023;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day7 extends Puzzle2023 {

    static List<Character> order1 = List.of('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2');
    static List<Character> order2 = List.of('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J');

    List<Character> order;

    public static void main(String[] args) {
        new Day7().printSolutions();
    }
    
    @Override
    protected Number getFirstSolution(List<String> lines) {
        order = order1;
        List<Hand> hands = lines.stream()
                .map(this::getHand)
                .sorted(Comparator.comparingLong(one -> one.getStrength(false)))
                .collect(Collectors.toList());
        return LongStream.range(0, hands.size()).map(i -> (i + 1) * hands.get((int)i).bid).sum();
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        order = order2;
        List<Hand> hands = lines.stream()
                .map(this::getHand)
                .sorted(Comparator.comparingLong(one -> one.getStrength(true)))
                .collect(Collectors.toList());
        return LongStream.range(0, hands.size()).map(i -> (i + 1) * hands.get((int)i).bid).sum();
    }


    private Hand getHand(String line) {
        var handRank = line.split(" ");
        Hand hand = new Hand();
        hand.bid = Integer.valueOf(handRank[1]);
        for (int i = 0; i < 5; i++) {
            hand.cards.add(handRank[0].charAt(i));
        }
        return hand;
    }

    private static class Hand {
        List<Character> cards = new ArrayList<>();
        Map<Character, Integer> cardMap;
        Integer bid;
        private Long strength;
        
        Long brakeTie() {
            long sum = 0;
            for (int i = 0; i < 5; i++) {
                sum += ((long) Math.pow(10, (4 - i) * 2)) * (order2.size() - order2.indexOf(cards.get(i)));
            }
            return sum;
        }

        private Long getStrength(boolean second) {
            if (strength != null) {
                return strength;
            }
            countCardMap(second);
            int primaryStrength;
            if (cardMap.containsValue(5)) {
                primaryStrength = 6;
            } else if (cardMap.containsValue(4)) {
                primaryStrength = 5;
            } else if (cardMap.containsValue(3) && cardMap.containsValue(2)) {
                primaryStrength = 4;
            } else if (cardMap.containsValue(3)) {
                primaryStrength = 3;
            } else if (cardMap.values().stream().filter(ai -> ai == 2).count() == 2) {
                primaryStrength = 2;
            } else if (cardMap.containsValue(2)) {
                primaryStrength = 1;
            } else {
                primaryStrength = 0;
            }
            strength = primaryStrength * (long) Math.pow(10, 10) + brakeTie();
            return strength;
        }
        
        private void countCardMap(boolean second) {
            cardMap = new HashMap<>();
            Map<Character, AtomicInteger> atomicCardMap = new HashMap<>();
            for (int i = 0; i < 5; i++) {
                var card = cards.get(i);
                var number = atomicCardMap.get(card);
                if (number == null) {
                    atomicCardMap.put(card, new AtomicInteger(1));
                } else {
                    number.addAndGet(1);
                }
            }
            int maxCount = 0;
            char maxChar = 'X';
            for (Character c : atomicCardMap.keySet()) {
                int number = atomicCardMap.get(c).get();
                if (c != 'J') {
                    if (maxCount < number) {
                        maxCount = number;
                        maxChar = c;
                    }
                    cardMap.put(c, number);
                }
                cardMap.put(c, number);
            }
            int jNumber = cardMap.get('J') == null ? 0 : cardMap.get('J');
            if (second) {
                cardMap.put(maxChar, maxCount + jNumber);
            }
        }
    }
    

    @Override
    public int getDay() {
        return 7;
    }
}
