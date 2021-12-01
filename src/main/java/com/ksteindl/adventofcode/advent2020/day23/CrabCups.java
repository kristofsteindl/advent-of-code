package com.ksteindl.adventofcode.advent2020.day23;

import com.ksteindl.adventofcode.advent2020.Puzzle2020;

public class CrabCups extends Puzzle2020 {

    private static final int DAY = 23;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static Long TEST_INPUT_NUMBERS = 389125467L;
    private static Long INPUT_NUMBERS = 368195742L ;

    private final Long input;

    public CrabCups(boolean isTest) {
        super(isTest);
        input = isTest ? TEST_INPUT_NUMBERS : INPUT_NUMBERS;
    }

    @Override
    public Number getFirstSolution() {
        CrabGame crabGame = new CrabGame(input, false);
        return crabGame.getLabelsEndOfRounds(100);
    }

    @Override
    public Number getSecondSolution() {
        CrabGame crabGame = new CrabGame(input, true);
        // return crabGame.getLabelsEndOfRounds(1000000);
        return -DAY;
    }

    @Override
    public int getDay() {
        return DAY;
    }

    /*
    *
    * References
    *
    * */
}
