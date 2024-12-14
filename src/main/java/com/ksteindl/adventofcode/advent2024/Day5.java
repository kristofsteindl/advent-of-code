package com.ksteindl.adventofcode.advent2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day5 extends Puzzle2024{
    
    record Relation(Integer bigger, Integer smaller) {
    }

    public static void main(String[] args) {
        var day = new Day5();
        day.printSolutions();
    }
    
    @Override
    protected Number getFirstSolution(List<String> lines) {
        List<Integer> order = new ArrayList<>();
        List<Relation> relaions = new ArrayList<>();
        for (int i = 0; !lines.get(i).isEmpty(); i++) {
            String[] sRels = lines.get(i).split("\\|");
            relaions.add(new Relation(Integer.parseInt(sRels[0]), Integer.parseInt(sRels[1])));
        }
        
        
        return 15;
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        return 25;
    }

    @Override
    public int getDay() {
        return 5;
    }
}
