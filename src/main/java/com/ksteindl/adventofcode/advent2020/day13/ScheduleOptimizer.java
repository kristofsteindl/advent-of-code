package com.ksteindl.adventofcode.advent2020.day13;

import com.ksteindl.adventofcode.Puzzle2020;
import com.ksteindl.adventofcode.advent2020.day13.model.BusScheduleElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduleOptimizer extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(ScheduleOptimizer.class);

    private static final int DAY = 13;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;

    public ScheduleOptimizer(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }


    @Override
    public Number getFirstSolution() {
        return getMultiplicationOfBusIdAndWaitingTime();
    }

    @Override
    public Number getSecondSolution() {
        if (isTest) {
            return getFirstTimestampForSubsequentBuses();
        }
        // TODO algorithm still not optimized enough
        return -getDay();
    }

    @Override
    public int getDay() {
        return DAY;
    }

    private long getFirstTimestampForSubsequentBuses() {
        List<String> input = fileManager.parseLines(fileName);
        String[] stringElements = input.get(1).split(",");
        List<BusScheduleElement> busSchedule = new ArrayList<>();
        for (int i = 0; i < stringElements.length; i++) {
            String stringElement = stringElements[i];
            if (!stringElement.equals("x")) {
                busSchedule.add(new BusScheduleElement(i, Integer.parseInt(stringElement)));
            }
        }
        Collections.sort(busSchedule);
        long t = 0;
        long firstBusId = busSchedule.get(0).getBusId();
        while (!areSubsequents(t += firstBusId, busSchedule)) {
                //logger.debug("t: " + t);

        }
        return t;
    }

    private boolean areSubsequents(final long t, final List<BusScheduleElement> busIdSequence) {
        if (t % busIdSequence.get(0).getBusId() != 0) {
            return false;
        }
        if (busIdSequence.size() == 1) {
            return true;
        }
        long timeDiff = busIdSequence.get(1).getIndex() - busIdSequence.get(0).getIndex();
        return areSubsequents(t + timeDiff, busIdSequence.subList(1, busIdSequence.size()));
    }

    private long getMultiplicationOfBusIdAndWaitingTime() {
        List<String> notes = fileManager.parseLines(fileName);
        int arrival = Integer.parseInt(notes.get(0));
        List<Integer> busIds= Arrays.asList(notes.get(1)
                .split(","))
                .stream()
                .filter(line -> !line.equals("x"))
                .map(string -> Integer.parseInt(string))
                .collect(Collectors.toList());
        int minDiffIndex = 0;
        for (int i = 0; i < busIds.size(); i++) {
            int busId = busIds.get(i);
            logger.debug("busId: " + busId + ", (arrival / busId + 1): " + (arrival / busId + 1) + ", ((arrival / busId + 1) * busId): " + ((arrival / busId + 1) * busId));
            int diff = getWaitingTime(arrival, busId);
            if (diff < getWaitingTime(arrival, busIds.get(minDiffIndex))) {
                logger.debug("found, diff: " + diff + ", busIds.get(minDiffIndex): " + busIds.get(minDiffIndex));
                minDiffIndex = i;
            }
        }
        logger.debug("minDiffIndex: " + minDiffIndex + ", busId: " + busIds.get(minDiffIndex));
        return busIds.get(minDiffIndex) * getWaitingTime(arrival, busIds.get(minDiffIndex));
    }

    private int getWaitingTime(int arrival, int busId) {
        return ((arrival / busId + 1) * busId) - arrival;
    }

    /*
    *
    * References
    *
    * */
}
