package com.ksteindl.adventofcode;

import com.ksteindl.adventofcode.utils.FileManager;

public abstract class Puzzle {

    protected final FileManager fileManager;
    protected final boolean isTest;

    public Puzzle(boolean isTest) {
        this.fileManager = new FileManager();
        this.isTest = isTest;
    }

    public abstract Object getFirstSolution();

    public abstract Object getSecondSolution();

    public abstract int getDay();
}
