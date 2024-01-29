package com.ksteindl.adventofcode.advent2023;

import java.util.ArrayList;
import java.util.List;

public class Day13 extends Puzzle2023{

    public static void main(String[] args) {
        new Day13().printSolutions();
    }
    
    private static class Tile {
        
        List<List<Character>> rows = new ArrayList<>();

        Tile(List<String> lines) {
            for (String line : lines) {
                List<Character> row = new ArrayList<>();
                for (int j = 0; j < line.length(); j++) {
                    row.add(line.charAt(j));
                }
                rows.add(row);
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (List<Character> row : rows) {
                for (Character character : row) {
                    sb.append(character);
                }
                sb.append("\n");
            }
            return sb.toString();
        }
        
        long calc(boolean fixSmudge) {
           return calcVertical(fixSmudge) + calcHorizontal(fixSmudge) * 100; 
        }

        private long calcVertical(boolean fixSmudge) {
            for (int i = 1; i < rows.get(0).size(); i++) {
                List<Character> left = calcVerticalHalfHash(i, true);
                List<Character> right = calcVerticalHalfHash(i, false);
                if (fixSmudge) {
                    if (equalsIfAnyIsSmudged(left, right) || equalsIfAnyIsSmudged(right, left)) {
                        return i;
                    } 
                } else {
                    if (left.equals(right)) {
                        return i;
                    }
                }
            }
            return 0;
        }
        
        private boolean equalsIfAnyIsSmudged(List<Character> one, List<Character> other) {
            for (int i = 0; i < one.size(); i++) {
                var original = one.get(i);
                var opposite = original == '#' ? '.' : '#';
                one.set(i, opposite);
                if (one.equals(other)) {
                    return true;
                }
                one.set(i, original);
            }
            return false;
        }
        
        private long calcHorizontal(boolean fixSmudge) {
            for (int i = 1; i < rows.size(); i++) {
                List<Character> above = calcHorizontalHalfHash(i, true);
                List<Character> below = calcHorizontalHalfHash(i, false);
                if (fixSmudge) {
                    if (equalsIfAnyIsSmudged(above, below) || equalsIfAnyIsSmudged(below, above)) {
                        return i;
                    }
                } else {
                    if (above.equals(below)) {
                        return i;
                    }
                }
            }
            return 0;
        }

        private List<Character> calcHorizontalHalfHash(int mirrorRow, boolean above) {
            List<Character> hash = new ArrayList<>();
            int iterateRowTo;
            int j;
            if (above) {
                iterateRowTo = Math.min(mirrorRow, rows.size() - mirrorRow);
                j = 0;
            } else {
                iterateRowTo = Math.min(2 * mirrorRow, rows.size());
                j = mirrorRow;
            }
            for (; j < iterateRowTo; j++) {
                for (int i = 0; i < rows.get(0).size(); i++) {
                    hash.add(rows.get(above ? mirrorRow - j - 1 : j).get(i));
                }
            }
            return hash;
        }
        
        private List<Character> calcVerticalHalfHash(int mirrorColumn, boolean left) {
            List<Character> hash = new ArrayList<>();
            int iterateColumnTo;
            int j;
            if (left) {
                iterateColumnTo = Math.min(mirrorColumn, rows.get(0).size() - mirrorColumn);
                j = 0;
            } else {
                iterateColumnTo = Math.min(2 * mirrorColumn, rows.get(0).size());
                j = mirrorColumn;
            }
            for (; j < iterateColumnTo; j++) {
                for (int i = 0; i < rows.size(); i++) {
                    hash.add(rows.get(i).get(left ? mirrorColumn - j - 1 : j));
                }
            }
            return hash;
        }
    }


    @Override
    protected Number getSecondSolution(List<String> lines) {
        List<Tile> tiles = collectTiles(lines);
        return tiles.stream().mapToLong(tile -> tile.calc(true)).sum();
    }
    
    @Override
    protected Number getFirstSolution(List<String> lines) {
        List<Tile> tiles = collectTiles(lines);
        return tiles.stream().mapToLong(tile -> tile.calc(false)).sum();
    }
    
    private static List<Tile> collectTiles(List<String> lines) {
        List<Tile> tiles = new ArrayList<>();
        int startIndex = 0;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).isEmpty()) {
                tiles.add(new Tile(lines.subList(startIndex, i)));
                startIndex = i + 1;
            }
        }
        tiles.add(new Tile(lines.subList(startIndex, lines.size())));
        return tiles;
    }

    @Override
    public int getDay() {
        return 13;
    }
}
