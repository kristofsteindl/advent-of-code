package com.ksteindl.adventofcode.advent2024;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day14 extends Puzzle2024 {

    private static int dimX;
    private static int dimY;
    private static int timeElapsed = 0;

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

        @Override
        public String toString() {
            return "Robot{" +
                    "posY=" + posY +
                    ", posX=" + posX +
                    '}';
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
        while (!isSymmetric(robots)) {
            System.out.println(timeElapsed++);
            robots.forEach(robot -> robot.move(1));
        }
        
        return timeElapsed;
    }
    
    private boolean isSymmetric(List<Robot> robots) {
        Map<Integer, List<Robot>> mappedByRow = robots.stream().collect(Collectors.groupingBy(r -> r.posY));
        for (Integer row : mappedByRow.keySet()) {
            List<Robot> robotsInRow = mappedByRow.get(row);
            robotsInRow.sort(Comparator.comparing(r -> r.posX));
            int maxNextToEachOther = 0;
            int currentNextToEachOther = 0;
            for (int i = 1; i < robotsInRow.size(); i++) {
                if (robotsInRow.get(i - 1).posX + 1 == robotsInRow.get(i).posX) {
                    currentNextToEachOther++;
                } else {
                    if (currentNextToEachOther > maxNextToEachOther) {
                        maxNextToEachOther = currentNextToEachOther;
                    }
                    currentNextToEachOther = 0;
                }
            }
            if (maxNextToEachOther > 10) {
                return true;
            }
        }
        return false;
    }
    
    private boolean altIsSymmetric(List<Robot> robots) {
        int symmetricalIndex = 0;
        while (!indexIsSymmetrical(symmetricalIndex, mappedByRow) && symmetricalIndex < 101) {
            symmetricalIndex++;
            System.out.println(symmetricalIndex);
        }
        return symmetricalIndex > 101;
    }
    
    private boolean indexIsSymmetrical(int symmetryIndex, Map<Integer, List<Robot>> mappedByRow) {
        for (Integer row : mappedByRow.keySet()) {
            List<Robot> robotsInRow = mappedByRow.get(row);
            robotsInRow.sort(Comparator.comparing(r -> r.posX));
            for (int i = 0; i < robotsInRow.size() / 2; i++) {
                int leftDistance = symmetryIndex - robotsInRow.get(i).posX;
                int rightDistance = robotsInRow.get(robotsInRow.size() - i - 1).posX - symmetryIndex;
                if (leftDistance < 0 || rightDistance < 0 || leftDistance != rightDistance) {
                    return false;
                }
            }
        }
        return true;
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
                .filter(robot -> correctHalf(xHalf, dimX, robot.posX) && correctHalf(yHalf, dimY, robot.posY))
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
    private static boolean correctHalf(int half, int dim, int number) {
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
