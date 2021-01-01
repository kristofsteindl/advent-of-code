package com.ksteindl.adventofcode.advent2020.day05.pojo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Seat {

    private static final Logger logger = LogManager.getLogger(Seat.class);

    private final String seatCode;
    private final int row;
    private final int column;

    public Seat(int row, int column) {
        this.seatCode = null;
        this.row = row;
        this.column = column;
    }

    public Seat(String seatCode) {
        this.seatCode = seatCode;
        this.row = getRow(seatCode);
        this.column = getColumn(seatCode);

    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getSetId() {
        return row * 8 + column;
    }

    private static int getRow(String seatCode) {
        boolean[] bitArray = new boolean[7];
        for (int i = 0; i < bitArray.length; i++) {
            bitArray[i] = seatCode.charAt(i) == 'B';
        }
        return bitToInt(bitArray);
    }

    private static int getColumn(String seatCode) {
        boolean[] bitArray = new boolean[3];
        for (int i = 0; i < bitArray.length; i++) {
            bitArray[i] = seatCode.charAt(7 + i) == 'R';
        }
        return bitToInt(bitArray);
    }

    private static int bitToInt(boolean[] bitArray) {
        int length = bitArray.length;
        int integer = 0;
        for (int i = 0; i < length; i++) {
            if (bitArray[i]) {
                integer += Math.pow(2, length - i - 1);
            }
        }
        return integer;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatCode='" + seatCode + '\'' +
                ", row=" + row +
                ", column=" + column +
                ", seatId=" + getSetId() +
                '}';
    }
}
