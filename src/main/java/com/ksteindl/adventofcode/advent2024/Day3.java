package com.ksteindl.adventofcode.advent2024;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Day3 extends Puzzle2024{
    
    private Map<Integer, Boolean> enabled = new HashMap<>();

    public static void main(String[] args) {
        var day = new Day3();
        day.printSolutions();
    }
    @Override
    protected Number getFirstSolution(List<String> lines) {
        return lines.stream().mapToLong(this::addMultipliedNumbers).sum();
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        String oneLine = String.join("", lines);
        String[] doLines = oneLine.split("do\\(\\)");
        List<String> filteredDos = Arrays.stream(doLines)
                .map(this::filterOutDont)
                .toList();
        return filteredDos.stream().mapToLong(this::addMultipliedNumbers).sum();
    }
    
    private String filterOutDont(String doLine) {
        String filtered = doLine.split("don't\\(\\)")[0];
        return filtered;
    }
    
    private Long addMultipliedNumbers(String line) {
        return Arrays.stream(line.split("mul\\("))
                .map(prev -> prev.split("\\)")[0])
                .filter(Objects::nonNull)
                .filter(prev -> prev.contains(","))
                .mapToLong(this::multiplyBetweenRoundBrackets)
                .sum();
                
    }
    
    private Long multiplyBetweenRoundBrackets(String line) {
        String[] stringNumbers = line.split(",");
        if (!isEnabled()) {
            return 0L;
        }
        if (stringNumbers.length != 2) {
            return 0L;
        }
        Long result = convertAttributeToLong(stringNumbers[0]) * convertAttributeToLong(stringNumbers[1]);
        return result;
    }
    
    private boolean isEnabled() {
        return true;
    }
    
    private Long convertAttributeToLong(String attribute) {
        try {
            Long foundLond = Long.parseLong(attribute);   
            return foundLond.toString().length() == attribute.length() ? foundLond : 0;
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
    

    @Override
    public int getDay() {
        return 3;
    }
}
