package com.ksteindl.adventofcode.advent2024;

import java.util.*;

public class Day6 extends Puzzle2024 {

    public static void main(String[] args) {
        new Day6().printSolutions();
    }

    Character[][] map;
    Guard guard = null;

    static class Tile {
        int row, col;

        public Tile(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Tile tile = (Tile) o;
            return row == tile.row && col == tile.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    static class Guard extends Tile {
        int dir = 0;
        public Guard(int row, int col, int dir) {
            super(row, col);
            this.dir = dir;
        }

        void step() {
            switch (dir) {
                case 0 -> row--;
                case 1 -> col++;
                case 2 -> row++;
                case 3 -> col--;
            }
        }

        void back() {
            switch (dir) {
                case 0 -> row++;
                case 1 -> col--;
                case 2 -> row--;
                case 3 -> col++;
            }
        }

        boolean offTheMap(Character[][] map) {
            return col < 0 || row >= map.length || col >= map[0].length || row < 0;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Guard guard = (Guard) o;
            return dir == guard.dir;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), dir);
        }
    }

    Set<Guard> visited;
    Set<Tile> newObstacles;

    @Override
    protected Number getFirstSolution(List<String> lines) {
        initMap(lines);
        visited = new HashSet<>();
        newObstacles = new HashSet<>();
        while (moveToObstacle(guard));
        return visited.size();
    }

    private void initMap(List<String> lines) {
        map = new Character[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                map[i][j] = c;
                if (c == '^') {
                    guard = new Guard(i, j, 0);
                }
            }
        }
    }

    private boolean moveToObstacle(Guard guard) {
        while (map[guard.row][guard.col] != '#') {
            visited.add(new Guard(guard.row, guard.col, guard.dir));
            guard.step();
            if (guard.offTheMap(map)) {
                return false;
            }
            checkPotentialNewObstacle();
        }
        guard.back();
        guard.dir = (guard.dir + 1) % 4;
        return true;
    }

    private void checkPotentialNewObstacle() {
        guard.step();
        if (guard.offTheMap(map)) {
            guard.back();
            return;
        }
        if (map[guard.row][guard.col] != '#') {
            guard.back();
            return;
        }
        guard.back();
        int newDir = (guard.dir + 1) % 4;
        var newObs = new Guard(guard.row, guard.col, newDir);
        if (visited.contains(newObs)) {
            newObstacles.add(newObs);
        }
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        initMap(lines);
        visited = new HashSet<>();
        newObstacles = new HashSet<>();
        while (moveToObstacle(guard));
        return newObstacles.size();
    }

    @Override
    public int getDay() {
        return 6;
    }
}
