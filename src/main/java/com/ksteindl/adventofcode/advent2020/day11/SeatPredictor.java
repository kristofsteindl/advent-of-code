package com.ksteindl.adventofcode.advent2020.day11;

import com.ksteindl.adventofcode.Puzzle2020;
import com.ksteindl.adventofcode.advent2020.day11.model.Seat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class SeatPredictor extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(SeatPredictor.class);

    private static final int DAY = 11;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;
    private final char[][] initialWaitingArea;

    public SeatPredictor(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
        List<String> emptyWaitingAreaLine = fileManager.parseLines(fileName);
        this.initialWaitingArea = getEmptyWaitingArea(emptyWaitingAreaLine);
    }

    @Override
    public Number getFirstSolution() {
        return countOccupiedSeatsAfterStabilizaion(4);
    }

    @Override
    public Number getSecondSolution() {
        return countOccupiedSeatsAfterStabilizaion(5);
    }

    private int countOccupiedSeatsAfterStabilizaion(int limit) {
        char[][] waitingArea = clone(initialWaitingArea);
        boolean isChanged = true;
        boolean roundToComeIsOdd = true; //1st round to come
        do {
            char[][] nextWaitingArea = clone(waitingArea);
            isChanged = nextRound(waitingArea, roundToComeIsOdd, nextWaitingArea, limit);
            roundToComeIsOdd = !roundToComeIsOdd;
            waitingArea = nextWaitingArea;
        } while (isChanged);
        int occupiedSeatCount = countOccupiedSeats(waitingArea);
        return occupiedSeatCount;
    }

    private boolean nextRound(char[][] waitingArea, boolean roundIsOdd, char[][] nextWaitingArea, int limit) {
        boolean tableChanged = false;
        for (int row = 0; row < waitingArea.length; row++) {
            for (int column = 0; column < waitingArea[0].length; column++) {
                boolean changed = tryChange(waitingArea, row, column, roundIsOdd, nextWaitingArea, limit);
                tableChanged = tableChanged || changed;
            }
        }
        return tableChanged;
    }

    private boolean tryChange(char[][] waitingArea, int row, int column, boolean roundIsOdd, char[][] nextWaitingArea, int limit) {
        char seat = waitingArea[row][column];
        int occupiedCount;
        if (limit == 4) {
            occupiedCount = countAdjecantOccupied(waitingArea, row, column);
        } else {
            occupiedCount = countOccupiedFirstCanBeSeen(waitingArea, row, column);
        }
        if (roundIsOdd && seat == Seat.EMPTY.symbol) {
            if (occupiedCount == 0) {
                nextWaitingArea[row][column] = Seat.OCCUPIED.symbol;
                return true;
            }
        } else if (seat == Seat.OCCUPIED.symbol) {
            if (occupiedCount >= limit) {
                nextWaitingArea[row][column] = Seat.EMPTY.symbol;
                return true;
            }
        }
        return false;
    }

    private int countOccupiedFirstCanBeSeen(char[][] waitingArea, int row, int column) {
        int count = 0;

        // Left-Up
        int counter = 1;
        boolean foundOccupied = false;
        while(!foundOccupied && row - counter > -1 && column - counter > -1) {
            char seat = waitingArea[row - counter][column - counter];
            if (seat != Seat.FLOOR.symbol) {
                foundOccupied = true;
                if (seat == Seat.OCCUPIED.symbol) {
                    count++;
                }
            }
            counter++;
        }

        // Up
        counter = 1;
        foundOccupied = false;
        while(!foundOccupied && row - counter > -1) {
            char seat = waitingArea[row - counter][column];
            if (seat != Seat.FLOOR.symbol) {
                foundOccupied = true;
                if (seat == Seat.OCCUPIED.symbol) {
                    count++;
                }
            }
            counter++;
        }

        // Up Right
        counter = 1;
        foundOccupied = false;
        while(!foundOccupied && row - counter > -1 && column + counter < waitingArea[0].length) {
            char seat = waitingArea[row - counter][column + counter];
            if (seat != Seat.FLOOR.symbol) {
                foundOccupied = true;
                if (seat == Seat.OCCUPIED.symbol) {
                    count++;
                }
            }
            counter++;
        }

        // Left
        counter = 1;
        foundOccupied = false;
        while(!foundOccupied && column - counter > -1) {
            char seat = waitingArea[row][column - counter];
            if (seat != Seat.FLOOR.symbol) {
                foundOccupied = true;
                if (seat == Seat.OCCUPIED.symbol) {
                    count++;
                }
            }
            counter++;
        }

        // Right
        counter = 1;
        foundOccupied = false;
        while(!foundOccupied && column + counter < waitingArea[0].length) {
            char seat = waitingArea[row][column + counter];
            if (seat != Seat.FLOOR.symbol) {
                foundOccupied = true;
                if (seat == Seat.OCCUPIED.symbol) {
                    count++;
                }
            }
            counter++;
        }

        // Left Down
        counter = 1;
        foundOccupied = false;
        while(!foundOccupied && row + counter < waitingArea.length && column - counter > -1) {
            char seat = waitingArea[row + counter][column - counter];
            if (seat != Seat.FLOOR.symbol) {
                foundOccupied = true;
                if (seat == Seat.OCCUPIED.symbol) {
                    count++;
                }
            }
            counter++;
        }

        // Down
        counter = 1;
        foundOccupied = false;
        while(!foundOccupied && row + counter < waitingArea.length) {
            char seat = waitingArea[row + counter][column];
            if (seat != Seat.FLOOR.symbol) {
                foundOccupied = true;
                if (seat == Seat.OCCUPIED.symbol) {
                    count++;
                }
            }
            counter++;
        }

        // Down Right
        counter = 1;
        foundOccupied = false;
        while(!foundOccupied && row + counter < waitingArea.length && column + counter <  waitingArea[0].length) {
            char seat = waitingArea[row + counter][column + counter];
            if (seat != Seat.FLOOR.symbol) {
                foundOccupied = true;
                if (seat == Seat.OCCUPIED.symbol) {
                    count++;
                }
            }
            counter++;
        }
        return count;
    }


    private int countAdjecantOccupied(char[][] waitingArea, int row, int column) {
        int count = 0;
        Seat seat = Seat.OCCUPIED;
        // Left-Up
        if (row > 0 && column > 0 && waitingArea[row - 1][column - 1] == seat.symbol) {
            count++;
        }

        // Up
        if (row > 0 && waitingArea[row - 1][column] == seat.symbol) {
            count++;
        }

        // Up Right
        if (row > 0 && column + 1 < waitingArea[0].length && waitingArea[row - 1][column + 1] == seat.symbol) {
            count++;
        }

        // Left
        if (column > 0 && waitingArea[row][column - 1] == seat.symbol) {
            count++;
        }

        // Right
        if (column + 1 < waitingArea[0].length && waitingArea[row][column + 1] == seat.symbol) {
            count++;
        }

        // Left Down
        if (row + 1  < waitingArea.length && column > 0 && waitingArea[row + 1][column - 1] == seat.symbol) {
            count++;
        }

        // Down
        if (row + 1 < waitingArea.length && waitingArea[row + 1][column] == seat.symbol) {
            count++;
        }

        // Down Right
        if (row + 1  < waitingArea.length && column + 1  < waitingArea[0].length && waitingArea[row + 1][column + 1] == seat.symbol) {
            count++;
        }
        return count;
    }

    private int countOccupiedSeats(char[][] waitingArea) {
        int occupiedSeatCount = 0;
        for (int i = 0; i < waitingArea.length; i++) {
            for (int j = 0; j < waitingArea[0].length; j++) {
                if (waitingArea[i][j] == Seat.OCCUPIED.symbol) {
                    occupiedSeatCount++;
                }
            }
        }
        return occupiedSeatCount;

    }


    @Override
    public int getDay() {
        return DAY;
    }

    private char[][] clone(char[][] oldArray) {
        char[][] array = new char[oldArray.length][oldArray[0].length];
        for (int i = 0; i < oldArray.length; i++) {
            for (int j = 0; j < oldArray[0].length; j++) {
                array[i][j] = oldArray[i][j];
            }
        }
        return array;
    }

    private char[][] getEmptyWaitingArea(List<String> emptyWaitingAreaLine) {
        char[][] emptyWaitingArea = new char[emptyWaitingAreaLine.size()][emptyWaitingAreaLine.get(0).length()];
        for (int i = 0; i < emptyWaitingAreaLine.size(); i++) {
            for (int j = 0; j < emptyWaitingAreaLine.get(0).length(); j++) {
                emptyWaitingArea[i][j] = emptyWaitingAreaLine.get(i).charAt(j);
            }
        }
        return emptyWaitingArea;
    }

    /*
    *
    * References
    *
    * */
}
