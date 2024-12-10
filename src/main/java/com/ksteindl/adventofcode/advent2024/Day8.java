package com.ksteindl.adventofcode.advent2024;

import java.util.*;
import java.util.stream.Stream;

public class Day8 extends Puzzle2024 {

    static class Tile {
        public Tile(int row, int column) {
            this.row = row;
            this.column = column;
        }

        int row;
        int column;

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Tile tile = (Tile) o;
            return row == tile.row && column == tile.column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }
    }

    public static void main(String[] args) {
        new Day8().printSolutions();
    }

    Map<Character, List<Tile>> map;

    private void initMap(List<String> lines) {
        map = new HashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (c != '.') {
                    map.merge(c, List.of(new Tile(i, j)),
                            (prev, next) -> Stream.concat(prev.stream(), next.stream()).toList());
                }
            }
        }
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (c != '.') {
                    map.merge(c, List.of(new Tile(i, j)),
                            (prev, next) -> Stream.concat(prev.stream(), next.stream()).toList());
                }
            }
        }
    }

    protected Number getFirstSolution(List<String> lines) {
        initMap(lines);
        Set<Tile> antinodes = new HashSet<>();
        for (Map.Entry<Character, List<Tile>> entry : map.entrySet()) {
            List<Tile> antennas = entry.getValue();
            for (int i = 0; i < antennas.size(); i++) {
                Tile antenna = antennas.get(i);
                antennas.stream()
                        .filter(a -> !a.equals(antenna))
                        .forEach(a -> {
                            int aRow = antenna.row - (a.row - antenna.row);
                            int aColumn = antenna.column - (a.column - antenna.column);
                            if (aRow >= 0 && aColumn >= 0 && aRow < lines.size() && aColumn < lines.get(0).length()) {
                                antinodes.add(new Tile(aRow, aColumn));
                            }
                        });
            }
        }
        return antinodes.size();
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        initMap(lines);
        Set<Tile> antinodes = new HashSet<>();
        for (Map.Entry<Character, List<Tile>> entry : map.entrySet()) {
            List<Tile> antennas = entry.getValue();
            antinodes.addAll(antennas);
            for (int i = 0; i < antennas.size(); i++) {
                Tile antenna = antennas.get(i);
                antennas.stream()
                        .filter(a -> !a.equals(antenna))
                        .forEach(a -> {
                            int rowIncr = a.row - antenna.row;
                            int columnIncr = a.column - antenna.column;
                            int aRow = antenna.row - rowIncr;
                            int aColumn = antenna.column - columnIncr;
                            while (aRow >= 0 && aColumn >= 0 && aRow < lines.size() && aColumn < lines.get(0).length()) {
                                antinodes.add(new Tile(aRow, aColumn));
                                aRow -= rowIncr;
                                aColumn -= columnIncr;
                            }
                        });
            }
        }

        return antinodes.size();
    }


    @Override
    public int getDay() {
        return 8;
    }
}
