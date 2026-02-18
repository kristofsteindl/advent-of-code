package com.ksteindl.adventofcode.advent2025;

import java.util.List;

public class Day1 extends Puzzle2025 {

    public static void main(String[] args) {
        var day = new Day1();
        day.printSolutions();
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        int sum = 0;
        int pointer = 50;
        for (String line : lines) {
            int rotation = Integer.parseInt(line.substring(1));
            if (line.startsWith("L")) {
                if (pointer == 0) {
                    sum--;
                }
                pointer -= rotation;
                if (pointer == 0) {
                    if rotation
                    sum++;
                }
                while (pointer < 0) {
                    sum++;
                    pointer += 100;
                }
            } else {
                pointer += rotation;
                while (pointer > 99) {
                    sum++;
                    pointer -= 100;
                }
            }
        }
        return sum;
    }

    @Override
    protected Number getFirstSolution(List<String> lines) {
        int sum = 0;
        int pointer = 50;
        for (String line : lines) {
            int rotation = Integer.parseInt(line.substring(1));
            if (line.startsWith("L")) {
                pointer -= rotation;
            } else {
                pointer += rotation;
            }
            pointer %= 100;
            if (pointer == 0) {
                sum++;
            }
        }
        return sum;
    }


    @Override
    public int getDay() {
        return 1;
    }
}
