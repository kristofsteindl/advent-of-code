package com.ksteindl.adventofcode.advent2023;

import java.util.ArrayList;
import java.util.List;

public class Day11 extends Puzzle2023 {

    private List<Integer> horizontalEmptyLines;
    private List<Integer> verticalEmptyLines;
    
    private static class Galaxy {
        int row;
        int column;

        public Galaxy(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }

    public static void main(String[] args) {
        new Day11().printSolutions();
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        return calcSumOfDistance(lines, 1000000);
    }
    
    @Override
    protected Number getFirstSolution(List<String> lines) {
        return calcSumOfDistance(lines, 2);
    }
    
    private long calcSumOfDistance(List<String> lines, int factor) {
//        lines.forEach(System.out::println);
        verticalEmptyLines = collectHorizontalEmptyLines(lines);
        horizontalEmptyLines = collectVerticalEmptyLines(lines);
        List<Galaxy> galaxies = collectGalaxies(lines);
        long sumOfShortestPath = 0;
        for (int i = 0; i < galaxies.size(); i++) {
            Galaxy galaxy = galaxies.get(i);
            for (int j = 0; j < galaxies.size(); j++) {
                sumOfShortestPath += calcShortestPath(galaxy, galaxies.get(j), factor);
            }
        }
        return sumOfShortestPath / 2;
    }
    
    private int calcShortestPath(Galaxy one, Galaxy two, int factor) {
        if (one.column == two.column && one.row == two.row) {
            return 0;
        }
        int foundHorizontalEmptyLines = (int) horizontalEmptyLines.stream()
                .filter(emptyLine -> emptyLine > Math.min(one.column, two.column))
                .filter(emptyLine -> emptyLine < Math.max(one.column, two.column))
                .count();

        int foundVerticalEmptyLines = (int) verticalEmptyLines.stream()
                .filter(emptyLine -> emptyLine > Math.min(one.row, two.row))
                .filter(emptyLine -> emptyLine < Math.max(one.row, two.row))
                .count();
        int horizontalDist = Math.abs(one.column - two.column) - foundHorizontalEmptyLines + foundHorizontalEmptyLines * factor;
        int verticalDist = Math.abs(one.row - two.row) - foundVerticalEmptyLines + foundVerticalEmptyLines * factor;
        return verticalDist + horizontalDist;
    }
    
    private List<Galaxy> collectGalaxies(List<String> lines) {
        List<Galaxy> galaxies = new ArrayList<>();
        int i = 1;
        for (int row = 0; row < lines.size(); row++) {
            for (int column = 0; column < lines.get(row).length(); column++) {
                if (lines.get(row).charAt(column) == '#') {
                    galaxies.add(new Galaxy(column, row));
                }
            }
        }
        return galaxies;
    }
    
    private  List<Integer> collectVerticalEmptyLines(List<String> lines) {
        List<Integer> emptyRowIndices = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            boolean empty = true;
            int j = 0;
            while (empty && j < lines.get(i).length()) {
                if (lines.get(i).charAt(j) != '.') {
                    empty = false;
                }
                j++;
            }
            if (empty) {
                emptyRowIndices.add(i);
            }
        }
        return emptyRowIndices;
    }

    private List<Integer> collectHorizontalEmptyLines(List<String> lines) {
        List<Integer> emptyColumnIndices = new ArrayList<>();
        for (int column = 0; column < lines.get(0).length(); column++) {
            boolean emptyColumn = true;
            int row = 0;
            while (emptyColumn && row < lines.size()) {
                if (lines.get(row).charAt(column) != '.') {
                    emptyColumn = false;
                }
                row++;
            }
            if (emptyColumn) {
                emptyColumnIndices.add(column);
            }
        }
        return emptyColumnIndices;
    }

    @Override
    public int getDay() {
        return 11;
    }
}
