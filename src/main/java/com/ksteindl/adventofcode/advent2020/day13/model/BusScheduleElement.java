package com.ksteindl.adventofcode.advent2020.day13.model;

public class BusScheduleElement implements Comparable<BusScheduleElement>{

    private final int index;
    private final int busId;


    public BusScheduleElement(int index, int busId) {
        this.index = index;
        this.busId = busId;

    }

    public int getBusId() {
        return busId;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public int compareTo(BusScheduleElement other) {
        return this.getBusId() < other.getBusId() ? 1 :this.getBusId() > other.getBusId() ? -1 : 0;
    }
}
