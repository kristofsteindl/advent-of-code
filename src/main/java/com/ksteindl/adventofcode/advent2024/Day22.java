package com.ksteindl.adventofcode.advent2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day22 extends Puzzle2024 {

    public static void main(String[] args) {
        new Day22().printSolutions();
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        List<Map<List<Integer>, Integer>> report =
                lines.stream().map(this::generatePricesMap).toList();
        Map<List<Integer>, Integer> finalResults = report.stream()
                .flatMap(map -> map.entrySet().stream()).collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum));

        return finalResults.values().stream().mapToInt(Integer::intValue).max().orElse(0);
    }

    private Map<List<Integer>, Integer> generatePricesMap(String line) {
        long prev = Integer.parseInt(line);
        Map<List<Integer>, Integer> prices = new HashMap<>();
        List<Integer> previous4Changes = new ArrayList<>();
        long next;
        int nextLastDigit;
        int prevLastDigit;
        for (int i = 0; i < 2000; i++) {
            next = generateNextSecretNumber(prev);
            nextLastDigit = (int) next % 10;
            prevLastDigit = (int) prev % 10;
            int change = nextLastDigit - prevLastDigit;
            previous4Changes.add(change);
            if (previous4Changes.size() > 4) {
                previous4Changes.remove(0);
                List<Integer> key = new ArrayList<>(previous4Changes);
                prices.putIfAbsent(key, nextLastDigit);
            }
            prev = next;
        }
        return prices;
    }

    @Override
    protected Number getFirstSolution(List<String> lines) {
        return lines.stream().mapToLong(this::get2000thSecretNumber).sum();
    }

    private long get2000thSecretNumber(String line) {
        long next = Integer.parseInt(line);
        for (int i = 0; i < 2000; i++) {
            next = generateNextSecretNumber(next);
        }
        return next;
    }

    private long generateNextSecretNumber(long prev) {
        long next = prev;
        next *= 64;
        next = next ^ prev;
        next = next % 16777216;

        prev = next;
        next /= 32;
        next = next ^ prev;
        next = next % 16777216;

        prev = next;
        next *= 2048;
        next = next ^ prev;
        next = next % 16777216;

        return next;
    }

    @Override
    public int getDay() {
        return 22;
    }
}
