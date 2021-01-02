package com.ksteindl.adventofcode.advent2020.day13.model;

public class BusScheduleElement implements Comparable<BusScheduleElement>{

    private final int index;
    private final Long busId;


    public BusScheduleElement(int index, Long busId) {
        this.index = index;
        this.busId = busId;

    }

    public Long getBusId() {
        return busId;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public int compareTo(BusScheduleElement other) {
        return this.getBusId() < other.getBusId() ? 1 :this.getBusId() > other.getBusId() ? -1 : 0;
    }

    @Override
    public String toString() {
        return "BusScheduleElement{" +
                "index=" + index +
                ", busId=" + busId +
                '}';
    }
}
