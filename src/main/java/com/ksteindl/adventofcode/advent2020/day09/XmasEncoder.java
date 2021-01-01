package com.ksteindl.adventofcode.advent2020.day09;

import com.ksteindl.adventofcode.Puzzle2020;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class XmasEncoder extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(XmasEncoder.class);

    private static final int DAY = 9;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;

    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private static final int PREAMBLE_SIZE_TEST = 5;
    private static final int PREAMBLE_SIZE = 25;

    private final List<Long> numbers;
    private final int preambleSize;
    private final String fileName;
    private Long wrongNumber;

    public XmasEncoder(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
        this.numbers = fileManager.parseLines(fileName).stream().mapToLong(line -> Long.parseLong(line)).boxed().collect(Collectors.toList());;
        this.preambleSize = isTest ? PREAMBLE_SIZE_TEST : PREAMBLE_SIZE;
    }


    @Override
    public Number getFirstSolution() {
        return getFirstWrongNumber();
    }

    @Override
    public Number getSecondSolution() {
        return getEncryptionWeaknessRangeAddition();
    }

    @Override
    public int getDay() {
        return DAY;
    }

    private long getEncryptionWeaknessRangeAddition() {
        if (wrongNumber == null) {
            calculateFirstWrongNumber();
        }
        long sum = -1l;
        int rangeStart = 0;
        int rangeEnd = 0;
        List<Long> range = null;
        logger.debug(wrongNumber);
        while (sum != wrongNumber) {
            rangeEnd = rangeStart + 2;
            sum = 0l;
            while (sum < wrongNumber) {
                range = numbers.subList(rangeStart, rangeEnd);
                sum = range.stream().mapToLong(i -> i).sum();
                logger.debug("rangeStart: " + rangeStart + ", rangeEnd: " + rangeEnd + ", sum: " + sum);
                rangeEnd++;
            }
            rangeStart++;
        }
        rangeStart--;
        rangeEnd--;
        Long num = range.stream().mapToLong(i -> i).max().getAsLong() + range.stream().mapToLong(i -> i).min().getAsLong();
        return num;
    }

    private void calculateFirstWrongNumber() {
        long calcWrongNumber = -1;
        int index  = preambleSize;
        while (index < numbers.size() && calcWrongNumber == -1) {
            Long number = numbers.get(index);
            if (isWrong(number, numbers.subList(index - preambleSize, index))) {
                calcWrongNumber = numbers.get(index);
            }
            index ++;
        }
        wrongNumber = calcWrongNumber;
    }

    private long getFirstWrongNumber() {
        if (wrongNumber == null) {
            calculateFirstWrongNumber();
        }
        return  wrongNumber;
    }

    private boolean isWrong(Long number, List<Long> preambes) {
        Set<Long> allPossibleSum = getAllPossibleSum(preambes);
        return allPossibleSum.contains(number) ? false : true;
    }

    private Set<Long> getAllPossibleSum(final List<Long> preambes) {
        Set<Long> sums = new HashSet<>();
        for (int i = 0; i < preambes.size() - 1; i++) {
            Long n1 = preambes.get(i);
            for (int j = i + 1; j < preambes.size(); j++) {
                Long n2 = preambes.get(j);
                if (!n1.equals(n2)) {
                    Long sum = n1 + n2;
                    logger.trace(sum);
                    sums.add(sum);
                }
            }
        }
        return sums;
    }

    /*
    *
    * References
    * https://www.geeksforgeeks.org/arraylist-sublist-method-in-java-with-examples/
    *
    * */
}
