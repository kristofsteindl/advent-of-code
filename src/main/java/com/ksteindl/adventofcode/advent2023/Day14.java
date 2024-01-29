package com.ksteindl.adventofcode.advent2023;

import java.util.ArrayList;
import java.util.List;

public class Day14 extends Puzzle2023{

    public static void main(String[] args) {
        new Day14().printSolutions();
    }

    @Override
    protected Number getFirstSolution(List<String> lines) {
        Platform platform = new Platform(lines);
        platform.tiltNorth();
        return platform.calcNorthLoad();
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        Platform platform = new Platform(lines);
        for (int i = 0; i < 1000; i++) {
            platform.tiltNorth();
            platform.tiltWest();
            platform.tiltSouth();
            platform.tiltEast();
            System.out.println(i);
            System.out.println( platform.calcNorthLoad());
        }
        
        return platform.calcNorthLoad();
    }
    
    private static class Platform {
        List<List<Character>> rows;
        
        public Platform(List<String> lines) {
            rows = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                List<Character> row = new ArrayList<>();
                for (int j = 0; j < line.length(); j++) {
                    row.add(line.charAt(j));
                }
                rows.add(row);
            }
        }
        
        void tiltNorth() {
            for (int columnIndex = 0; columnIndex < rows.get(0).size(); columnIndex++) {
                for (int rowIndex = 1; rowIndex < rows.size(); rowIndex++) {
                    Character rock = rows.get(rowIndex).get(columnIndex);
                    if (rock == 'O') {
                        int toFall = rowIndex - 1;
                        while (toFall >= 0 && rows.get(toFall).get(columnIndex) == '.') {
                            toFall--;
                        }
                        toFall++;
                        if (rows.get(toFall).get(columnIndex) == '.') {
                            rows.get(toFall).set(columnIndex, 'O');
                            rows.get(rowIndex).set(columnIndex, '.');
                        }
                    }
                }
            }
        }


        void tiltWest() {
            for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
                for (int columnIndex = 1; columnIndex < rows.get(0).size(); columnIndex++) {
                    Character rock = rows.get(rowIndex).get(columnIndex);
                    if (rock == 'O') {
                        int toFall = columnIndex - 1;
                        while (toFall >= 0 && rows.get(rowIndex).get(toFall) == '.') {
                            toFall--;
                        }
                        toFall++;
                        if (rows.get(rowIndex).get(toFall) == '.') {
                            rows.get(rowIndex).set(toFall, 'O');
                            rows.get(rowIndex).set(columnIndex, '.');
                        }
                    }
                }
            }
        }

        void tiltEast() {
            for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
                for (int columnIndex = rows.get(0).size() - 2; columnIndex >= 0; columnIndex--) {
                    Character rock = rows.get(rowIndex).get(columnIndex);
                    if (rock == 'O') {
                        int toFall = columnIndex + 1;
                        while (toFall < rows.get(0).size() && rows.get(rowIndex).get(toFall) == '.') {
                            toFall++;
                        }
                        toFall--;
                        if (rows.get(rowIndex).get(toFall) == '.') {
                            rows.get(rowIndex).set(toFall, 'O');
                            rows.get(rowIndex).set(columnIndex, '.');
                        }
                    }
                }
            }
        }


        void tiltSouth() {
            for (int columnIndex = 0; columnIndex < rows.get(0).size(); columnIndex++) {
                for (int rowIndex = rows.size() - 2; rowIndex >= 0; rowIndex--) {
                    Character rock = rows.get(rowIndex).get(columnIndex);
                    if (rock == 'O') {
                        int toFall = rowIndex + 1;
                        while (toFall < rows.size() && rows.get(toFall).get(columnIndex) == '.') {
                            toFall++;
                        }
                        toFall--;
                        if (rows.get(toFall).get(columnIndex) == '.') {
                            rows.get(toFall).set(columnIndex, 'O');
                            rows.get(rowIndex).set(columnIndex, '.');
                        }
                    }
                }
            }
        }
        
        
        long calcNorthLoad() {
            int sum = 0;
            for (int i = 0; i < rows.size(); i++) {
                List<Character> row = rows.get(i);
                int weight = rows.size() - i;
                for (int j = 0; j < row.size(); j++) {
                    if (row.get(j) == 'O') {
                        sum += weight;
                    }
                }
            }
            return sum;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < rows.size(); i++) {
                List<Character> line = rows.get(i);
                List<Character> row = new ArrayList<>();
                for (int j = 0; j < line.size(); j++) {
                    builder.append(line.get(j));
                }
                builder.append("\n");
            }
            return builder.toString();
        }
    }
    

    @Override
    public int getDay() {
        return 14;
    }
}
