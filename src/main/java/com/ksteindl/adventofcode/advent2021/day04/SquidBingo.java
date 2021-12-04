package com.ksteindl.adventofcode.advent2021.day04;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class SquidBingo extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(SquidBingo.class);

    private static final int DAY = 4;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;
    private List<Integer> numbers;
    private List<Board> boards;
    private List<Board> boardsInOrder;

    public SquidBingo(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
        List<String> lines = fileManager.parseLines(fileName);
        numbers = Arrays.stream(lines.get(0).split(",")).map(string -> Integer.parseInt(string)).collect(Collectors.toList());
        boards = new ArrayList<>();
        for (int i = 0; i < (lines.size() - 1) / 6; i++) {
            int startIndex = 2 + i*6;
            Board board = new Board(lines.subList(startIndex, startIndex + 5));
            boards.add(board);
        }
        playBoards();
    }

    public static void main(String[] args) {
        SquidBingo day04 = new SquidBingo(false);
        logger.info(day04.getFirstSolution());
        logger.info(day04.getSecondSolution());
    }

    @Override
    public Number getFirstSolution() {
        return boardsInOrder.get(0).getScore();
    }

    @Override
    public Number getSecondSolution() {
        return boardsInOrder.get(boardsInOrder.size()-1).getScore();
    }
    
    
    private void playBoards() {
        boardsInOrder = new ArrayList<>();
        for (Integer number : numbers) {
            for (Board board : boards) {
                if (!boardsInOrder.stream().filter(played -> played.equals(board)).findAny().isPresent()) {
                    board.play(number);
                    if (board.winningNumber != null) {
                        boardsInOrder.add(board);
                    }
                }
            }
        }
    }
    

    @Override
    public int getDay() {
        return DAY;
    }

   


}
