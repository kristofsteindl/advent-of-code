package com.ksteindl.adventofcode.advent2022;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day3RucksackOrganizer extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(Day3RucksackOrganizer.class);

    private static final int DAY = 3;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;

    public Day3RucksackOrganizer(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    public static void main(String[] args) {
        Puzzle2021 day3 = new Day3RucksackOrganizer(true);
        System.out.println(day3.getFirstSolution());
        System.out.println(day3.getSecondSolution());
        Puzzle2021 day3NoTest = new Day3RucksackOrganizer(false);
        System.out.println("--------------------------");
        System.out.println(day3NoTest.getFirstSolution());
        System.out.println(day3NoTest.getSecondSolution());
    }


    @Override
    public Number getSecondSolution() {
        List<String> lines = fileManager.parseLines(fileName);
        int sum = 0;
        for (int i = 0; i < lines.size(); i = i + 3) {
            Set<Character> first = getCharSet(lines.get(i));
            Set<Character> second = getCharSet(lines.get(i+1));
            Set<Character> third = getCharSet(lines.get(i+2));
            first.retainAll(second);
            first.retainAll(third);
            sum += countPriority(first.iterator().next());
        }
        return sum;
    }

    @Override
    public Number getFirstSolution() {
        List<String> lines = fileManager.parseLines(fileName);
        return lines.stream().mapToInt(this::getLinePriority).sum();
    }
    
    private int getLinePriority(String line) {
        Set<Character> first = getCharSet(line.substring(0, line.length()/2));
        Set<Character> second = getCharSet(line.substring(line.length()/2, line.length()));
        first.retainAll(second);
        Character c = first.iterator().next();
        return countPriority(c);
    }
    
    private Set<Character> getCharSet(String line) {
        return line.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
    }
    
    private int countPriority(Character c) {
        return c > 90 ? c - 96 : c - 65 + 27;
    }

    

    @Override
    public int getDay() {
        return DAY;
    }

   


}
