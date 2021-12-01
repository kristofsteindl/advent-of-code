package com.ksteindl.adventofcode.advent2020.day18.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdvancedEvaluator extends AbstractEvaluator implements Evaluator{

    private static final Logger logger = LogManager.getLogger(AdvancedEvaluator.class);

    @Override
    public Long calculateElements(final String[] elements) {
        String[] onlyNumberElements = elements.clone();
        int index;
        do {
            index = 0;
            while (index < onlyNumberElements.length && onlyNumberElements[index].charAt(0) != '('){
                index++;
            }
            if (index < onlyNumberElements.length) {
                int endIndex = getEndParentIndex(onlyNumberElements, index);
                onlyNumberElements[index] = onlyNumberElements[index].substring(1);
                onlyNumberElements[endIndex] = onlyNumberElements[endIndex].substring(0, onlyNumberElements[endIndex].length() - 1);
                onlyNumberElements = updateElements(
                        onlyNumberElements,
                        calculateElements(getNextELementSubArray(onlyNumberElements, index, endIndex)),
                        index ,
                        endIndex - index);
            }
        } while (index < onlyNumberElements.length);
        final String[] elementsAfterAddition = evaluateAdditionsBetweenNumbers(onlyNumberElements);
        long result = evaluateMultiplicationBetweenNumbers(elementsAfterAddition);
        return result;
    }


    private Long evaluateMultiplicationBetweenNumbers(final String[] elements) {
        long result = Long.parseLong(elements[0]);
        for (int i = 2; i < elements.length; i += 2) {
            result *= Long.parseLong(elements[i]);
        }
        return result;
    }

    private String[] evaluateAdditionsBetweenNumbers(final String[] elements) {
        String[] elementsAfterAddition = elements.clone();
        int index;
        do {
            index = 0;
            while (index < elementsAfterAddition.length && elementsAfterAddition[index].charAt(0) != '+'){
                index++;
            } if (index < elementsAfterAddition.length) {
                elementsAfterAddition = updateElements(
                        elementsAfterAddition,
                        Long.parseLong(elementsAfterAddition[index - 1]) + Long.parseLong(elementsAfterAddition[index + 1]),
                        index - 1 ,
                        2);
            }

        } while (index < elementsAfterAddition.length);
        return elementsAfterAddition;
    }

    private String[] updateElements(String[] elements, Long newValue, int startIndex, int sizeToShrink) {
        String[] updatedElements = new String[elements.length - sizeToShrink];
        for (int i = 0; i < startIndex; i++) {
            updatedElements[i] = elements[i];
        }
        updatedElements[startIndex] = newValue.toString();
        for (int i = startIndex + 1; i < elements.length - sizeToShrink; i++) {
            updatedElements[i] = elements[i + sizeToShrink];
        }
        return updatedElements;
    }


}
