package com.ksteindl.adventofcode.advent2021.day03;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DiagnisticDecoder extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(DiagnisticDecoder.class);

    private static final int DAY = 3;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;

    public DiagnisticDecoder(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    @Override
    public Number getSecondSolution() {
        List<String> lines = fileManager.parseLines(fileName);
        int i = 0;
        while (lines.size() > 1) {
            int ones = 0;
            int zeros = 0;
            for (int j = 0; j < lines.size(); j++) {
                if (lines.get(j).charAt(i) == '0') {
                    zeros++;
                } else {
                    ones++;
                }
            }
            List<String> oldLnes = lines;
            lines = new ArrayList<>();
            for (int j = 0; j < oldLnes.size(); j++) {
                String line = oldLnes.get(j);
                if (line.charAt(i) == (ones < zeros ? '0' : '1')) {
                    lines.add(line);
                }
            }
            i++;
        }
        int ox = 0;
        String line = lines.get(0);
        for (i = 0; i < line.length(); i++) {
            Integer number = Integer.parseInt(line.substring(i, i + 1));
            ox += number * Math.pow(2, line.length() - i -1);
        }

        lines = fileManager.parseLines(fileName);
        i = 0;
        while (lines.size() > 1) {
            int ones = 0;
            int zeros = 0;
            for (int j = 0; j < lines.size(); j++) {
                if (lines.get(j).charAt(i) == '0') {
                    zeros++;
                } else {
                    ones++;
                }
            }
            List<String> oldLnes = lines;
            lines = new ArrayList<>();
            for (int j = 0; j < oldLnes.size(); j++) {
                line = oldLnes.get(j);
                if (line.charAt(i) == (ones < zeros ? '1' : '0')) {
                    lines.add(line);
                }
            }
            i++;
        }
        int co2 = 0;
        line = lines.get(0);
        for (i = 0; i < line.length(); i++) {
            Integer number = Integer.parseInt(line.substring(i, i + 1));
            co2 += number * Math.pow(2, line.length() - i -1);
        }
        return ox * co2;
    }


    @Override
    public Number getFirstSolution() {
        List<String> lines = fileManager.parseLines(fileName);
        List<Counter> counters = new ArrayList<>();
        for (int i = 0; i < lines.get(0).length(); i++) {
            Counter counter = new Counter();
            for (int j = 0; j < lines.size(); j++) {
                if (lines.get(j).charAt(i) == '0') {
                    counter.zeros++;
                } else {
                    counter.ones++;
                }
            }
            counters.add(counter);
        }
        List<Integer> max = counters.stream().map(counter -> Math.max(counter.zeros, counter.ones)).collect(Collectors.toList());
        int gamma = 0;
        for (int i = 0; i < max.size(); i++) {
            gamma += (counters.get(i).ones > counters.get(i).zeros ? 1 : 0) * Math.pow(2, max.size() - i -1);
        }
        logger.info("gamma: " + gamma);
        int epsilon = (int)Math.pow(2, max.size()) - gamma -1;
        logger.info("epsilon: " + epsilon);
        return gamma * epsilon;
    }



    @Override
    public int getDay() {
        return DAY;
    }
    
    private static class Counter {
        int zeros = 0;
        int ones = 0;
    }

   


}
