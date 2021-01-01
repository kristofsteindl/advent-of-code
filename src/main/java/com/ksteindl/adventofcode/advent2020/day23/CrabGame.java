package com.ksteindl.adventofcode.advent2020.day23;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CrabGame {

    private static final Logger logger = LogManager.getLogger(CrabGame.class);

    private final List<Integer> numbers;

    public CrabGame(Long input, boolean millionGame) {
        numbers = new ArrayList<>();
        long temp = 1;
        while (input > 0) {
            numbers.add((int) (input % 10));
            input = input / 10;
        }
        Collections.reverse(numbers);
        if (millionGame) {
            int max = numbers.stream().mapToInt(i -> i).max().getAsInt();
            while (numbers.size() < 1000000) {
                logger.trace("numbers.size(): " + numbers.size() + ", max: " + max + 1);
                numbers.add(++max);
            }
        }
    }

    public Long getLabelsEndOfRounds(int maxRound) {
        int[] threeCups = new int[3];
        for (int round = 0; round < maxRound; round++) {
            int  roundIndex = round % numbers.size();
            int currentCup = numbers.get(roundIndex);
            threeCups = getThreeCups(threeCups, roundIndex, maxRound);
            insertThreeCups(threeCups, currentCup);
            rearrangeCups(currentCup, roundIndex);
        }
        rearrangeCups(1, 0);
        StringBuilder builder = new StringBuilder();
        numbers.subList(1, numbers.size()).forEach(number -> builder.append(number));
        return Long.parseLong(builder.toString());
    }

    private void rearrangeCups(int currentCup, int roundIndex) {
        int shift = 0;
        while (numbers.get(roundIndex + shift) != currentCup) {
            shift++;
        }
        for (int i = 0; i < shift; i++) {
            int first = numbers.remove(0);
            numbers.add(first);
        }
    }

    private void insertThreeCups(int[] threeCups, int currentCup) {
        Integer indexAfterInsert = getIndexAfterInsert(threeCups, currentCup);
        for (int i = 0; i < threeCups.length; i++) {
            numbers.add(indexAfterInsert + 1, threeCups[threeCups.length - 1 - i]);
        }
    }

    private int getIndexAfterInsert(int[] threeCups, int currentCup) {
        int lowestCupValue = getLowestCupValue();
        Integer indexAfterInsert = null;
        int i = 1;
        while (currentCup - i >= lowestCupValue && indexAfterInsert == null) {
            int j = 0;
            while (j < numbers.size() && numbers.get(j) != currentCup - i) {
                j++;
            }
            if (j < numbers.size())  {
                indexAfterInsert = j;
            }
            i++;
        }
        if (indexAfterInsert != null) {
            return indexAfterInsert;
        }
        indexAfterInsert = 0;
        for (int j = 0; j < numbers.size(); j++) {
            if (numbers.get(j) > numbers.get(indexAfterInsert) && numbers.get(j) != currentCup) {
                indexAfterInsert = j;
            }
        }
        return indexAfterInsert;

    }

    private int getLowestCupValue() {
        return numbers.stream().mapToInt(i -> i).min().getAsInt();
    }


    private int[] getThreeCups(int[] threeCups, int roundIndex, int maxRound) {
        for (int i = 0; i < 3; i++) {
            int indexToRemove = roundIndex + 1 > numbers.size() - 1 ? 0 : roundIndex + 1;
            threeCups[i] = numbers.remove(indexToRemove);
        }
        return  threeCups;

    }
}
