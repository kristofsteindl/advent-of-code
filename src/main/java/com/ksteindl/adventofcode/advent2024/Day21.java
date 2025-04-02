package com.ksteindl.adventofcode.advent2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day21 extends Puzzle2024 {

//            +---+---+---+
//            | 7 | 8 | 9 |
//            +---+---+---+
//            | 4 | 5 | 6 |
//            +---+---+---+
//            | 1 | 2 | 3 |
//            +---+---+---+
//                | 0 | A |
//                +---+---+
//
//                +---+---+
//                | ^ | A |
//            +---+---+---+
//            | < | v | > |
//            +---+---+---+

    private static class Tile {
        public Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int x;
        int y;
    }

    private static Map<Character, Tile> numKeyPad;

    static {
        numKeyPad = new HashMap<>();
        numKeyPad.put('0', new Tile(0, 1));
        numKeyPad.put('A', new Tile(0, 2));
        numKeyPad.put('1', new Tile(1, 0));
        numKeyPad.put('2', new Tile(1, 1));
        numKeyPad.put('3', new Tile(1, 3));
        numKeyPad.put('4', new Tile(2, 0));
        numKeyPad.put('5', new Tile(2, 1));
        numKeyPad.put('6', new Tile(2, 3));
        numKeyPad.put('7', new Tile(3, 0));
        numKeyPad.put('8', new Tile(3, 1));
        numKeyPad.put('9', new Tile(3, 3));
    }

    private int countComplexity(String line) {
        List<List<Character>> possibleRoutes = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            Character number = line.charAt(i);
            
        }
        return 0;
    }

    @Override
    protected Number getFirstSolution(List<String> lines) {
        return lines.stream().mapToInt(this::countComplexity).sum();
    }


    @Override
    protected Number getSecondSolution(List<String> lines) {
        return null;
    }

    @Override
    public int getDay() {
        return 21;
    }
}
