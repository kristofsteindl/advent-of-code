package com.ksteindl.adventofcode.advent2022;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class Day4AssignmentOverlapFinder extends Puzzle2022 {

    private static final Logger logger = LogManager.getLogger(Day4AssignmentOverlapFinder.class);

    public static void main(String[] args) {
        new Day4AssignmentOverlapFinder().printSolutions();
    }

    @Override
    public Number getSecondSolution(List<String> lines) {
        List<Pair> pairs = lines.stream().map(this::mapPair).collect(Collectors.toList());
        return pairs.stream().filter(this::partiallyOverlap).count();
    }

    private boolean partiallyOverlap(Pair pair) {
        if (pair.firstMin < pair.secondMin && pair.firstMax < pair.secondMin) {
            return false;
        }
        if (pair.secondMin < pair.firstMin && pair.secondMax < pair.firstMin) {
            return false;
        }
        return true;
    }

    @Override
    public Number getFirstSolution(List<String> lines) {
        List<Pair> pairs = lines.stream().map(this::mapPair).collect(Collectors.toList());
        return pairs.stream().filter(this::fullyOverlap).count();
    }


    
    private boolean fullyOverlap(Pair pair) {
        if (pair.firstMin <= pair.secondMin && pair.firstMax >= pair.secondMax) {
            return true;
        }
        if (pair.secondMin <= pair.firstMin && pair.secondMax >= pair.firstMax) {
            return true;
        }
        return false;
    }
    
    private Pair mapPair(String line) {
        String[] ranges = line.split(",");
        String[] first = ranges[0].split("-");
        String[] second = ranges[1].split("-");
        return new Pair(
                Integer.valueOf(first[0]),
                Integer.valueOf(first[1]),
                Integer.valueOf(second[0]),
                Integer.valueOf(second[1])
        );
    }
    
    static class Pair {
        Integer firstMin;
        Integer firstMax;
        Integer secondMin;
        Integer secondMax;

        public Pair(Integer firstMin, Integer firstMax, Integer secondMin, Integer secondMax) {
            this.firstMin = firstMin;
            this.firstMax = firstMax;
            this.secondMin = secondMin;
            this.secondMax = secondMax;
        }
    }
    
    
    @Override
    public int getDay() {
        return 4;
    }
    

   


}