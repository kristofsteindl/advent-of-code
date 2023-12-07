package com.ksteindl.adventofcode.advent2022;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day1CaloryCounter extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(Day1CaloryCounter.class);

    private static final int DAY = 1;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;

    public Day1CaloryCounter(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    public static void main(String[] args) {
        Puzzle2021 test = new Day1CaloryCounter(false);
        System.out.println(test.getFirstSolution());
        System.out.println(test.getSecondSolution());
    }

    @Override
    public Number getFirstSolution() {
        return getElfCals().stream().mapToInt(i -> i).max().getAsInt();
    }

    @Override
    public Number getSecondSolution() {
        return getElfCals().stream().sorted(Collections.reverseOrder()).mapToInt(i -> i).limit(3).sum();
    }
    
    private List<Integer> getElfCals() {
        List<String> lines = fileManager.parseLines(fileName);
        List<Integer> elfCals = new ArrayList<>();
        int elfCal = 0;
        for (int i = 0; i < lines.size() ; i++) {
            String line = lines.get(i);
            if (line.equals("")) {
                elfCals.add(elfCal);
                elfCal = 0;
            } else {
                elfCal += Integer.parseInt(line);
            }
        }
        elfCals.add(elfCal);
        return elfCals;
    }
    

    @Override
    public int getDay() {
        return DAY;
    }

   


}
