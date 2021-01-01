package com.ksteindl.adventofcode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Puzzle2020Template extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(Puzzle2020Template.class);

    private static final int DAY = 0;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;

    public Puzzle2020Template(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }


    @Override
    public Number getFirstSolution() {
        return -getDay();
    }

    @Override
    public Number getSecondSolution() {
        return -getDay();
    }

    @Override
    public int getDay() {
        return DAY;
    }

    /*
    *
    * References
    *
    * */
}
