package com.ksteindl.adventofcode.advent2023;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day2 extends Puzzle2023{

    public static void main(String[] args) {
        var puzzle = new Day2();
        puzzle.printSolutions();
    }

    static class Game {
        Integer id;
        List<int[]> reveals = new ArrayList<>();

        public Game(String line) {
            var withougGame = line.split("Game ")[1];
            var idAndReveleals = withougGame.split(":");
            id = Integer.valueOf(idAndReveleals[0]);
            var reveleals = idAndReveleals[1];
            for (String revealString : reveleals.split(";")) {
                var reveal = new int[3]; 
                for (String colorInfo : revealString.split(",")) {
                    if (colorInfo.endsWith("red")) {
                        reveal[0] = Integer.valueOf(colorInfo.split(" ")[1]);
                    }
                    if (colorInfo.endsWith("green")) {
                        reveal[1] = Integer.valueOf(colorInfo.split(" ")[1]);
                    }
                    if (colorInfo.endsWith("blue")) {
                        reveal[2] = Integer.valueOf(colorInfo.split(" ")[1]);
                    }
                }
                reveals.add(reveal);
            }
        }
        
        boolean isPossible() {
            return !reveals.stream().anyMatch(reveal -> reveal[0] > 12 || reveal[1] > 13|| reveal[2] > 14);
        }

        int mintPossibleSetPower() {
            int[] minPossibleSet = new int[3];
            for (int[] reveal : reveals) {
                for (int i = 0; i < 3; i++) {
                    if (reveal[i] > minPossibleSet[i]) {
                        minPossibleSet[i] = reveal[i];
                    }
                }
            }
            var min = minPossibleSet[0]  * minPossibleSet[1]* minPossibleSet[2];
            System.out.println(min);
            return min;
        }
    }
    
    @Override
    protected Number getFirstSolution(List<String> lines) {
        return lines.stream()
                .map(Game::new)
                .filter(Game::isPossible)
                .mapToInt(game -> game.id)
                .sum();
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        return lines.stream()
                .map(Game::new)
                .mapToInt(Game::mintPossibleSetPower)
                .sum();
    }

    @Override
    public int getDay() {
        return 2;
    }
}
