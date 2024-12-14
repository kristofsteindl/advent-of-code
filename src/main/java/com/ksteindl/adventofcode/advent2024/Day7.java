package com.ksteindl.adventofcode.advent2024;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day7 extends Puzzle2024 {

    BinaryOperator<Long> ADD = (a, b) -> a + b;
    BinaryOperator<Long> MULTIPLY = (a, b) -> a * b;
    BinaryOperator<Long> CONCAT = (a, b) -> Long.parseLong(a.toString() + b.toString());

    List<BinaryOperator<Long>> operators;

    public static void main(String[] args) {
        new Day7().printSolutions();
    }

    private static class Equation {
        long result;
        List<Long> operands;

        public Equation(long result, List<Long> operands) {
            this.result = result;
            this.operands = operands;
        }
    }

    @Override
    protected Number getFirstSolution(List<String> lines) {
        operators = List.of(ADD, MULTIPLY);
        return lines.stream()
                .mapToLong(this::calcTestLine)
                .sum();
    }

    private long calcTestLine(String line) {
        Equation equation = parseEquation(line);
        int enumNumber = (int) Math.pow(operators.size(), equation.operands.size() - 1);
        for (int enumeration = 0; enumeration < enumNumber; enumeration++) {
            long result = equation.operands.get(0);
            for (int j = 0; j < equation.operands.size() - 1; j++) {
                result = getOperation(j, enumeration).apply(result, equation.operands.get(j + 1));
            }
            if (result == equation.result) {
                return equation.result;
            }
        }
        return 0;
    }

    private BinaryOperator<Long> getOperation(int index, int enumeration) {
        int operatorSize = operators.size();
        int residue = enumeration / (int) Math.pow(operatorSize, index) % operatorSize;
        return operators.get(residue);
    }

    private Equation parseEquation(String line) {
        String[] stringEquation = line.split(":");
        List<Long> operators = Arrays.stream(stringEquation[1].substring(1).split(" "))
                .map(Long::parseLong).toList();
        return new Equation(Long.parseLong(stringEquation[0]), operators);
    }


    @Override
    protected Number getSecondSolution(List<String> lines) {
        operators = List.of(ADD, MULTIPLY, CONCAT);
        return lines.stream()
                .mapToLong(this::calcTestLine)
                .sum();
    }

    @Override
    public int getDay() {
        return 7;
    }
}
