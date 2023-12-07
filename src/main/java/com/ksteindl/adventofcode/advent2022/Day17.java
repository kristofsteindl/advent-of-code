package com.ksteindl.adventofcode.advent2022;

import java.util.ArrayList;
import java.util.List;

public class Day17 extends Puzzle2022 {
    /*
####

.#.
###
.#.

..#
..#
###

#
#
#
#

##
##
     */
    public static void main(String[] args) {
        new Day17().printSolutions();
    }
    
    @Override
    protected Number getFirstSolution(List<String> lines) {
        Cave cave = new Cave(lines.get(0));
        for (int i = 0; i < 2022; i++) {
            cave.startNewRound();
            cave.playRound();
            cave.transformTower();
            cave.calcHeight();
        }
        return cave.getHeight();
    }
    
    static class Cave {
        final String movements;
        int round = 0;
        int height = 0;
        List<Point[]> levels = new ArrayList<>();

        public Cave(String movements) {
            this.movements = movements;
        }

        void addLevel() {
            Point[] level = new Point[7];
            for (int k = 0; k < 7; k++) {
                level[k] = Point.SPACE;
            }
            levels.add(level);
        }

        void transformTower() {
            boolean foundSymbol = false;
            for (int row = levels.size() - 1; row >= 0; row--) {
                boolean foundSymbolInRow = false;
                for (int column = 0; column < 7; column++) {
                    if (levels.get(row)[column] == Point.ROCK) {
                        levels.get(row)[column] = Point.TOWER;
                        foundSymbol = true;
                        foundSymbolInRow = true;
                    }
                }
                if (foundSymbol && !foundSymbolInRow) {
                    break;
                }
            }
        }
        
        void calcHeight() {
            for (int row = levels.size() - 1; row >= 0; row--) {
                for (int column = 0; column < 7; column++) {
                    if (levels.get(row)[column] == Point.TOWER) {
                        height = row;
                        break;
                    }
                }
            }
        }
        
        void playRound() {
            boolean nextRound;
            do {
                push();
                nextRound = fall();
            } while (nextRound);
        }
        
        void push() {
            if (movements.charAt(round % movements.length()) == '>') {
                if (isShiftable(6)) {
                    boolean started = false;
                    for (int i = levels.size() - 1; i >= 0; i--) {
                        Point[] level = levels.get(i);
                        boolean symbolInRow = false;
                        for (int pos = 5; pos >= 0; pos--) {
                            if (level[pos] == Point.ROCK) {
                                started = true;
                                symbolInRow = true;
                                level[pos + 1] = level[pos];
                            }
                            if (level[pos + 1] == Point.ROCK && level[pos] == Point.SPACE) {
                                level[pos + 1] = Point.SPACE;
                            }
                        }
                        if (started && !symbolInRow) {
                            break;
                        }
                    }
                }
            } else {
                if (isShiftable(0)) {
                    boolean started = false;
                    for (int i = levels.size() - 1; i >= 0; i--) {
                        Point[] level = levels.get(i);
                        boolean symbolInRow = false;
                        for (int pos = 1; pos < 7; pos++) {
                            if (level[pos] == Point.ROCK) {
                                started = true;
                                symbolInRow = true;
                                level[pos - 1] = Point.ROCK;
                            }
                            if (level[pos - 1] == Point.ROCK && level[pos] == Point.SPACE) {
                                level[pos - 1] = Point.SPACE;
                            }
                        }
                        if (started && !symbolInRow) {
                            break;
                        }
                    }
                }
            }
        }
        
        int getSymbolStartRow() {
            int row = levels.size() - 1;
            while (row >= 0) {
                Point[] level = levels.get(row);
                for (int i = 0; i < 7; i++) {
                    if (level[i] == Point.ROCK) {
                        return row;
                    }
                }
                row--;
            }
            return -1;
        }
        
        boolean isShiftable(int column) {
            int row = getSymbolStartRow();
            boolean symbolFinished = true;
            do {
                Point[] level = levels.get(row);
                if (level[column] != Point.SPACE) {
                    return false;
                }
                if (column == 0) {
                    for (int pos = 1; pos <= 6; pos++) {
                        if (level[pos] == Point.ROCK) {
                            symbolFinished = false;
                            if (level[pos - 1] == Point.TOWER) {
                                return false;
                            }
                        }
                    }
                } else {
                    for (int pos = 6; pos >= 0; pos--) {
                        if (level[pos] == Point.ROCK) {
                            symbolFinished = false;
                            if (level[pos + 1] == Point.TOWER) {
                                return false;
                            }
                        }
                    }
                }
                row--;
            } while (!symbolFinished && row >= 0);
            return true;
        }
        
        boolean canFall() {
            for (int column = 0; column < 7; column++) {
                for (int row = levels.size() - 1; row >= 0; row--) {
                    if (levels.get(row)[column] == Point.ROCK) {
                        if (row == 0) {
                            return false;
                        }
                        if (levels.get(row - 1)[column] == Point.TOWER) {
                            return false;
                        }
                    } else if (levels.get(row)[column] == Point.TOWER) {
                        break;
                    }
                }
            }
            return true;
        }
        
        boolean fall() {
            if (canFall()) {
                for (int column = 0; column < 7; column++) {
                    for (int row = levels.size() - 1; row >= 0; row--) {
                        if (levels.get(row)[column] == Point.ROCK) {
                            levels.get(row)[column] = Point.SPACE;
                            while (row >= 0 && levels.get(row--)[column] == Point.ROCK);
                            levels.get(row)[column] = Point.ROCK;
                            
                            break;
                        }
                    }
                }
                return true;
            }
            return false;
        }
        
        void startNewRound() {
            while (levels.size() < height + 3) {
                addLevel();
            }
            round++;
            if (round % 5 == 1) { // HORIZONTAL
                levels.add(createRockLevel(2, 4));
            }
            if (round % 5 == 2) { //CROSS
                levels.add(createRockLevel(3, 1));
                levels.add(createRockLevel(2, 3));
                levels.add(createRockLevel(3, 1));
            }
            if (round % 5 == 3) { // L LETTER
                levels.add(createRockLevel(2, 3));
                levels.add(createRockLevel(4, 1));
                levels.add(createRockLevel(4, 1));
            }
            if (round % 5 == 4) { // VERTICAL
                for (int i = 0; i < 4; i++) {
                    levels.add(createRockLevel(2, 1));
                }
            }
            if (round % 5 == 0) { // SQUERE
                levels.add(createRockLevel(2, 2));
                levels.add(createRockLevel(2, 2));
            }
        }

        Point[] createRockLevel(int left, int rock) {
            Point[] level = new Point[7];
            for (int i = 0; i < left; i++) {
                level[i] = Point.SPACE;
            }
            for (int i = left; i < left + rock ; i++) {
                level[i] = Point.ROCK;
            }
            for (int i = left + rock; i < 7; i++) {
                level[i] = Point.SPACE;
            }
            return level;
        }
        
        int getHeight() {
            return height;
        }
    }
    
    enum Point {
        SPACE, TOWER, ROCK 
    }


    @Override
    protected Number getSecondSolution(List<String> lines) {
        return null;
    }

    @Override
    public int getDay() {
        return 17;
    }
}
