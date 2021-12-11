package com.ksteindl.adventofcode.advent2021.day11;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OctopusPredicter extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(OctopusPredicter.class);

    private static final int DAY = 11;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;
    List<List<Octopus>> octopuses;

    public OctopusPredicter(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
        init();
    }
    
    private void init() {
        List<String> lines = fileManager.parseLines(fileName);
        octopuses = lines.stream()
                .map(line -> IntStream.range(0, line.length()).mapToObj(i -> new Octopus(Integer.parseInt("" + line.charAt(i)))).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        OctopusPredicter day = new OctopusPredicter(false);
        logger.info(day.getFirstSolution());
        logger.info(day.getSecondSolution());
    }
    

    @Override
    public Number getFirstSolution() {
        return getSolution(result -> result.day > 100).flashes;
    }

    @Override
    public Number getSecondSolution() {
        return getSolution(result -> result.synced).day;
    }
    
    
    class Result {
        int day = 0;
        int flashes = 0;
        boolean synced = false;
    }
    
    public Result getSolution(Predicate<Result> winningCondition) {
        init();
        Result result = new Result();
        while (!winningCondition.test(result)) {
            
            for (int row = 0; row < octopuses.size(); row++) {
                for (int column = 0; column < octopuses.get(0).size(); column++) {
                    octopuses.get(row).get(column).number++;
                }
            }
            boolean changed = true;
            while (changed) {
                changed = false;
                for (int row = 0; row < octopuses.size(); row++) {
                    for (int column = 0; column < octopuses.get(0).size(); column++) {
                        if (flashIfCan(row, column)) {
                            result.flashes++;
                            changed = true;
                        }
                    }
                }
            }
            //System.out.println("Day: " + result.day);
            //prinOctopuses();
            result.day++;
            result.synced = synced();
        }
        return result;
    }


    
    private boolean synced() {
        int number = octopuses.get(0).get(0).number;
        for (int row = 0; row < octopuses.size(); row++) {
            for (int column = 0; column < octopuses.get(0).size(); column++) {
                if (number != octopuses.get(row).get(column).number) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean flashIfCan(int row, int column) {
        Octopus octopus = octopuses.get(row).get(column);
        if (octopus.number > 9) {
            octopus.number = 0;
            if (row > 0 && column > 0) {
                increaseAfterFlash(row - 1, column - 1);
            }
            if (row > 0) {
                increaseAfterFlash(row - 1, column);
            }
            if (row > 0 && column < octopuses.get(0).size() - 1) {
                increaseAfterFlash(row - 1, column + 1);
            }
            if (column > 0) {
                increaseAfterFlash(row, column - 1);
            }
            if (column < octopuses.get(0).size() - 1) {
                increaseAfterFlash(row, column + 1);
            }
            if (row < octopuses.size() - 1 && column > 0) {
                increaseAfterFlash(row + 1, column - 1);
            }
            if (row < octopuses.size() - 1) {
                increaseAfterFlash(row + 1, column);
            }
            if (row < octopuses.size() - 1 && column < octopuses.get(0).size() - 1) {
                increaseAfterFlash(row + 1, column + 1);
            } 
            return true;
        }
        return false;
    }
    
    
    private void increaseAfterFlash(int row, int column) {
        Octopus octopus = octopuses.get(row).get(column);
        if (octopus.number > 0) {
            octopus.number++;
        }
    }
    
    
    public static class Octopus {
        int number;
        public Octopus(int number) {
            this.number = number;
        }
    }
    
    private void prinOctopuses() {
        System.out.println("++++++++++++++++++++++++++++++");
        for (int i = 0; i < octopuses.size(); i++) {
            for (int j = 0; j < octopuses.get(0).size(); j++) {
                System.out.print(octopuses.get(i).get(j).number);
                if (j == octopuses.get(0).size() -1) {
                    System.out.println();
                }
            }
        }
        System.out.println("-------------------------------");
    }
    
    @Override
    public int getDay() {
        return DAY;
    }
    

}
