package com.ksteindl.adventofcode.advent2022;

import java.util.List;

public class Day8TreeCounter extends Puzzle2022 {

    int[][] forest;
    
    public static void main(String[] args) {
        new Day8TreeCounter().printSolutions();
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        forest = getForest(lines);
        int maxScenic = 0;
        for (int row = 1; row < forest.length - 1; row++) {
            for (int column = 1; column < forest[0].length - 1; column++) {
                int scenic = scenicFromAbove(row, column) *
                        scenicFromBelow(row, column) *
                        scenicFromRight(row, column) *
                        scenicFromLeft(row, column);
                if (scenic > maxScenic) {
                    maxScenic = scenic;
                }
            }
        }
        return maxScenic;
    }

    private int scenicFromAbove(int row, int column) {
        int scenicCounter = 0;
        for (int i = row - 1; i >= 0; i--) {
            scenicCounter++;
            if (forest[i][column] >= forest[row][column]) {
                return scenicCounter;
            }
        }
        return scenicCounter;
    }

    private int scenicFromBelow(int row, int column) {
        int scenicCounter = 0;
        for (int i = row + 1; i < forest.length; i++) {
            scenicCounter++;
            if (forest[i][column] >= forest[row][column]) {
                return scenicCounter;
            }
        }
        return scenicCounter;
    }

    private int scenicFromLeft(int row, int column) {
        int scenicCounter = 0;
        for (int i = column - 1; i >= 0; i--) {
            scenicCounter++;
            if (forest[row][i] >= forest[row][column]) {
                return scenicCounter;
            }
        }
        return scenicCounter;
    }

    private int scenicFromRight(int row, int column) {
        int scenicCounter = 0;
        for (int i = column + 1; i < forest[0].length; i++) {
            scenicCounter++;
            if (forest[row][i] >= forest[row][column]) {
                return scenicCounter;
            }
        }
        return scenicCounter;
    }
    
    @Override
    protected Number getFirstSolution(List<String> lines) {
        forest = getForest(lines);
        int visible = 2 * (forest.length + forest[0].length - 2);
        for (int row = 1; row < forest.length - 1; row++) {
            for (int column = 1; column < forest[0].length - 1; column++) {
                if (fromAbove(row, column) || 
                        fromBelow(row, column) ||
                        fromRight(row, column) ||
                        fromLeft(row, column)) {
                    visible++;
                }
            }
        }
        return visible;
    }
    
    private boolean fromAbove(int row, int column) {
        for (int i = 0; i < row; i++) {
            if (forest[i][column] >= forest[row][column]) {
                return false;
            }
        }
        return true;
    }

    private boolean fromBelow(int row, int column) {
        for (int i = forest.length - 1; i > row; i--) {
            if (forest[i][column] >= forest[row][column]) {
                return false;
            }
        }
        return true;
    }

    private boolean fromLeft(int row, int column) {
        for (int i = 0; i < column; i++) {
            if (forest[row][i] >= forest[row][column]) {
                return false;
            }
        }
        return true;
    }

    private boolean fromRight(int row, int column) {
        for (int i = forest[0].length - 1; i > column; i--) {
            if (forest[row][i] >= forest[row][column]) {
                return false;
            }
        }
        return true;
    }
    
    private int[][] getForest(List<String> lines) {
        forest = new int[lines.get(0).length()][lines.size()];
        for (int row = 0; row < lines.size(); row++) {
            for (int column = 0; column < lines.get(0).length(); column++) {
                forest[row][column] = Integer.parseInt(lines.get(row).substring(column, column + 1));
            }
        }
        return forest;
    }
    

    @Override
    public int getDay() {
        return 8;
    }
}
