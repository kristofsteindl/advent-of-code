package com.ksteindl.adventofcode.advent2021.day01;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import org.apache.logging.log4j.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LevelCalculator extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(LevelCalculator.class);

    private static final int DAY = 1;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;

    public LevelCalculator(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    @Override
    public Number getFirstSolution() {
        return getInceasingLevels(this::getIncreasingLevels1);
    }

    @Override
    public Number getSecondSolution() {
        return getInceasingLevels(this::getIncreasingLevels2);
    }
    
    private Integer getInceasingLevels(Function<List<Integer>, Integer> levelGetter) {
        List<Integer> levels = getLevels();
        return levelGetter.apply(levels);
    }
    
    private List<Integer> getLevels() {
        return  fileManager.parseLines(fileName).stream()
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toList());
    }
    
    private int getIncreasingLevels1(List<Integer> levels) {
        int increasing = 0;
        for (int i = 1; i < levels.size(); i++) {
            if (levels.get(i) > levels.get(i -1 )) {
                increasing ++;
            }
        }
        return increasing;
    }

    private int getIncreasingLevels2(List<Integer> levels) {
        int increasing = 0;
        int prevSum = levels.get(2) +  levels.get(1) + levels.get(0);
        for (int i = 3; i < levels.size(); i++) {
            int sum = levels.get(i - 2) +  levels.get(i - 1) + levels.get(i);
            if (sum > prevSum) {
                increasing ++;
            }
            prevSum = sum;
        }
        return increasing;
    }

    @Override
    public int getDay() {
        return DAY;
    }

   


}
