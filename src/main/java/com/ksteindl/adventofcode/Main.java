package com.ksteindl.adventofcode;

import com.ksteindl.adventofcode.advent2020.day01.Find2020;
import com.ksteindl.adventofcode.advent2020.day02.TobogganPasswordValidator;
import com.ksteindl.adventofcode.advent2020.day03.TrajectoryCalculator;
import com.ksteindl.adventofcode.advent2020.day04.PassportScanner;
import com.ksteindl.adventofcode.advent2020.day05.SeatScanner;
import com.ksteindl.adventofcode.advent2020.day06.CustomFormProcessor;
import com.ksteindl.adventofcode.advent2020.day07.BagRuleProcessor;
import com.ksteindl.adventofcode.advent2020.day08.BootingCodeAnalyzer;
import com.ksteindl.adventofcode.advent2020.day09.XmasEncoder;
import com.ksteindl.adventofcode.advent2020.day10.AdapterCombiner;
import com.ksteindl.adventofcode.advent2020.day11.SeatPredictor;
import com.ksteindl.adventofcode.advent2020.day12.NavigationHelper;
import com.ksteindl.adventofcode.advent2020.day13.ScheduleOptimizer;
import com.ksteindl.adventofcode.advent2020.day14.DockingDataDecoder;
import com.ksteindl.adventofcode.advent2020.day15.MemoryChallenge;
import com.ksteindl.adventofcode.advent2020.day16.TicketTranslator;
import com.ksteindl.adventofcode.advent2020.day17.CubeInitiator;
import com.ksteindl.adventofcode.advent2020.day18.OperationEvaluator;
import com.ksteindl.adventofcode.advent2020.day19.MessageValidator;
import com.ksteindl.adventofcode.advent2020.day20.ImageReconstruator;
import com.ksteindl.adventofcode.advent2020.day21.AllergenFinder;
import com.ksteindl.adventofcode.advent2020.day22.CardCombat;
import com.ksteindl.adventofcode.advent2020.day23.CrabCups;
import com.ksteindl.adventofcode.advent2020.day24.TileFlipper;
import com.ksteindl.adventofcode.advent2020.day25.EncryptionBreaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final List<Puzzle2020> puzzles2020 = new ArrayList<>();
    private static final boolean IS_DEV_TEST = false;
    private static final String TEST_KEYWORD = "test";

    public static void main(String[] args) {
        boolean isTest;
        if (args.length == 0) {
            System.out.println("Run with '" + TEST_KEYWORD + "' as first argument in main, if you would like to test against test data!");
            isTest = IS_DEV_TEST;
        } else {
            isTest = args[0].equals(TEST_KEYWORD);
        }
        initPuzzles(isTest);
        printSolutions(22);
        //printAllSolutions();
    }

    private static void initPuzzles(boolean isTest) {
        puzzles2020.add(new Find2020(isTest));
        puzzles2020.add(new TobogganPasswordValidator(isTest));
        puzzles2020.add(new TrajectoryCalculator(isTest));
        puzzles2020.add(new PassportScanner(isTest));
        puzzles2020.add(new SeatScanner(isTest));
        puzzles2020.add(new CustomFormProcessor(isTest));
        puzzles2020.add(new BagRuleProcessor(isTest));
        puzzles2020.add(new BootingCodeAnalyzer(isTest));
        puzzles2020.add(new XmasEncoder(isTest));
        puzzles2020.add(new AdapterCombiner(isTest));
        puzzles2020.add(new SeatPredictor(isTest));
        puzzles2020.add(new NavigationHelper(isTest));
        puzzles2020.add(new ScheduleOptimizer(isTest));
        puzzles2020.add(new MemoryChallenge(isTest));
        puzzles2020.add(new TicketTranslator(isTest));
        puzzles2020.add(new CubeInitiator(isTest));
        puzzles2020.add(new OperationEvaluator(isTest));
        puzzles2020.add(new AllergenFinder(isTest));
        puzzles2020.add(new CardCombat(isTest));
        puzzles2020.add(new CrabCups(isTest));
        puzzles2020.add(new TileFlipper(isTest));
        puzzles2020.add(new EncryptionBreaker(isTest));
        puzzles2020.add(new ImageReconstruator(isTest));
        puzzles2020.add(new MessageValidator(isTest));
        puzzles2020.add(new DockingDataDecoder(isTest));
    }

    public static void printAllSolutions() {
        puzzles2020
                .stream()
                .sorted((puzzle1, puzzle2) -> ((Integer) puzzle1.getDay()).compareTo(puzzle2.getDay()))
                .forEach(puzzle -> printSolutions(puzzle));
    }

    public static void printSolutions(Puzzle2020 puzzle2020) {
        String solution1 = "Day " + puzzle2020.getDay() + " task1 solution: " + puzzle2020.getFirstSolution();
        String solution2 = "Day " + puzzle2020.getDay() + " task2 solution: " + puzzle2020.getSecondSolution();
        System.out.println(solution1);
        System.out.println(solution2);
        logger.info(solution1);
        logger.info(solution2);
    }

    public static void printSolutions(int day) {
        puzzles2020.stream().filter(puzzle -> puzzle.getDay() == day).findAny().ifPresent(puzzle -> printSolutions(puzzle));
    }


}
