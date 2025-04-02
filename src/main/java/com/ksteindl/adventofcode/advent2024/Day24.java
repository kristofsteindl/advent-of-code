package com.ksteindl.adventofcode.advent2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;

public class Day24 extends Puzzle2024 {

    private List<Operation> operations;
    private Map<String, Wire> wires;
    private Set<Wire> xWires;
    private Set<Wire> yWires;
    private Set<Wire> zWires;

    public static void main(String[] args) {
        new Day24().printSolutions();
    }
    
    private class Combinator {
        int size;
        int[] toBeSwapped = new int[8];
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        init(lines);
        Long xResult = calcResult(xWires);
        Long yResult = calcResult(yWires);
        for (int i = 0; i < Math.pow(operations.size(), 8); i++) {
            List<Operation> combinationSet = getCombinationSet(operations, operations.size(), i);
            if (xResult + yResult != calcResult(zWires)) {
                break;
            }
        }
        return 0;
    }

    private List<Operation> getCombinationSet(List<Operation> operations, int size, long i) {
        List<Operation> combinationSet = new ArrayList<>(operations);
        List<Integer> toBeSwapped = new ArrayList<>();
        for (int j = 0; j < 8; j++) {
            toBeSwapped.add((int) i % size);
            i /= size;
        }
        for (int j = 0; j < 8 / 2; j++) {
            for (int k = j + 1; k < 8; k++) {
                
            }
        }
        return null;
    }
    
    private static class Pair {
        Integer one;
        Integer other;
    }
    
    private long calcZResult(List<Operation> operations) {
        Long result = null;
        while (result == null) {
            iterate(operations);
            result = calcResult(zWires);
        }
        return result;
    }

    private void iterate(List<Operation> operations) {
        operations.forEach(operation -> {
            if (operation.op1.value != null && operation.op2.value != null) {
                operation.result.value = operation.operator.apply(
                        operation.op1.value,
                        operation.op2.value);
            }
        });
    }

    private Long calcResult(Set<Wire> wires) {
        long result = 0L;
        for (Wire wire : wires) {
            if (wire.value == null) {
                return null;
            }
            if (wire.value) {
                result += (long) Math.pow(2, Integer.parseInt(wire.label.substring(1)));
            }
        }
        return result;
    }
    
    private void reset() {
        zWires.forEach(zWire -> zWire.value = null);
        wires.values().stream()
                .filter(wire -> !wire.label.startsWith("x") && !wire.label.startsWith("y"))
                .forEach(zWire -> zWire.value = null);
    }

    private void init(List<String> lines) {
        xWires = new HashSet<>();
        yWires = new HashSet<>();
        zWires = new HashSet<>();
        wires = new HashMap<>();
        int i = 0;
        while (!lines.get(i).isEmpty()) {
            String line = lines.get(i++);
            Wire wire = new Wire(line.substring(0, 3));
            wire.value = line.charAt(5) == '1';
            wires.put(wire.label, wire);
            if (wire.label.startsWith("x")) {
                xWires.add(wire);
            } else {
                yWires.add(wire);
            }
        }
        i++;
        operations = new ArrayList<>();
        while (i < lines.size()) {
            String[] split = lines.get(i++).split(" ");
            Wire op1 = wires.computeIfAbsent(split[0], w -> new Wire(split[0]));
            Wire op2 = wires.computeIfAbsent(split[2], w -> new Wire(split[2]));
            Wire result = wires.computeIfAbsent(split[4], w -> new Wire(split[4]));
            Operation operation = new Operation(op1, op2, getOperator(split[1]), result);
            operations.add(operation);
            if (result.label.startsWith("z")) {
                zWires.add(result);
            }
        }
        operations = List.copyOf(operations);
    }

    private BinaryOperator<Boolean> getOperator(String string) {
        return switch (string) {
            case "AND" -> (op1, op2) -> op1 & op2;
            case "OR" -> (op1, op2) -> op1 | op2;
            case "XOR" -> (op1, op2) -> op1 ^ op2;
            default -> throw new IllegalArgumentException();
        };
    }

    @Override
    protected Number getFirstSolution(List<String> lines) {
        init(lines);
        return calcZResult(operations);
    }

    @Override
    public int getDay() {
        return 24;
    }

    private static class Wire {
        String label;
        Boolean value;

        public Wire(String label) {
            this.label = label;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;

            Wire wire = (Wire) object;
            return label.equals(wire.label);
        }

        @Override
        public int hashCode() {
            return label.hashCode();
        }
    }

    private static class Operation {
        Wire op1;
        Wire op2;
        BinaryOperator<Boolean> operator;
        Wire result;

        public Operation(Wire op1, Wire op2, BinaryOperator<Boolean> operator, Wire result) {
            this.op1 = op1;
            this.op2 = op2;
            this.operator = operator;
            this.result = result;
        }
    }
}
