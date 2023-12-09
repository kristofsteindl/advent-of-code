package com.ksteindl.adventofcode.advent2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day9 extends Puzzle2023 {

    public static void main(String[] args) {
        new Day9().printSolutions();
    }
    
    private static class History {
        
        List<Long> originals;

        History(String line) {
            originals = Arrays.stream(line.split(" ")).map(Long::valueOf).collect(Collectors.toList());
        }
        
        long extrapolateOnWard() {
            List<List<Long>> calculated = calcHistories();
            for (int i = calculated.size() - 1; i > 0; i--) {
                List<Long> numbers = calculated.get(i);
                List<Long> belowNumbers = calculated.get(i - 1);
                belowNumbers.add(belowNumbers.get(belowNumbers.size() - 1) + numbers.get(numbers.size() -1));
            }
            return originals.get(originals.size() -1 );
        }

        List<List<Long>> calcHistories() {
            List<List<Long>> calculated = new ArrayList<>();
            calculated.add(originals);
            List<Long> prevNumbers = List.copyOf(originals);
            while (prevNumbers.stream().filter(number -> number == 0).count() < prevNumbers.size()) {
                List<Long> newNumbers = new ArrayList<>();
                for (int i = 0; i < prevNumbers.size() - 1; i++) {
                    newNumbers.add(prevNumbers.get(i + 1) - prevNumbers.get(i));
                }
                calculated.add(newNumbers);
                prevNumbers = newNumbers;
            }
            return calculated;
        }

        long extrapolateBackWard() {
            List<List<Long>> calculated = calcHistories();
            for (int i = calculated.size() - 1; i > 0; i--) {
                List<Long> numbers = calculated.get(i);
                List<Long> belowNumbers = calculated.get(i - 1);
                belowNumbers.add(0, belowNumbers.get(0) - numbers.get(0));
            }
            return originals.get(0);
        }
        
    }
    

    
    @Override
    protected Number getFirstSolution(List<String> lines) {
        return lines.stream().map(History::new).mapToLong(History::extrapolateOnWard).sum();
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        return lines.stream().map(History::new).mapToLong(History::extrapolateBackWard).sum();
    }

    @Override
    public int getDay() {
        return 9;
    }
}
