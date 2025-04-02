package com.ksteindl.adventofcode.advent2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day2 extends Puzzle2024 {

    public static void main(String[] args) {
        new Day2().printSolutions();
    }


    @Override
    protected Number getSecondSolution(List<String> lines) {
        return lines.stream()
                .filter(this::isSafePermissive)
                .count();
    }
    
    @Override
    protected Number getFirstSolution(List<String> lines) {
        return lines.stream()
                .filter(this::isSafe)
                .count();
    }

    private boolean isSafePermissive(String line) {
        List<Integer> report = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();
        return IntStream.range(0, report.size())
                .mapToObj(i -> 
                        Stream.concat(report.subList(0,i).stream(), 
                        report.subList(i + 1, report.size()).stream())
                                .toList())
                .anyMatch(this::isSafe);

    }
    
    private boolean isSafe(String line) {
        List<Integer> report = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();
        return isSafe(report);
        
    }
    private boolean isSafe(List<Integer> report) {
        boolean incr = report.get(1) - report.get(0) > 0;
        for (int i = 1; i < report.size(); i++) {
            int diff = report.get(i) - report.get(i - 1);
            if (incr && diff < 0 || !incr && diff > 0) {
                return false;
            }
            diff = Math.abs(diff);
            if (diff == 0 || diff > 3) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getDay() {
        return 2;
    }
}
