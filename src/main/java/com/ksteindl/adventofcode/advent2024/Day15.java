package com.ksteindl.adventofcode.advent2024;

import org.checkerframework.checker.units.qual.C;

import java.util.List;
import java.util.stream.Collectors;

public class Day15 extends Puzzle2024 {

    public static void main(String[] args) {
        new Day15().printSolutions();
    }

    @Override
    protected Number getFirstSolution(List<String> lines) {
        int i = 0;
        while (!lines.get(i).isEmpty()) {
            i++;
        }
        char[][] map = new char[i][lines.get(0).length()];
        List<Coordinate> commands = lines.subList(++i, lines.size()).stream()
                .flatMap(line -> line.chars()
                        .mapToObj(c -> (char) c)
                .filter(c -> c != ' '))
                .map(Day15::createStep)
                .toList();

        Coordinate robot = null;
        for (i = 0; i < map.length; i++) {
            String line = lines.get(i);
            for (int l = 0; l < line.length(); l++) {
                char c = line.charAt(l);
                if (c == '@') {
                    robot = new Coordinate(i, l);
                } else {
                    map[i][l] = line.charAt(l);
                }
            }
        }
        print(map, robot);
        for (Coordinate step : commands) {
            robot = execute(step, map, robot);
            print(map, robot);
        }
        return calcBoxCoordinates(map);
    }

    private Coordinate execute(Coordinate step, char[][] map, Coordinate robot) {
        Coordinate next = move(robot, step);
        if (map[next.row][next.column] == '#') {
            return robot;
        }
        if (map[next.row][next.column] == '.') {
            return next;
        }
        Coordinate blockEnd = move(next, new Coordinate(0,0));
            while (map[blockEnd.row][blockEnd.column] == 'O') {
            blockEnd = move(blockEnd, step);
        }
        if (map[blockEnd.row][blockEnd.column] == '.') {
            map[blockEnd.row][blockEnd.column] = 'O';
            map[next.row][next.column] = '.';
            return next;
        }
        return robot;
    }

    private Coordinate move(Coordinate start, Coordinate command) {
        return new Coordinate(start.row + command.row, start.column + command.column);
    }

    private long calcBoxCoordinates(char[][] map) {
        int sum = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 'O') {
                    sum += (i * 100) + j;
                }
            }
        }
        return sum;
    }

    private void print(char[][] map, Coordinate robot) {
        map[robot.row][robot.column] = '@';
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
        map[robot.row][robot.column] = '.';
    }

    private static Coordinate createStep(Character c) {
        return switch (c) {
            case '^' -> new Coordinate(-1, 0);
            case 'v' -> new Coordinate(1, 0);
            case '>' -> new Coordinate(0, 1);
            case '<' -> new Coordinate(0, -1);
            default -> throw new RuntimeException("'"  + c + "'");
        };
    }



    @Override
    protected Number getSecondSolution(List<String> lines) {
        return null;
    }

    @Override
    public int getDay() {
        return 15;
    }

    private static class Coordinate {
        int row;
        int column;

        public Coordinate(int x, int y) {
            this.row = x;
            this.column = y;
        }

        @Override
        public String toString() {
            if (row == -1 && column == 0) {
                return "^";
            }
            if (row == 1 && column == 0) {
                return "v";
            }
            if (row == 0 && column == -1) {
                return "<";
            }
            if (row == 0 && column == 1) {
                return ">";
            }
            return "Coordinate{" +
                    "row=" + row +
                    ", column=" + column +
                    '}';
        }
    }
}
