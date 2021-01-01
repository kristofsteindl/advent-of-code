package com.ksteindl.adventofcode;

import com.ksteindl.adventofcode.utils.FileManager;

public abstract class Puzzle2020 {

    protected static String FOLDER_NAME = "./src/main/resources/com/ksteindl/adventofcode/advent2020/input/";

    protected final FileManager fileManager;
    protected final boolean isTest;

    public Puzzle2020(boolean isTest) {
        this.fileManager = new FileManager();
        this.isTest = isTest;
    }

    public abstract Object getFirstSolution();

    public abstract Object getSecondSolution();

    public abstract int getDay();
}
