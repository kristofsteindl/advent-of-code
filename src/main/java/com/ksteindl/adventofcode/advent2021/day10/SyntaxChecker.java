package com.ksteindl.adventofcode.advent2021.day10;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class SyntaxChecker extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(SyntaxChecker.class);

    private static final int DAY = 10;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;

    public SyntaxChecker(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    public static void main(String[] args) {
        SyntaxChecker day = new SyntaxChecker(false);
        logger.info(day.getFirstSolution());
        logger.info(day.getSecondSolution());
    }

    @Override
    public Number getSecondSolution() {
        List<String> lines = fileManager.parseLines(fileName);
        List<String> incompleteLines = lines.stream()
                .filter(line -> getCorruptedScore(line) == 0)
                .collect(Collectors.toList());
        List<Long> incompleteScores = incompleteLines.stream()
                .mapToLong(line -> getIncompleteScore(line))
                .boxed()
                .sorted()
                .collect(Collectors.toList());
        int size = incompleteLines.size();
        int halfIndex = (incompleteLines.size()-1)/2;
        Long score = incompleteScores.get((incompleteLines.size()-1)/2);
        return score;
        
    }

    private Long getIncompleteScore(String line) {
        char[] charArray = line.toCharArray();
        Stack<Character> stack = new Stack<>();
        
        for (int i = 0; i < line.length(); i++) {
            Character current = charArray[i];
            if (stack.size() == 0) {
                stack.add(current);
            } else if (Symbol.isOpen(current) != null) {
                stack.add(current);
            } else if (Symbol.isClose(current) != null) {
                stack.pop();
            }
        }
        long sum = 0;
        List<Symbol> found = new ArrayList<>();
        while (!stack.empty()) {
            Character character = stack.pop();
            Symbol symbol = Symbol.isOpen(character);
            sum = sum * 5 + symbol.incompletePoint;
        }

        return sum;
    }


    @Override
    public Number getFirstSolution() {
        List<String> lines = fileManager.parseLines(fileName);
        return lines.stream().mapToInt(line -> getCorruptedScore(line)).sum();
    }
    
    private Integer getCorruptedScore(String line) {
        char[] charArray = line.toCharArray();
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < line.length(); i++) {
            Character current = charArray[i];
            if (stack.size() == 0) {
                stack.add(current);
            } else if (Symbol.isClose(current) != null) {
                Symbol symbol = Symbol.isClose(current);
                Character last = stack.pop();
                if (last != symbol.open) {
                    return symbol.corruptPoint;
                }
            } else {
                stack.add(current);
            }
        }
        return 0;
    }
    


    static enum Symbol {
        ROUNDY('(', ')', 3, 1),
        BRACKET('[', ']', 57, 2),
        CURLY('{', '}', 1197, 3),
        POINTY('<', '>', 25137, 4);

        Character open;
        Character close;
        Integer corruptPoint;
        Integer incompletePoint;

        Symbol(Character open, Character close, Integer point, Integer incompletePoint) {
            this.open = open;
            this.close = close;
            this.corruptPoint = point;
            this.incompletePoint = incompletePoint;
        }
        
        public static Symbol isClose(Character character) {
            for (Symbol symbol : Symbol.values()) {
                if (symbol.close == character) {
                    return symbol;
                }
            }
            return null;
        }

        public static Symbol isOpen(Character character) {
            for (Symbol symbol : Symbol.values()) {
                if (symbol.open == character) {
                    return symbol;
                }
            }
            return null;
        }
    }
    
    


    

    @Override
    public int getDay() {
        return DAY;
    }

   


}
