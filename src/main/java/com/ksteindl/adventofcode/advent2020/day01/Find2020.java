package com.ksteindl.adventofcode.advent2020.day01;

import com.ksteindl.adventofcode.Puzzle2020;
import org.apache.logging.log4j.*;

import java.util.List;
import java.util.stream.Collectors;

public class Find2020 extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(Find2020.class);

    private static final int DAY = 1;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;
    private static final int MAGIC_NUMBER = 2020;

    public Find2020(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    @Override
    public Number getFirstSolution() {
        return getTheTwoMagicNumberProduct();
    }

    @Override
    public Number getSecondSolution() {
        return getTheThreeMagicNumberProduct();
    }

    @Override
    public int getDay() {
        return DAY;
    }

    private int getTheTwoMagicNumberProduct() {
        List<Integer> numbers = getMagicNumberList(fileName);
        for (int i = 0; i < numbers.size() - 1; i++) {
            for (int j = i + 1; j < numbers.size(); j++) {
                if (numbers.get(i) + numbers.get(j) == MAGIC_NUMBER) {
                    logger.debug("number1: " + numbers.get(i));
                    logger.debug("number2: " + numbers.get(j));
                    return numbers.get(i) * numbers.get(j);
                }
            }
        }
        return -1;
    }

    private int getTheThreeMagicNumberProduct() {
        List<Integer> numbers = getMagicNumberList(fileName);
        for (int i = 0; i < numbers.size() - 2; i++) {
            for (int j = i + 1; j < numbers.size() -1; j++) {
                for (int k = j + 1; k < numbers.size(); k++) {
                    if (numbers.get(i) + numbers.get(j) + numbers.get(k)== MAGIC_NUMBER) {
                        logger.debug("number1: " + numbers.get(i));
                        logger.debug("number2: " + numbers.get(j));
                        logger.debug("number3: " + numbers.get(k));
                        return numbers.get(i) * numbers.get(j) * numbers.get(k);
                    }
                }
            }
        }
        return -1;
    }

    private List<Integer> getMagicNumberList(String fileName) {
        List<String> lines = fileManager.parseLines(fileName);
        List<Integer> numbers = lines.stream().map(line -> Integer.parseInt(line.trim())).collect(Collectors.toList());
        return numbers;

    }


}
