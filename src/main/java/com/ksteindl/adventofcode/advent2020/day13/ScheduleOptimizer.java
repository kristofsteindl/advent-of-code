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
        return getFirstTimestampForSubsequentBusesChinese();
    }

    @Override
    public int getDay() {
        return DAY;
    }

    private long getFirstTimestampForSubsequentBuses() {
        List<BusScheduleElement> busSchedule = getBusSchedule();
        Collections.sort(busSchedule);
        Long firstBusId = busSchedule.get(0).getBusId();
        long t = firstBusId;
        while (!areSubsequents(t, busSchedule)) {
            t += firstBusId;
        }
        return t;
    }

    private long getFirstTimestampForSubsequentBusesChinese() {
        List<BusScheduleElement> busSchedule = getBusSchedule();
        Long prodOfBusIds = busSchedule.stream().mapToLong(busScheduleELement -> busScheduleELement.getBusId()).reduce(1, (prod, element) -> prod * element);
        long sum = 0;
        for (int i = 0; i < busSchedule.size(); i++) {
            BusScheduleElement element = busSchedule.get(i);
            Long partialProduct = prodOfBusIds / element.getBusId();
            Long remainder = element.getIndex() == 0 ? 0 : element.getBusId() - element.getIndex();
            sum += partialProduct * computeInverse(partialProduct, element.getBusId()) * remainder;
        }
        return sum % prodOfBusIds;
    }

    public static long computeInverse(Long a, Long b){
        long m = b, t, q;
        long x = 0, y = 1;
        if (b == 1) {
            return 0; // Apply extended Euclid Algorithm
        }
        while (a > 1) {
            q = a / b; // q is quotient
            t = b; // now proceed same as Euclid's algorithm
            b = a % b;
            a = t;
            t = x;
            x = y - q * x;
            y = t;
        } // Make x1 positive
        if (y < 0) y += m;
        return y;

    }

    private List<BusScheduleElement> getBusSchedule() {
        List<String> input = fileManager.parseLines(fileName);
        String[] stringElements = input.get(1).split(",");
        List<BusScheduleElement> busSchedule = new ArrayList<>();
        for (int i = 0; i < stringElements.length; i++) {
            String stringElement = stringElements[i];
            if (!stringElement.equals("x")) {
                busSchedule.add(new BusScheduleElement(i, Long.parseLong(stringElement)));
            }
        }
        return  busSchedule;
    }

    private boolean areSubsequents(final long t, final List<BusScheduleElement> busIdSequence) {
        if (t % busIdSequence.get(0).getBusId() != 0) {
            return false;
        }
        if (busIdSequence.size() == 1) {
            return true;
        }
        long timeDiff = busIdSequence.get(1).getIndex() - busIdSequence.get(0).getIndex();
        logger.trace("areSubsequents call:t: " + (t + timeDiff) + ", busSequence: " + busIdSequence.subList(1, busIdSequence.size()).toString());
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
    * https://www.freecodecamp.org/news/how-to-implement-the-chinese-remainder-theorem-in-java-db88a3f1ffe0/
    *
    * */
}
