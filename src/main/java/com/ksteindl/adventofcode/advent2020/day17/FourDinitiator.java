package com.ksteindl.adventofcode.advent2020.day17;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FourDinitiator {

    private static final Logger logger = LogManager.getLogger(FourDinitiator.class);

    private final boolean[][] initialLevel;

    public FourDinitiator(boolean[][] initialLevel) {
        this.initialLevel = initialLevel;
    }

    public int getActiveCubeCount() {
        boolean[][][][] space = getInitialSpace();
        for (int cycle = 1; cycle < 7; cycle++) {
            space = executeCycle(space, cycle);
        }
        int acticeCubeCount = getActiveCubeCount(space);
        return acticeCubeCount;
    }

    private boolean[][][][] executeCycle(final boolean[][][][] spaceBefore, int cycle) {
        boolean[][][][] spaceAfter = new boolean[spaceBefore.length][spaceBefore[0].length][spaceBefore[0][0].length][spaceBefore[0][0][0].length];
        for (int i = 1; i < spaceBefore.length - 1; i++) {
            for (int j = 1; j < spaceBefore[0].length - 1; j++) {
                for (int k =1; k < spaceBefore[0][0].length - 1; k++) {
                    for (int l = 1; l < spaceBefore[0][0][0].length - 1; l++) {
                        spaceAfter[i][j][k][l] = activeAfterCycle(spaceBefore, i, j, k, l);
                    }

                }
            }
        }
        return spaceAfter;
    }

    private boolean activeAfterCycle(final boolean[][][][] spaceBefore, int z, int x, int y, int w) {
        int activeNeighboursCount = 0;
        for (int i = z - 1; i < z + 2; i++) {
            for (int j = x - 1; j < x + 2; j++) {
                for (int k = y - 1; k < y + 2; k++) {
                    for (int l = w - 1; l < w + 2; l++) {
                        activeNeighboursCount += spaceBefore[i][j][k][l] ? 1 : 0;
                    }

                }
            }
        }
        activeNeighboursCount -= spaceBefore[z][x][y][w] ? 1 : 0;
        if (!spaceBefore[z][x][y][w]) {
            return activeNeighboursCount == 3;
        }
        return activeNeighboursCount == 2 || activeNeighboursCount == 3;

    }

    private int getActiveCubeCount(boolean[][][][] space) {
        int acticeCubeCount = 0;
        for (int i = 0; i < space.length; i++) {
            for (int j = 0; j < space[0].length; j++) {
                for (int k = 0; k < space[0][0].length; k++) {
                    for (int l = 0; l < space[0][0][0].length; l++) {
                        acticeCubeCount += space[i][j][k][l] ? 1 : 0;
                    }
                }
            }
        }
        return acticeCubeCount;
    }

    private boolean[][][][] getInitialSpace() {
        boolean[][][][] space = new boolean[1 + 2 * (6 + 2)][initialLevel.length + 2 * (6 + 2)][initialLevel[0].length + 2 * (6 + 2)][1 + 2 * (6 + 2)];
        for (int i = 0; i < initialLevel.length; i++) {
            for (int j = 0; j < initialLevel[0].length; j++) {
                space[7][i + 7][j + 7][7] = initialLevel[i][j];
            }
        }
        return space;

    }
}
