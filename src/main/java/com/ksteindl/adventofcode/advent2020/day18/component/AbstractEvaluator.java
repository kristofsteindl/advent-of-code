package com.ksteindl.adventofcode.advent2020.day18.component;

import com.ksteindl.adventofcode.advent2020.day18.model.Processed;

import java.util.stream.IntStream;

public abstract class AbstractEvaluator implements Evaluator{

    protected Processed getProcessedUnit(String[] elements) {
        String firstELement = elements[0];
        if (firstELement.charAt(0) == '(') {
            int endParentIndex = getEndParentIndex(elements);
            elements[0] = elements[0].substring(1);
            elements[endParentIndex] = elements[endParentIndex].substring(0, elements[endParentIndex].length() - 1);
            String[] elementsInsideParentheris = IntStream.range(0, endParentIndex + 1)
                    .mapToObj(i -> elements[i])
                    .toArray(String[]::new);
            return new Processed(calculateElements(elementsInsideParentheris), endParentIndex + 1);
        } else {
            return new Processed(Long.parseLong(elements[0]), 1);
        }
    }

    protected String[] getNextELementSubArray(String[] elements, int startIndex) {
        return getNextELementSubArray(elements, startIndex, elements.length - 1);
    }

    protected String[] getNextELementSubArray(String[] elements, int startIndex, int endIndex) {
        return IntStream.range(startIndex, endIndex + 1)
                .mapToObj(i -> elements[i])
                .toArray(String[]::new);
    }

    protected int getEndParentIndex(String[] elements) {
        return getEndParentIndex(elements, 0);
    }

    protected int getEndParentIndex(String[] elements, final int startIndex) {
        int openCount = 0;
        int closeCount = 0;
        int elementIndex = startIndex - 1;
        do {
            elementIndex++;
            String element = elements[elementIndex];
            for(Character character : element.toCharArray()) {
                if (character == '(') {
                    openCount++;
                } else if (character == ')') {
                    closeCount++;
                }
            }
        } while (openCount > closeCount);
        return elementIndex;
    }


}
