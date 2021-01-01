package com.ksteindl.adventofcode.advent2020.day17;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreeDinitiator {

    private static final Logger logger = LogManager.getLogger(ThreeDinitiator.class);

    private final boolean[][] initialLevel;

    public ThreeDinitiator(boolean[][] initialLevel) {
        this.initialLevel = initialLevel;
    }

    public int getActiveCubeCount() {
        boolean[][][] space = getInitialSpace();
        logger.debug(getPrintedSpace(space, 0));
        for (int cycle = 1; cycle < 7; cycle++) {
            space = executeCycle(space, cycle);
            logger.debug(getPrintedSpace(space, cycle));
        }
        int acticeCubeCount = getActiveCubeCount(space);
        return acticeCubeCount;
    }

    private boolean[][][] executeCycle(final boolean[][][] spaceBefore, int cycle) {
        boolean[][][] spaceAfter = new boolean[spaceBefore.length][spaceBefore[0].length][spaceBefore[0][0].length];
        for (int i = 1; i < spaceBefore.length - 1; i++) {
            for (int j = 1; j < spaceBefore[0].length - 1; j++) {
                for (int k =1; k < spaceBefore[0][0].length - 1; k++) {
                    spaceAfter[i][j][k] = activeAfterCycle(spaceBefore, i, j, k);
                }
            }
        }
        return spaceAfter;
    }

    private boolean activeAfterCycle(final boolean[][][] spaceBefore, int z, int x, int y) {
        int activeNeighboursCount = 0;
        for (int i = z - 1; i < z + 2; i++) {
            for (int j = x - 1; j < x + 2; j++) {
                for (int k = y - 1; k < y + 2; k++) {
                    activeNeighboursCount += spaceBefore[i][j][k] ? 1 : 0;
                }
            }
        }
        activeNeighboursCount -= spaceBefore[z][x][y] ? 1 : 0;
        if (!spaceBefore[z][x][y]) {
            return activeNeighboursCount == 3;
        }
        return activeNeighboursCount == 2 || activeNeighboursCount == 3;

    }

    private String getPrintedSpace(boolean[][][] space, int cycle) {
        StringBuilder builder = new StringBuilder(cycle + " cycle\n");
        for (int i = 0; i < space.length; i++) {
            builder.append(i + " level\n");
            for (int j = 0; j < space[0].length; j++) {
                builder.append("\n");
                for (int k = 0; k < space[0][0].length; k++) {
                    builder.append(space[i][j][k] ? '#' : '.');
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private int getActiveCubeCount(boolean[][][] space) {
        int acticeCubeCount = 0;
        for (int i = 0; i < space.length; i++) {
            for (int j = 0; j < space[0].length; j++) {
                for (int k = 0; k < space[0][0].length; k++) {
                    acticeCubeCount += space[i][j][k] ? 1 : 0;
                }
            }
        }
        return acticeCubeCount;
    }

    private boolean[][][] getInitialSpace() {
        boolean[][][] space = new boolean[1 + 2 * (6 + 2)][initialLevel.length + 2 * (6 + 2)][initialLevel[0].length + 2 * (6 + 2)];
        for (int i = 0; i < initialLevel.length; i++) {
            for (int j = 0; j < initialLevel[0].length; j++) {
                space[7][i + 7][j + 7] = initialLevel[i][j];
            }
        }
        return space;

    }
}
