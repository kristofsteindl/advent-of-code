package com.ksteindl.adventofcode.advent2024;

import com.ksteindl.adventofcode.advent2021.day04.Board;

import java.util.List;

public class Day14 extends Puzzle2024 {

    private static int dimX;
    private static int dimY;

    public static void main(String[] args) {
        new Day14().printSolutions();
    }

    private static class Robot {
        int posX;
        int posY;
        int velX;
        int velY;

        public Robot(int posX, int posY, int velX, int velY) {
            this.posX = posX;
            this.posY = posY;
            this.velX = velX;
            this.velY = velY;
        }

        private void move(int times) {
            posX = (posX + velX * times) % dimX;
            if (posX < 0) {
                posX += dimX;
            }
            posY = (posY + velY * times) % dimY;
            if (posY < 0) {
                posY += dimY;
            }
        }
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        if (lines.size() < 15) {
            return 0;
        }
        List<Robot> robots = lines.stream().map(this::lineToRobot).toList();
        char[][] map = getMap(robots);
        var timeElapsed = 0;
        while (!isSymmetric(map)) {
            timeElapsed++;
            robots.forEach(robot ->robot.move(1));
            map = getMap(robots);
        }
        print(map);
        return timeElapsed;
    }

    private boolean isSymmetric(char[][] map) {
        for (char[] row : map) {
            if (!rowIsSymmetric(row)) {
                return false;
            }
        }
        return true;
    }

    private boolean rowIsSymmetric(char[] row) {
        for (int i = 0; i < row.length / 2; i++) {
            if (row[i] != row[row.length - i - 1]) {
                return false;
            }
        }
        return true;
    }


    private char[][] getMap(List<Robot> robots) {
        char[][] map = new char[101][103];
        for (int i = 0; i < 101; i++) {
            for (int j = 0; j < 103; j++) {
                map[i][j] = ' ';
            }
        }
        robots.forEach(robot -> map[robot.posX][robot.posY] = '#');
        return map;
    }

    private void print(char[][] map) {
        for (int i = 0; i < 101; i++) {
            for (int j = 0; j < 103; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }

    @Override
    protected Number getFirstSolution(List<String> lines) {
        dimX = lines.size() == 12 ? 11 : 101;
        dimY = lines.size() == 12 ? 7 : 103;
        List<Robot> robots = lines.stream().map(this::lineToRobot).toList();
        robots.forEach(robot -> robot.move(100));
        return countQuadrant(robots, 0, 0)
                * countQuadrant(robots, 1, 0)
                * countQuadrant(robots, 0, 1)
                * countQuadrant(robots, 1, 1);
    }

    private long countQuadrant(List<Robot> robots, int xHalf, int yHalf) {
        return robots.stream()
                .filter(robot -> correctHalf(xHalf, dimX, robot.posX) &&  correctHalf(yHalf, dimY, robot.posY))
                .count();
    }
/*
    public static void main(String[] args) {
        var numbers = List.of(0, 1,2,3,4,5,6);
        numbers.forEach(n -> {
            System.out.println(n + ": " + correctHalf(0, 7, n));
        });
        System.out.println("---------");
        numbers.forEach(n -> {
            System.out.println(n + ": " + correctHalf(1, 7, n));
        });
    }
*/
    private static  boolean correctHalf(int half, int dim, int number) {
        if (number * 2 == dim - 1) {
            return false;
        }
        return half * (dim / 2) <= number && (half + 1) * (dim / 2) >= number;
    }

    private Robot lineToRobot(String line) {
        String[] halves = line.split(" ");
        String[] positions = halves[0].substring(2).split(",");
        String[] vels = halves[1].substring(2).split(",");
        return new Robot(
                Integer.parseInt(positions[0]),
                Integer.parseInt(positions[1]),
                Integer.parseInt(vels[0]),
                Integer.parseInt(vels[1]));
    }

    @Override
    public int getDay() {
        return 14;
    }
}
