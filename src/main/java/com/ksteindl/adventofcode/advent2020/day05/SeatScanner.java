package com.ksteindl.adventofcode.advent2020.day05;

import com.ksteindl.adventofcode.Puzzle2020;
import com.ksteindl.adventofcode.advent2020.day05.pojo.Seat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.groupingBy;


public class SeatScanner extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(SeatScanner.class);

    private static final int DAY = 5;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;

    public SeatScanner(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    @Override
    public Number getFirstSolution() {
        return getHighestSeatId();
    }

    @Override
    public Number getSecondSolution() {
        return getMySeat().getSetId();
    }

    @Override
    public int getDay() {
        return DAY;
    }

    private void testInputTest() {
        fileManager.parseLines(fileName).stream().map(seatCode -> new Seat(seatCode)).forEach(seat -> {
            logger.debug("row: " + seat.getRow());
            logger.debug("column: " + seat.getColumn());
            logger.debug("getSetId: " + seat.getSetId());
        });
    }

    private int getHighestSeatId() {
        return fileManager.parseLines(fileName)
                .stream()
                .mapToInt(seatCode -> new Seat(seatCode).getSetId())
                .max()
                .getAsInt();
    }

    private Seat getMySeat() {
        Map<Integer, List<Seat>> seats = fileManager.parseLines(fileName)
                .stream()
                .map(seatCode -> new Seat(seatCode))
                .collect(groupingBy(Seat::getRow));
       List<Seat> myRow = seats.entrySet().stream().filter(rowEntry -> rowEntry.getValue().size() < 8).findAny().get().getValue();
       myRow.stream().forEach(seat -> logger.debug(seat.toString()));
       int myColumn = 0;
       while (!isThisMyColumn(myRow, myColumn)) {
           myColumn++;
       }
       return new Seat(myRow.get(0).getRow(), myColumn);
    }

    private boolean isThisMyColumn(List<Seat> myRow, final int myColumn) {
        return !myRow.stream().anyMatch(seat -> seat.getColumn() == myColumn);
    }




}
