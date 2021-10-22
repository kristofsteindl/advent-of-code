package com.ksteindl.adventofcode.advent2020.day17;

import com.ksteindl.adventofcode.advent2020.Puzzle2020;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CubeInitiator extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(CubeInitiator.class);

    private static final int DAY = 17;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;
    private final ThreeDinitiator threeDinitiator;
    private final FourDinitiator fourDinitiator;

    public CubeInitiator(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
        List<String> lines = fileManager.parseLines(fileName);
        boolean[][] initialLevel = getInitialLevel(lines);
        this.threeDinitiator = new ThreeDinitiator(initialLevel);
        this.fourDinitiator = new FourDinitiator(initialLevel);
    }

    @Override
    public Number getFirstSolution() {
        return threeDinitiator.getActiveCubeCount();
    }

    @Override
    public Number getSecondSolution() {
        return fourDinitiator.getActiveCubeCount();
    }


    @Override
    public int getDay() {
        return DAY;
    }

    private static boolean[][] getInitialLevel(List<String> lines) {
        boolean[][] initialLevel = new boolean[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < lines.get(0).length(); j++) {
                initialLevel[i][j] = line.charAt(j) == '#';
            }
        }
        return initialLevel;
    }



}

    /*
    *
    * References
    *
    * */
