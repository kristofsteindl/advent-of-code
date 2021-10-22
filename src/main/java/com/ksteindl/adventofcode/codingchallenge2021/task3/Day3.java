package com.ksteindl.adventofcode.codingchallenge2021.task3;

import com.ksteindl.adventofcode.codingchallenge2021.CoCha2021;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day3 extends CoCha2021 {

    private static final Logger logger = LogManager.getLogger(Day3.class);

    private static final int DAY = 3;
    private static String INPUT_FILE = FOLDER_NAME + "cocha-input" + DAY + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "cocha-input" + DAY + "_test.txt";

    private final String fileName;

    public Day3(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;

    }

    @Override
    public Object getFirstSolution() {
        List<String> lines = fileManager.parseLines(fileName);
        int invalid = 0;
        for (String line : lines) {
            List<String> edges = Arrays.stream(line.split("\\s+")).filter(str -> !str.equals("")).collect(Collectors.toList());
            int a = Integer.parseInt(edges.get(0));
            int b = Integer.parseInt(edges.get(1));
            int c = Integer.parseInt(edges.get(2));

            if (a + b <= c || a + c <= b || c + b <= a) {
                logger.info(a + ", " + b + ", " + c );
                invalid++;
            }

        }

       return lines.size()-invalid;
    }


    @Override
    public Object getSecondSolution() {
        return -1;
    }

    @Override
    public int getDay() {
        return DAY;
    }
}
