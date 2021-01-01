package com.ksteindl.adventofcode.advent2020.day15.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Game {

    private int size;
    private int lastNumber;
    private Map<Integer, LinkedList<Integer>> numbers;

    public Game(List<Integer> initialNumbers) {
        this.size = initialNumbers.size() - 1;
        numbers = new HashMap<>();
        this.lastNumber = initialNumbers.get(size);
        for (int i = 0; i < initialNumbers.size() - 1; i++) {
            Integer number = initialNumbers.get(i);
            if (numbers.get(number) == null) {
                numbers.put(number, new LinkedList<>());
            }
            numbers.get(number).add(0, i);
        }

    }

    public void playNextRound() {
        size++;
        int nexLastNumber;
        List<Integer> numberList = numbers.get(lastNumber);
        if (numberList == null) {
            nexLastNumber = 0;
        } else {
            nexLastNumber = size - numberList.get(0) - 1;
        }
        mergeLastNumber();
        lastNumber = nexLastNumber;
    }

    private void mergeLastNumber() {
        if (numbers.get(lastNumber) == null) {
            numbers.put(lastNumber, new LinkedList<>());
        }
        numbers.get(lastNumber).add(0, size - 1);
    }

    public int getSize() {
        return size;
    }

    public int getLastNumber() {
        return lastNumber;
    }

    public Map<Integer, LinkedList<Integer>> getNumbers() {
        return numbers;
    }
}
