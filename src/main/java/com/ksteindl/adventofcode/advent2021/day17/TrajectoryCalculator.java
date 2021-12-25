package com.ksteindl.adventofcode.advent2021.day17;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TrajectoryCalculator extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(TrajectoryCalculator.class);

    private static final int DAY = 17;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;
    Integer xtStart;
    Integer xtEnd;
    Integer ytStart;
    Integer ytEnd;

    public TrajectoryCalculator(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
        xtStart = isTest? 20 : 25;
        xtEnd = isTest? 30 : 67;
        ytStart = isTest? -10 : -260;
        ytEnd = isTest? -5 : -200;
    }

    public static void main(String[] args) {
        TrajectoryCalculator day = new TrajectoryCalculator(false);
        logger.info(day.getFirstSolution());
        logger.info(day.getSecondSolution());
    }

    @Override
    public Number getSecondSolution() {
        Integer minXSpeed = getMinXSpeed();
        int xSpeed = minXSpeed;
        int sum = 0;
        while (xSpeed <= xtEnd) {
            int ySpeed = getMaxYSpeed();
            while (ySpeed >= ytStart  ) {
                if (willBeInTarget(xSpeed, ySpeed)) {
                    System.out.println("xSpeed: " + xSpeed + ", ySpeed: " + ySpeed);
                    sum++;
                }
                ySpeed--;
            }
            xSpeed++;
        }
        return sum;
    }
    
    private boolean willBeInTarget(Integer xSpeed, Integer ySpeed) {
        int x = 0;
        int y = 0;
        while ((x += xSpeed) <= xtEnd && (y += ySpeed) >= ytStart) {
            if (x >= xtStart && y <= ytEnd) {
                return true;
            }
            if (xSpeed > 0) {
                xSpeed--;
            }
            ySpeed--;
        }
        return false;
    }
    
    
    
    private Integer getMinXSpeed() {
        int minXSpeed = 0;
        do {
            minXSpeed++;
        } while (getSumWidth(minXSpeed) < xtStart);
        return minXSpeed;
    }
    
    private int getSumWidth(int speed) {
        int sum = 0;
        while (speed > 0) {
            sum += speed;
            speed--;
        }
        return sum;
    }


    @Override
    public Number getFirstSolution() {
        int maxHeight = 0;
        int height = getMaxYSpeed();
        while (height > 0) {
            maxHeight += height;
            height--;
        }
        return maxHeight;
    }
    
    private Integer getMaxYSpeed() {
        return - (ytStart + 1);
    }
    

    @Override
    public int getDay() {
        return DAY;
    }

}
