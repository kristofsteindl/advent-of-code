package com.ksteindl.adventofcode.advent2024;

import com.ksteindl.adventofcode.codingchallenge2021.task1.Coordinate;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day15 extends Puzzle2024 {

    public static void main(String[] args) {
        new Day15().printSolutions();
    }

    private char[][] map;
    private List<Coordinate> commands;
    private Coordinate robot;
    
    private void initMap(List<String> lines) {
        int i = 0;
        while (!lines.get(i).isEmpty()) {
            i++;
        }
        map = new char[i][lines.get(0).length()];
        commands = lines.subList(++i, lines.size()).stream()
                .flatMap(line -> line.chars()
                        .mapToObj(c -> (char) c)
                        .filter(c -> c != ' '))
                .map(Day15::createStep)
                .toList();

        robot = null;
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
    }

    private void initSecondMap(List<String> lines) {
        int i = 0;
        while (!lines.get(i).isEmpty()) {
            i++;
        }
        map = new char[i][lines.get(0).length() * 2];
        commands = lines.subList(++i, lines.size()).stream()
                .flatMap(line -> line.chars()
                        .mapToObj(c -> (char) c)
                        .filter(c -> c != ' '))
                .map(Day15::createStep)
                .toList();

        robot = null;
        for (i = 0; i < map.length; i++) {
            String line = lines.get(i);
            for (int l = 0; l < line.length(); l++) {
                char c = line.charAt(l);
                if (c == '@') {
                    robot = new Coordinate(i, 2 * l);
                    map[i][2 * l] = '.';
                    map[i][2 * l + 1] = '.';
                } else {
                    map[i][2 * l] = c == 'O' ? '[' : c;
                    map[i][2 * l + 1] = c == 'O' ? ']' : c;
                }
            }
        }
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        initSecondMap(lines);
        print(map, robot);
        for (Coordinate step : commands) {
            robot = executeDouble(step, map, robot);
            print(map, robot);
        }
        return calcBoxCoordinates(map);
    }

    private Coordinate executeDouble(Coordinate step, char[][] map, Coordinate robot) {
        Coordinate next = move(robot, step);
        if (map[next.row][next.column] == '#') {
            return robot;
        }
        if (map[next.row][next.column] == '.') {
            return next;
        }
        if (step.row == 0) {
            return executeSideways(next, step, map, robot);
        } else {
            try{ 
                executeVertical(robot, step);
                return next;
            } catch (BlockedException exception) {
                return robot;
            }
        }
        
    }

    private void executeVertical(Coordinate robot, Coordinate step) {
        int nextRow = robot.row + step.row;
        List<Map.Entry<Integer, Set<Integer>>> toBePushed = new ArrayList<>();
        Set<Integer> nextPushedColumns = getNextPushedColumns(nextRow, Set.of(robot.column));
        while (!nextPushedColumns.isEmpty()) {
            toBePushed.add(Map.entry(nextRow, nextPushedColumns));
            nextRow += step.row;
            nextPushedColumns = getNextPushedColumns(nextRow, nextPushedColumns);
        }
        for (int i = toBePushed.size() - 1; i >=0 ; i--) {
            Map.Entry<Integer, Set<Integer>> rowEntry = toBePushed.get(i);
            for (Integer column : rowEntry.getValue()) {
                map[rowEntry.getKey() + step.row][column] = map[rowEntry.getKey()][column];
                map[rowEntry.getKey()][column] = '.';
            }
        }
    }
    
    private Set<Integer> getNextPushedColumns(int nextRow, Set<Integer> prevPushedColumns) {
        Set<Integer> nextPushedColumns = new HashSet<>();
        for (Integer prevPushedColumn : prevPushedColumns) {
            char aboveBelow = map[nextRow][prevPushedColumn];
            if (aboveBelow == '#') {
                throw new BlockedException();
            } else if (aboveBelow == '[') {
                nextPushedColumns.add(prevPushedColumn);
                nextPushedColumns.add(prevPushedColumn + 1);
            } else if (aboveBelow == ']') {
                nextPushedColumns.add(prevPushedColumn);
                nextPushedColumns.add(prevPushedColumn - 1);
            }
        }
        return nextPushedColumns;
    }


        private Coordinate executeSideways(Coordinate next, Coordinate step, char[][] map, Coordinate robot) {
        Coordinate blockEnd = move(next, new Coordinate(0,0));
        int blockLength = 0;
        while (map[blockEnd.row][blockEnd.column] == ']' || map[blockEnd.row][blockEnd.column] == '[') {
            blockLength++;
            blockEnd = move(blockEnd, step);
        }
        if (map[blockEnd.row][blockEnd.column] == '.') {
            for (int i = 0; i < blockLength; i++) {
                map[next.row][blockEnd.column - i * step.column] =  map[next.row][blockEnd.column - (i + 1) * step.column];
            }
            return next;
        }
        return robot;
    }

    @Override
    protected Number getFirstSolution(List<String> lines) {
        initMap(lines);
        print(map, robot);
        for (Coordinate step : commands) {
            robot = execute(step, map, robot);
//            print(map, robot);
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
                if (map[i][j] == 'O' || map[i][j] == '[') {
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
    public int getDay() {
        return 15;
    }

    private static class Coordinate {
        int row;
        int column;

        public Coordinate(Coordinate coordinate) {
            this.row = coordinate.row;
            this.column = coordinate.column;
        }

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
    
    private static class BlockedException extends RuntimeException {}
}
