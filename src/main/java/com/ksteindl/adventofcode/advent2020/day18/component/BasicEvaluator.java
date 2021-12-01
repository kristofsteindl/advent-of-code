package com.ksteindl.adventofcode.advent2020.day18.component;

import com.ksteindl.adventofcode.advent2020.day18.model.Processed;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BasicEvaluator extends AbstractEvaluator implements Evaluator{

    private static final Logger logger = LogManager.getLogger(BasicEvaluator.class);

    @Override
    public Long calculateElements(String[] elements) {
        Processed firstProcessed = getProcessedUnit(elements);
        long result = firstProcessed.getResult();
        for (int i = firstProcessed.getCount(); i < elements.length;) {
            char operand = elements[i++].charAt(0);
            Processed nextProcessed = getProcessedUnit(getNextELementSubArray(elements, i));
            if (operand == '*') {
                result *= nextProcessed.getResult();
            } else if (operand == '+') {
                result += nextProcessed.getResult();
            }
            i += nextProcessed.getCount();
        }
        return result;
    }

}
