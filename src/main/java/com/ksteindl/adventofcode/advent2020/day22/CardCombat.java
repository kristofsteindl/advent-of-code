package com.ksteindl.adventofcode.advent2020.day22;

import com.ksteindl.adventofcode.Puzzle2020;
import com.ksteindl.adventofcode.advent2020.day22.model.BasicGame;
import com.ksteindl.adventofcode.advent2020.day22.model.Game;
import com.ksteindl.adventofcode.advent2020.day22.model.GameSetUp;
import com.ksteindl.adventofcode.advent2020.day22.model.RecursiveGame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CardCombat extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(CardCombat.class);

    private static final int DAY = 22;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";
    private static String INPUT_TEST_FILE_INF = FOLDER_NAME + "inputDec" + DAY_STRING + "_test_inf.txt";

    private final String fileName;
    private GameSetUp gameSetUp;

    public CardCombat(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
        List<String> lines = fileManager.parseLines(fileName);
        this.gameSetUp = new GameSetUp(lines);
    }


    @Override
    public Number getFirstSolution() {
        return new BasicGame(gameSetUp).getFinalResult().getWinningScore();
    }

    @Override
    public Number getSecondSolution() {
        return new RecursiveGame(gameSetUp).getFinalResult().getWinningScore();
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
