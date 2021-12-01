package com.ksteindl.adventofcode.advent2020.day18.model;

public class Processed {

    private Long result;
    private int count;

    public Processed(Long result, int count) {
        this.result = result;
        this.count = count;
    }

    public Processed() {
    }

    public Long getResult() {
        return result;
    }

    public void setResult(Long result) {
        this.result = result;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
