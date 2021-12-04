package com.ksteindl.adventofcode.advent2021;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Template2021 extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(Template2021.class);

    private static final int DAY = -1;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;

    public Template2021(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    @Override
    public Number getFirstSolution() {
        return getDay();
    }

    @Override
    public Number getSecondSolution() {
        return getDay();
    }
    

    @Override
    public int getDay() {
        return DAY;
    }

   


}
