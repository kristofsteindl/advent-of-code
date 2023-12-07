package com.ksteindl.adventofcode.advent2022;

import java.util.Arrays;
import java.util.List;

public class Day18 extends Puzzle2022 {
    
    int dimension;
    boolean[][][] matrix;

    public static void main(String[] args) {
        new Day18().printSolutions();
    }
    
    @Override
    protected Number getFirstSolution(List<String> lines) {
        dimension = lines.stream().flatMap(line -> Arrays.stream(line.split(","))).mapToInt(Integer::parseInt).max().getAsInt();
        matrix = initMatrix(lines);
        return countSurface();
    }
    
    private int countSurface() {
        int sum = 0;
        for (int x = 0; x < dimension + 1; x++) {
            for (int y = 0; y < dimension + 1; y++) {
                for (int z = 0; z < dimension + 1; z++) {
                    if (matrix[x][y][z]) {
                        sum += countCubeSurface(x,y,z);
                    }
                }
            }
        }
        return sum;
    }
    
    int countCubeSurface(int x, int y, int z) {
        int surfaceCount = 0;
        if (x == 0 || !matrix[x - 1][y][z]) {
            surfaceCount++;
        }
        if (!matrix[x + 1][y][z]) {
            surfaceCount++;
        }
        if (y == 0 || !matrix[x][y - 1][z]) {
            surfaceCount++;
        }
        if (!matrix[x][y + 1][z]) {
            surfaceCount++;
        }
        if (z == 0 || !matrix[x][y][z - 1]) {
            surfaceCount++;
        }
        if (!matrix[x][y][z + 1]) {
            surfaceCount++;
        }
        return surfaceCount;
    }
    
    private boolean[][][] initMatrix(List<String> lines) {
        boolean[][][] matrix = new boolean[dimension + 2][dimension + 2][dimension + 2];
        for (String line :lines) {
            String[] strings = line.split(",");
            matrix
                    [Integer.parseInt(strings[0])]
                    [Integer.parseInt(strings[1])]
                    [Integer.parseInt(strings[2])] = true;
        }
        return matrix;
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        return null;
    }

    @Override
    public int getDay() {
        return 18;
    }
}
