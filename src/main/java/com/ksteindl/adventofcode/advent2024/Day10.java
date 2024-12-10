package com.ksteindl.adventofcode.advent2024;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day10 extends Puzzle2024 {

    private Integer[][] map;

    public static void main(String[] args) {
        var day = new Day10();
        day.printSolutions();
    }

    static class Tile {
        public Tile(int row, int column, int value) {
            this.row = row;
            this.column = column;
            this.value = value;
        }

        int row;
        int column;
        int value;

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Tile tile = (Tile) o;
            return row == tile.row && column == tile.column && value == tile.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column, value);
        }
    }

    @Override
    protected Number getFirstSolution(List<String> lines) {
        initMap(lines);
        long sum = 0;
        for (int i = 0; i < map.length ; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 0) {
                    sum += calcTileScore(i,j);
                }
            }
        }
        return sum;
    }

    private long calcTileScore(int zeroRow, int zeroColumn) {
        List<Tile> pathTips = new ArrayList<>();
        List<Tile> newTips = new ArrayList<>();
        pathTips.add(new Tile(zeroRow, zeroColumn, 0));
        for (int i = 1; i < 10; i++) {
            for (int j = 0; j < pathTips.size(); j++) {
                Tile tile = pathTips.get(j);
                // UP
                if (tile.row > 0) {
                    if (map[tile.row - 1][tile.column] == i) {
                        newTips.add(new Tile(tile.row - 1, tile.column, i));
                    }
                }
                // DOWN
                if (tile.row < map.length - 1) {
                    if (map[tile.row + 1][tile.column] == i) {
                        newTips.add(new Tile(tile.row + 1, tile.column, i));
                    }
                }
                // LEFT
                if (tile.column > 0) {
                    if (map[tile.row][tile.column - 1] == i) {
                        newTips.add(new Tile(tile.row, tile.column - 1, i));
                    }
                }
                // RIGHT
                if (tile.column < map[0].length - 1) {
                    if (map[tile.row][tile.column + 1] == i) {
                        newTips.add(new Tile(tile.row, tile.column + 1, i));
                    }
                }
            }
            pathTips = newTips;
            newTips = new ArrayList<>();
        }
        return pathTips.stream().count();
    }

    private void initMap(List<String> lines) {
        map = new Integer[lines.size()][lines.get(0).length()];
        for (int i = 0; i <lines.size() ; i++) {
            String row =lines.get(i);
            for (int j = 0; j < row.length(); j++) {
                map[i][j] = Integer.parseInt(row.charAt(j)+"");
            }
        }
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        return null;
    }

    @Override
    public int getDay() {
        return 10;
    }
}
