package com.ksteindl.adventofcode.advent2021.day06;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import com.ksteindl.adventofcode.advent2021.day05.VentFinder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FishModeler extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(FishModeler.class);

    private static final int DAY = 6;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;

    public FishModeler(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    public static void main(String[] args) {
        FishModeler modeler = new FishModeler(false);
        logger.info(modeler.getFirstSolution());
        logger.info(modeler.getSolution(256));
    }
    
    public Long getSolution(int days) {
        List<Integer> initial = Arrays.stream(fileManager.parseLines(fileName).get(0).split(","))
                .map(string -> Integer.parseInt(string))
                .collect(Collectors.toList());
        Map<Integer, Long> fishes = IntStream.range(0, 9).boxed().collect(Collectors.toMap(Function.identity(),timer -> 0l));
        for (int i = 0; i < initial.size(); i++) {
            fishes.put(initial.get(i), fishes.get(initial.get(i)) + 1);
        }
        for (int day = 0; day < days; day++) {
            long newBreed = fishes.get(0);
            for (int i = 1; i < 9; i++) {
                fishes.put(i - 1, fishes.get(i));
            }
            long sixers = fishes.get(6);
            fishes.put(6, sixers + newBreed);
            fishes.put(8, newBreed);
        }
        return fishes.values().stream().mapToLong(i->i).sum();
    }

    @Override
    public Number getFirstSolution() {
        List<Integer> initial = Arrays.stream(fileManager.parseLines(fileName).get(0).split(","))
                .map(string -> Integer.parseInt(string))
                .collect(Collectors.toList());
        List<Fish> fishes = initial.stream().map(timer -> new Fish(timer)).collect(Collectors.toList());
        for (int day = 0; day < 80; day++) {
            List<Fish> dailyBreed = new ArrayList<>();
            for (int i = 0; i < fishes.size(); i++) {
                Fish fish = fishes.get(i);
                fish.plusDay();
                if (fish.timer == 6 && fish.justBear) {
                    dailyBreed.add(new Fish());
                }
            }
            fishes.addAll(dailyBreed);
        }
        return fishes.size();
    }

    @Override
    public Number getSecondSolution() {
        return getSolution(256);
    }
    

    @Override
    public int getDay() {
        return DAY;
    }

   


}
