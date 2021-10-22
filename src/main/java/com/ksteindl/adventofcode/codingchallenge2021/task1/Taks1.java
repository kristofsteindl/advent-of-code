package com.ksteindl.adventofcode.codingchallenge2021.task1;

import com.ksteindl.adventofcode.codingchallenge2021.CoCha2021;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Taks1 extends CoCha2021 {

    private static final Logger logger = LogManager.getLogger(Taks1.class);

    private static final int TASK = 1;
    private static String INPUT_FILE = FOLDER_NAME + "cocha-input" + TASK + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "cocha-input" + TASK + "_test.txt";

    private final String fileName;

    public Taks1(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;

    }

    @Override
    public Object getFirstSolution() {
        List<String> lines = fileManager.parseLines(fileName);
        lines.forEach(System.out::println);
        return -1;
    }

    @Override
    public Object getSecondSolution() {
        return -1;
    }

    @Override
    public int getDay() {
        return TASK;
    }
}
