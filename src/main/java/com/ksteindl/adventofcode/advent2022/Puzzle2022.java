package com.ksteindl.adventofcode.advent2022;

import com.ksteindl.adventofcode.utils.FileManager;

import java.util.List;

public abstract class Puzzle2022 {

    protected static String FOLDER_NAME = "./src/main/resources/com/ksteindl/adventofcode/advent2022/";

    protected final FileManager fileManager = new FileManager();
    protected final List<String> inpuTestLines;
    protected final List<String> inputLines;
    
    public Puzzle2022() {
        String inputFileName = FOLDER_NAME + "inputDec" + getDay() + ".txt";
        String testInputFileName =FOLDER_NAME + "inputDec" + getDay() + "_test.txt";
        inpuTestLines = fileManager.parseLines(testInputFileName);
        inputLines = fileManager.parseLines(inputFileName);
    }
    
    public void printSolutions() {
        System.out.println("with test inputs:");
        System.out.println(getFirstSolution(inpuTestLines));
        System.out.println(getSecondSolution(inpuTestLines));
        System.out.println("with inputs:");
        System.out.println(getFirstSolution(inputLines));
        System.out.println(getSecondSolution(inputLines));
    }

    protected abstract Number getFirstSolution(List<String> lines);

    protected abstract Number getSecondSolution(List<String> lines);

    public abstract int getDay();

    public Number getFirstSolution() {
        return getFirstSolution(inputLines);
    }

    public Number getSecondSolution() {
        return getSecondSolution(inputLines);
    }
    
    
}
