package com.ksteindl.adventofcode.advent2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Day1 extends Puzzle2024{

    public static void main(String[] args) {
        var day = new Day1();
        day.printSolutions();
    }
    @Override
    protected Number getFirstSolution(List<String> lines) {
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        lines.forEach(line -> {
            String[] leftRight = line.split(" {3}");
            left.add(Integer.parseInt(leftRight[0]));
            right.add(Integer.parseInt(leftRight[1]));
        });
        left.sort(Integer::compareTo);
        right.sort(Integer::compareTo);
        int sumDiff = 0;
        for (int i = 0; i < lines.size(); i++) {
            sumDiff += Math.abs(left.get(i) - right.get(i));
        }
        return sumDiff;
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        List<Integer> left = new ArrayList<>();
        Map<Integer, Integer> rightMap = new HashMap<>();
        lines.forEach(line -> {
            String[] leftRight = line.split(" {3}");
            left.add(Integer.parseInt(leftRight[0]));
            Integer rightNumber = Integer.parseInt(leftRight[1]);
            rightMap.merge(rightNumber, 1,
                    (old, newV) -> old + 1);
        });
        return left
                .stream()
                .mapToInt(leftNumber -> {
                    Integer number = rightMap.get(leftNumber);
                    number = number == null ? 0 : number;
                    return number * leftNumber;

                })
                .sum();
    }

    @Override
    public int getDay() {
        return 1;
    }
}
