package com.ksteindl.adventofcode.advent2022;

import java.util.List;
import java.util.stream.Collectors;

public class Day6SignalDecoder extends Puzzle2022{

    public static void main(String[] args) {
        new Day6SignalDecoder().printSolutions();
    }


    @Override
    protected Number getFirstSolution(List<String> lines) {
        String signal = lines.get(0);
        int i = 14;
        while (!has4uniqueChar(signal.substring(i - 14, i))) {
           i++;
        }
        return i;
    }
    
    private boolean has4uniqueChar(String string) {
        return string.chars().boxed().collect(Collectors.toSet()).size() >= 14;
    }
    

    @Override
    protected Number getSecondSolution(List<String> lines) {
        return null;
    }

    @Override
    public int getDay() {
        return 6;
    }
}
