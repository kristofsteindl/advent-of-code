package com.ksteindl.adventofcode.codingchallenge2021.task2;

import com.ksteindl.adventofcode.codingchallenge2021.CoCha2021;
import com.ksteindl.adventofcode.codingchallenge2021.task1.Coordinate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day2 extends CoCha2021 {

    private static final Logger logger = LogManager.getLogger(Day2.class);

    private static final int DAY = 2;
    private static String INPUT_FILE = FOLDER_NAME + "cocha-input" + DAY + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "cocha-input" + DAY + "_test.txt";

    private final String fileName;

    public Day2(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;

    }

    @Override
    public Object getFirstSolution() {
        List<Integer> pinCode = new ArrayList<>();
        int x = 1;
        int y = 1;
        List<String> lines = fileManager.parseLines(fileName);
        for (String line :lines) {
            for (int i = 0; i < line.length(); i++) {
                char step = line.charAt(i);
                switch (step) {
                    case 'U':
                        if (y > 0) {
                            y--;
                        }
                        break;
                    case 'D':
                        if (y < 2) {
                            y++;
                        }
                        break;
                    case 'L':
                        if (x > 0) {
                            x--;
                        }
                        break;
                    case 'R':
                        if (x < 2) {
                            x++;
                        }
                        break;
                }
            }
            addNumber(pinCode, x, y);
        }
        StringBuilder builder = new StringBuilder();
        for (Integer number: pinCode) {
            builder.append(number);
        }
        return builder.toString();
    }

    private void addNumber(List<Integer> pinCode, Integer x, Integer y) {
        if (x == 0 && y == 0) {
            pinCode.add(1);
        }
        if (x == 1 && y == 0) {
            pinCode.add(2);
        }
        if (x == 2 && y == 0) {
            pinCode.add(3);
        }
        if (x == 0 && y == 1) {
            pinCode.add(4);
        }
        if (x == 1 && y == 1) {
            pinCode.add(5);
        }
        if (x == 2 && y == 1) {
            pinCode.add(6);
        }
        if (x == 0 && y == 2) {
            pinCode.add(7);
        }
        if (x == 1 && y == 2) {
            pinCode.add(8);
        }
        if (x == 2 && y == 2) {
            pinCode.add(9);
        }
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
