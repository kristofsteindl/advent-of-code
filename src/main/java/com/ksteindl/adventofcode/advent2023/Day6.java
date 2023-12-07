package com.ksteindl.adventofcode.advent2023;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day6 extends Puzzle2023 {

    public static void main(String[] args) {
        new Day6().printSolutions();
    }
    
    private static class Race2 {
        final Long distance;
        final Long time;
        
        Long limitForSmallest;
        Long limitForBiggest = 0l;
        Long smallest = 0l;
        Long biggest;

        public Race2(Long distance, Long time) {
            this.distance = distance;
            this.time = time;
            
            this.biggest = time;
            this.limitForSmallest = time;
        }

        boolean iterate() {
            boolean updated = false;
            Long diff = (limitForSmallest - smallest) / 2;
            if (diff > 1) {
                if (isWinning(smallest + diff)) {
                    smallest = smallest + diff;
                } else {
                    limitForSmallest = smallest + diff;
                }
                updated = true;
            }

            diff = (biggest - limitForBiggest) / 2;
            if (diff > 1) {
                if (isWinning(biggest - diff)) {
                    biggest = biggest - diff;
                } else {
                    limitForBiggest = biggest - diff;
                }
                updated = true;
            }

            return updated;
        }

        private boolean isWinning(Long time) {
            return (this.time - time) * time > distance;
        }
    }
    
    @Override
    protected Number getSecondSolution(List<String> lines) {
        Long time = concatNumbers(lines.get(0));
        Long distance = concatNumbers(lines.get(1));
        var beating = new Race2(distance, time);
        while (beating.iterate());
        return beating.smallest - beating.biggest;
    }
    
    
    private Long concatNumbers(String line) {
        var stringArray = line.split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < stringArray.length; i++) {
            builder.append(stringArray[i]);
        }
        return Long.valueOf(builder.toString());
    }

    private static class Race {
        Integer time;
        Integer distance;

        public Race(Integer time) {
            this.time = time;
        }

        Integer getWinningVariations() {
            int winningVariations = 0;
            for (int i = 0; i < time; i++) {
                if ((time - i) * i > distance) {
                    winningVariations++;
                }
            }
            return winningVariations;
        }
    }
    
 

    @Override
    protected Number getFirstSolution(List<String> lines) {
        List<Race> races = Arrays.stream(lines.get(0).split("\\s+"))
                .filter(string -> !string.contains(":"))
                .map(Integer::valueOf)
                .map(Race::new)
                .collect(Collectors.toList());
        var stringArray = lines.get(1).split("\\s+");
        for (int i = 1; i < stringArray.length; i++) {
            races.get(i - 1).distance = Integer.valueOf(stringArray[i]);
        }
        return races.stream().mapToInt(Race::getWinningVariations).reduce(1, (a, b) -> a * b);
    }

    @Override
    public int getDay() {
        return 6;
    }
}
