package com.ksteindl.adventofcode.advent2021.day02;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class PositionCalculator extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(PositionCalculator.class);

    private static final int DAY = 2;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;

    public PositionCalculator(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    @Override
    public Number getSecondSolution() {
        return getSolution(new PositionPartTwo());
    }

    @Override
    public Number getFirstSolution() {
        return getSolution(new PositionPartOne());
    }
    
    public Number getSolution(Position position) {
        List<String> lines = fileManager.parseLines(fileName);
        for (String line : lines) {
            String[] parsed = line.split(" ");
            String command = parsed[0];
            int number = Integer.parseInt(parsed[1]);
            if (command.equals("up")) {
                position.upBy(number);
            } else if (command.equals("down")) {
                position.downBy(number);
            } else if (command.equals("forward")) {
                position.forwardBy(number);
            }
        }
        return position.getHorizontal() * position.getDepth();
    }
    
    private interface Position {
        void upBy(int number);
        void downBy(int number);
        void forwardBy(int number);
        int getHorizontal();
        int getDepth();
    }
    
    private static class PositionPartOne implements Position {
        
        int horizontal = 0;
        int depth = 0;
        
        @Override
        public void upBy(int number) {
            depth -= number;
        }

        @Override
        public void downBy(int number) {
            depth += number;
        }

        @Override
        public void forwardBy(int number) {
            horizontal += number;
        }

        @Override
        public int getHorizontal() {
            return horizontal;
        }

        @Override
        public int getDepth() {
            return depth;
        }
    }

    private static class PositionPartTwo implements Position {

        int aim = 0;
        int horizontal = 0;
        int depth = 0;

        @Override
        public void upBy(int number) {
            aim -= number;
        }

        @Override
        public void downBy(int number) {
            aim += number;
        }

        @Override
        public void forwardBy(int number) {
            horizontal += number;
            depth += number * aim;
        }

        @Override
        public int getHorizontal() {
            return horizontal;
        }

        @Override
        public int getDepth() {
            return depth;
        }
    }
    
    @Override
    public int getDay() {
        return DAY;
    }

   


}
