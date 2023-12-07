package com.ksteindl.adventofcode.advent2023;

import org.checkerframework.checker.units.qual.C;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class Day4 extends Puzzle2023{
    public static void main(String[] args) {
        var day = new Day4();
        day.printSolutions();
    }
    @Override
    protected Number getFirstSolution(List<String> lines) {
        return lines.stream()
                .map(this::createCard)
                .mapToInt(Card::getPoints)
                .sum();
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        List<Card> cards = lines.stream().map(this::createCard).collect(toList());
        Map<Card, AtomicInteger> counter = new HashMap<>();
        cards.forEach(card -> counter.put(card, new AtomicInteger(1)));
        for (Card card : cards) {
            for (int i = card.id + 1; i < card.id + 1 + card.getMatches(); i++) {
                Card cardToAdd = cards.get(i - 1);
                counter.get(cardToAdd).addAndGet(counter.get(card).get());
            }
        }
        return counter.values().stream().mapToInt(AtomicInteger::get).sum();
    }
    
    static class Card {
        Integer id;
        Set<Integer> winning;
        Set<Integer> actual;

        public Card(Integer id, Set<Integer> winning, Set<Integer> actual) {
            this.id = id;
            this.winning = winning;
            this.actual = actual;
        }
        
        int getPoints() {
            Set<Integer> actualWinning = new HashSet<>(actual);
            actualWinning.retainAll(winning);
            return (int) Math.pow(2, actualWinning.size() - 1);
        }

        int getMatches() {
            Set<Integer> actualWinning = new HashSet<>(actual);
            actualWinning.retainAll(winning);
            return actualWinning.size();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Card card = (Card) o;

            return id.equals(card.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }
    
    private Card createCard(String line) {
        var numbersSets = line.split(": ");
        var id = Integer.valueOf(numbersSets[0].split("\\s+")[1]);
        String[] stringSets = numbersSets[1].split(" \\|");
        var winning = Arrays.stream(stringSets[0].split(" "))
                .filter(string -> !string.isEmpty())
                .mapToInt(Integer::valueOf)
                .mapToObj(nu -> nu)
                .collect(toSet());
        var actual = Arrays.stream(stringSets[1].split(" "))
                .filter(string -> !string.isEmpty())
                .mapToInt(Integer::valueOf)
                .mapToObj(nu -> nu)
                .collect(toSet());
        return new Card(id, winning, actual);
    }
    

    @Override
    public int getDay() {
        return 4;
    }
}
