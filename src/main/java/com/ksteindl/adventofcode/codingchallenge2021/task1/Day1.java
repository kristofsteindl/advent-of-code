package com.ksteindl.adventofcode.codingchallenge2021.task1;

import com.ksteindl.adventofcode.codingchallenge2021.CoCha2021;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day1 extends CoCha2021 {

    private static final Logger logger = LogManager.getLogger(Day1.class);

    private static final int DAY = 1;
    private static String INPUT_FILE = FOLDER_NAME + "cocha-input" + DAY + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "cocha-input" + DAY + "_test.txt";

    private final String fileName;

    public Day1(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;

    }

    @Override
    public Object getFirstSolution() {
//        List<String> lines = fileManager.parseLines(fileName);
//        String[] elements = lines.get(0).split(", ");
//        int x = 0;
//        int y = 0;
//        int cardinal = 0;
//        for (int i = 0; i < elements.length; i++) {
//            logger.info("x: " + x);
//            logger.info("y: " + y);
//            logger.info("cardinal: " + cardinal);
//            logger.info("i: " + i);
//            logger.info("element: " + elements[i]);
//            char direction = elements[i].charAt(0);
//            if (cardinal == 0) {
//                if (direction =='R') {
//                    cardinal = 1;
//                } else {
//                    cardinal = 3;
//                }
//
//            } else if (cardinal == 1) {
//                if (direction =='R') {
//                    cardinal = 2;
//                } else {
//                    cardinal = 0;
//                }
//            } else if(cardinal == 2) {
//                if (direction =='R') {
//                    cardinal = 3;
//                } else {
//                    cardinal = 1;
//                }
//            } else {
//                if (direction =='R') {
//                    cardinal = 0;
//                } else {
//                    cardinal = 2;
//                }
//            }
//            Integer amount = Integer.parseInt(elements[i].substring(1));
//            switch (cardinal) {
//                case 0:
//                    y += amount;
//                    break;
//                case 1:
//                    x += amount;
//                    break;
//                case 2:
//                    y -= amount;
//                    break;
//                case 3:
//                    x -= amount;
//                    break;
//            }
//            logger.info("amount: " + amount);
//        }
//        logger.info("x: " + x);
//        logger.info("y: " + y);
//        logger.info("cardinal: " + cardinal);
        //return x + y;
        return -1;
        //return Math.abs(x) + Math.abs(y);
    }

    @Override
    public Object getSecondSolution() {
        List<String> lines = fileManager.parseLines(fileName);
        String[] elements = lines.get(0).split(", ");
        int x = 0;
        int y = 0;
        int cardinal = 0;
        Set<Coordinate> coordinates = new HashSet<>();
        for (int i = 0; i < elements.length; i++) {
            logger.info("x: " + x);
            logger.info("y: " + y);
            logger.info("cardinal: " + cardinal);
            logger.info("i: " + i);
            logger.info("element: " + elements[i]);
            char direction = elements[i].charAt(0);
            if (cardinal == 0) {
                if (direction =='R') {
                    cardinal = 1;
                } else {
                    cardinal = 3;
                }

            } else if (cardinal == 1) {
                if (direction =='R') {
                    cardinal = 2;
                } else {
                    cardinal = 0;
                }
            } else if(cardinal == 2) {
                if (direction =='R') {
                    cardinal = 3;
                } else {
                    cardinal = 1;
                }
            } else {
                if (direction =='R') {
                    cardinal = 0;
                } else {
                    cardinal = 2;
                }
            }
            Integer amount = Integer.parseInt(elements[i].substring(1));
            switch (cardinal) {
                case 0:
                    for (int j = 1; j < amount + 1; j++) {
                        y += 1;
                        if (!coordinates.add(new Coordinate(x, y))) {
                            logger.info("!!! " + new Coordinate(x, y));
                        }
                    }

                    break;
                case 1:
                    for (int j = 1; j < amount + 1; j++) {
                        x += 1;
                        if (!coordinates.add(new Coordinate(x, y))) {
                            logger.info("!!! " + new Coordinate(x, y));
                        }
                    }

                    break;
                case 2:
                    for (int j = 1; j < amount + 1; j++) {
                        y -= 1;
                        if (!coordinates.add(new Coordinate(x, y))) {
                            logger.info("!!! " + new Coordinate(x, y));
                        }
                    }
                    break;
                case 3:
                    for (int j = 1; j < amount + 1; j++) {
                        x -= 1;
                        if (!coordinates.add(new Coordinate(x, y))) {
                            logger.info("!!! " + new Coordinate(x, y));
                        }
                    }
                    break;
            }
            logger.info("amount: " + amount);
        }
        logger.info("x: " + x);
        logger.info("y: " + y);
        logger.info("cardinal: " + cardinal);
        //return x + y;
        return Math.abs(x) + Math.abs(y);
    }

    @Override
    public int getDay() {
        return DAY;
    }
}
