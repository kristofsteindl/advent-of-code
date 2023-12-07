package com.ksteindl.adventofcode.advent2023;

import java.util.List;
import java.util.Set;

public class Day1 extends Puzzle2023{
    
    private final List<String> NUMBERS = List.of(
            "one",
            "two",
            "three",
            "four",
            "five",
            "six",
            "seven",
            "eight",
            "nine");

    public static void main(String[] args) {
        var day1 = new Day1();
        day1.printSolutions();
    }
    
    @Override
    protected Number getFirstSolution(List<String> lines) {
        return lines.stream().mapToInt(this::parseLineWithDigits).sum();
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        return lines.stream().mapToInt(this::parseLineWithSpellings).sum();
    }

    private int parseLineWithSpellings(String line) {
        Integer first = null;
        Integer second = null;
        int i = 0;
        while (first == null) {
            if (Character.isDigit(line.charAt(i))) {
                first = Integer.valueOf(line.substring(i, i + 1));
            }
            for (String spelled : NUMBERS) {
                if (line.substring(0, i).contains(spelled)) {
                    first = NUMBERS.indexOf(spelled) + 1;
                }
            }
            i++;
        }
        i = line.length() - 1;
        while (second == null) {
            if (Character.isDigit(line.charAt(i))) {
                second = Integer.valueOf(line.substring(i, i + 1));
            }
            for (String spelled : NUMBERS) {
                if (line.substring(i).contains(spelled)) {
                    second = NUMBERS.indexOf(spelled) + 1;
                }
            }
            i--;
        }
        return first * 10 + second;
    }
    
    private int parseLineWithDigits(String line) {
        Integer first = null;
        Integer second = null;
        int i = 0;
        while (first == null) {
            if (Character.isDigit(line.charAt(i))) {
                first = Integer.valueOf(line.substring(i, i + 1));
            }
            i++;
        }
        i = line.length() - 1;
        while (second == null) {
            if (Character.isDigit(line.charAt(i))) {
                second = Integer.valueOf(line.substring(i, i + 1));
            }
            i--;
        }
        return first * 10 + second;
    }



    @Override
    public int getDay() {
        return 1;
    }
}
