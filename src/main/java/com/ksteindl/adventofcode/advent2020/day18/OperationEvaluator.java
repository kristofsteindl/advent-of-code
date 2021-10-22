package com.ksteindl.adventofcode.advent2020.day18;

import com.ksteindl.adventofcode.advent2020.Puzzle2020;
import com.ksteindl.adventofcode.advent2020.day18.component.AdvancedEvaluator;
import com.ksteindl.adventofcode.advent2020.day18.component.BasicEvaluator;
import com.ksteindl.adventofcode.advent2020.day18.component.Evaluator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class OperationEvaluator extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(OperationEvaluator.class);

    private static final int DAY = 18;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;

    public OperationEvaluator(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    @Override
    public Number getFirstSolution() {
        return getSumOfResultingValues(new BasicEvaluator());
    }

    @Override
    public Number getSecondSolution() {
        return getSumOfResultingValues(new AdvancedEvaluator());
    }

    @Override
    public int getDay() {
        return DAY;
    }

    private Long getSumOfResultingValues(Evaluator evaluator) {
        List<String> lines = fileManager.parseLines(fileName);
        return lines.stream().mapToLong(line -> calculateLineAndLogResult(line, evaluator)).sum();
    }

    private Long calculateLineAndLogResult(String line, Evaluator evaluator) {
        String[] wholeExpression = line.split(" ");
        Long result = evaluator.calculateElements(wholeExpression);
        logger.debug("result of the line: " + result);
        return  result;
    }




    /*
    *
    * References
    *
    * */
}
