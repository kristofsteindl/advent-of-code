package com.ksteindl.adventofcode.advent2020.day15;

import com.ksteindl.adventofcode.advent2020.Puzzle2020;
import com.ksteindl.adventofcode.advent2020.day15.model.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MemoryChallange extends Puzzle2020 {

    // TODO - introduce the multiple test cases, not just the first one
    private static final Logger logger = LogManager.getLogger(MemoryChallange.class);

    private static final int DAY = 15;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;

    public MemoryChallange(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }


    @Override
    public Number getFirstSolution() {
        return calculateElement(2020);
    }

    @Override
    public Number getSecondSolution() {
        return calculateElement(30000000);
    }

    @Override
    public int getDay() {
        return DAY;
    }

    private int calculateElement(int finalRound) {
        List<String> initialNumbers = fileManager.parseLines(fileName);
        LinkedList<Integer> numbers = new LinkedList<>();
        for (String string : initialNumbers.get(0).split(",")) {
            numbers.add(Integer.parseInt(string));
        }
        Game game = new Game(numbers);
        int round = numbers.size();
        while (round < finalRound) {
            game.playNextRound();
            round++;
        }
        return game.getLastNumber();
    }

    private int calculateElementOld(int index) {
        List<String> initialNumbers = fileManager.parseLines(fileName);
        LinkedList<Integer> numbers = new LinkedList<>();
        for (String string : initialNumbers.get(0).split(",")) {
            numbers.add(Integer.parseInt(string));
        }
        int round = numbers.size();
        while (round < index) {
            numbers.add(getNextNumber(numbers));
            round++;
        }
        return numbers.get(index - 1);
    }

    private int getNextNumber(LinkedList<Integer> numbers) {
        Iterator<Integer> iterator = numbers.descendingIterator();
        int number = iterator.next();
        logger.debug("round: " +  numbers.size() + ", number: " + number);
        int prevRound = numbers.size();
        while(iterator.hasNext()){
            prevRound--;
            int prevNumber = iterator.next();
            if (number == prevNumber) {
                return numbers.size() - prevRound;
            }
        }
        return 0;
    }

    /*
    *
    * References
    *
    * */
}
