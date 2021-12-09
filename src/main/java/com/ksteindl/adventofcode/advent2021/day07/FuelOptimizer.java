package com.ksteindl.adventofcode.advent2021.day07;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FuelOptimizer extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(FuelOptimizer.class);

    private static final int DAY = 7;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;
    Map<Integer, Long> consTable = new HashMap<>();

    public FuelOptimizer(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    public static void main(String[] args) {
        FuelOptimizer day = new FuelOptimizer(false);
        logger.info(day.getFirstSolution());
        logger.info(day.getSecondSolution());
    }

    @Override
    public Number getSecondSolution() {
        List<Integer> numbers = Arrays.stream(fileManager.parseLines(fileName).get(0).split(",")).map(string -> Integer.parseInt(string)).collect(Collectors.toList());
        int max = numbers.stream().mapToInt(i->i).max().getAsInt();
        initConsTable(max);
        long bestSumDiff = Long.MAX_VALUE;
        for (int i = 0; i < max; i++) {
            Integer num = Integer.valueOf(i);
            long sumDiff = numbers.stream().mapToLong(number -> consTable.get(Math.abs(num - number))).sum();
            if (sumDiff < bestSumDiff) {
                bestSumDiff = sumDiff;
            }
        }
        return bestSumDiff;
    }

    @Override
    public Number getFirstSolution() {
        List<Integer> numbers = Arrays.stream(fileManager.parseLines(fileName).get(0).split(",")).map(string -> Integer.parseInt(string)).collect(Collectors.toList());
        int max =-1;
        for (int i = 0; i < numbers.size(); i++) {
            if (max < numbers.get(i)) {
                max = numbers.get(i);
            }
        }
        long bestSumDiff = Long.MAX_VALUE;
        for (int i = 0; i < max; i++) {
            Long num = Long.valueOf(i);
            long sumDiff = numbers.stream().mapToLong(number -> Math.abs(num - number)).sum();
            if (sumDiff < bestSumDiff) {
                bestSumDiff = sumDiff;
            }
        }
        return bestSumDiff;
    }
    
    
    private void initConsTable(int max) {
        consTable.put(0, 0L);
        for (int i = 1; i < max + 1; i++) {
            consTable.put(i, consTable.get(i - 1) + i);
        }
    }
    

    @Override
    public int getDay() {
        return DAY;
    }

   


}
