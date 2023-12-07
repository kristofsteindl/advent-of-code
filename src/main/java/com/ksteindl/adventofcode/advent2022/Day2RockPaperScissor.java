package com.ksteindl.adventofcode.advent2022;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Day2RockPaperScissor extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(Day2RockPaperScissor.class);

    private static final int DAY = 2;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;

    public Day2RockPaperScissor(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    public static void main(String[] args) {
        Puzzle2021 puzzle = new Day2RockPaperScissor(false);
        System.out.println(puzzle.getFirstSolution());
        System.out.println(puzzle.getSecondSolution());
    }

    @Override
    public Number getSecondSolution() {
        List<String> lines = fileManager.parseLines(fileName);
        return lines.stream()
                .map(line -> line.split(" "))
                .map(this::calcRound2)
                .mapToInt(num -> num)
                .sum();
    }

    private int calcRound2(String[] round) {
        int roundResult = Integer.MIN_VALUE;
        switch (round[1]) {
            case "X":
                roundResult = 0;
                break;
            case "Y":
                roundResult = 3;
                break;
            case "Z":
                roundResult = 6;
                break;
            default: throw new RuntimeException();
        }
        int mine = getMine2(round[0], roundResult);
        return roundResult + mine;
    }
    
    private int getMine2(String opponent, int roundResult) {
        switch (opponent) {
            case "A":
                return roundResult == 3 ? 1 : roundResult == 6 ? 2 : 3;
            case "B":
                return roundResult == 3 ? 2 : roundResult == 6 ? 3 : 1;
            case "C":
                return roundResult == 3 ? 3 : roundResult == 6 ? 1 : 2;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Number getFirstSolution() {
        List<String> lines = fileManager.parseLines(fileName);
        return lines.stream()
                .map(line -> line.split(" "))
                .map(this::calcRound)
                .mapToInt(num -> num)
                .sum();
    }
    
    private int calcRound(String[] round) {
        int mine =  round[1].charAt(0) - 87;
        int totalScore = mine + calcWinner(mine, round[0]);
        return totalScore;
    }
    
    private int calcWinner(int mine, String sign) {
        switch (sign) {
            case "A":
                return mine == 1 ? 3 : mine == 2 ? 6 : 0;
            case "B":
                return mine == 2 ? 3 : mine == 3 ? 6 : 0;
            case "C":
                return mine == 3 ? 3 : mine == 1 ? 6 : 0;
        }
        throw new IllegalArgumentException();
    }
    

    @Override
    public int getDay() {
        return DAY;
    }

   


}
