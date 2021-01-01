package com.ksteindl.adventofcode.advent2020.day03;

import com.ksteindl.adventofcode.Puzzle2020;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TrajectoryCalculator extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(TrajectoryCalculator.class);

    private static final int DAY = 3;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private static final char TREE = '#';

    private final String fileName;

    public TrajectoryCalculator(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    @Override
    public Number getFirstSolution() {
        return countTrees();
    }

    @Override
    public Number getSecondSolution() {
        return multiplyTrajectoryCounts();
    }

    @Override
    public int getDay() {
        return DAY;
    }

    private int multiplyTrajectoryCounts() {
        return
                countTrees(1,1) *
                countTrees(1,3) *
                countTrees(1,5) *
                countTrees(1,7) *
                countTrees(2,1);
    }

    private int countTrees() {
        return countTrees(1,3);
    }

    private int countTrees(int downStep, int rightStep) {
        boolean[][] boolArrayForest = getBoolArrayForest(fileManager.parseLines(fileName));
        int treeCount = 0;
        int column = 0;
        int row = 0;
        while (row < boolArrayForest.length) {
            if (boolArrayForest[row][column]) {
                logger.trace("Hit! row: " + row + ", column: " + column);
                treeCount++;
            }
            column = incrementColumn(boolArrayForest, column, rightStep);
            row = row + downStep;
        }
        return treeCount;
    }

    private int incrementColumn(boolean[][] boolArrayForest, int column, int step) {
        column = column + step;
        if (column >= boolArrayForest[0].length) {
            column = column - boolArrayForest[0].length;
        }
        return column;
    }

    private boolean[][] getBoolArrayForest(List<String> stringListForest) {
        boolean[][] boolArrayForest = new boolean[stringListForest.size()][stringListForest.get(0).length()];
        for (int i = 0; i < stringListForest.size(); i++) {
            for (int j = 0; j < stringListForest.get(0).length(); j++) {
                boolArrayForest[i][j] = stringListForest.get(i).charAt(j) == TREE;
            }
        }
        return boolArrayForest;
    }

}
