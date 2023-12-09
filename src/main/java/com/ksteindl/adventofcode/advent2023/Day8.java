package com.ksteindl.adventofcode.advent2023;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day8 extends Puzzle2023{

    public static void main(String[] args) {
        new Day8().printSolutions();
    }
    private static class Node {
        String id;
        Node left;
        Node right;

        public Node(String id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            return id.equals(node.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }
    
    @Override
    protected Number getFirstSolution(List<String> lines) {
//        String instructions = lines.get(0);
//        Map<String, Node> nodes = lines.subList(2, lines.size()).stream()
//                .map(line -> new Node(line.split(" ")[0]))
//                .collect(Collectors.toMap(node -> node.id, node -> node));
//        for (int i = 2; i < lines.size(); i++) {
//            String line = lines.get(i);
//            var parts = line.split(" ");
//            Node parent = nodes.get(parts[0]);
//            parent.left = nodes.get(parts[2].substring(1, 4));
//            parent.right = nodes.get(parts[3].substring(0, 3));
//            
//        }
//        Node actual = nodes.get("AAA");
//        return reachZZZ(instructions, nodes, actual);
        return 0;
    }

    private long reachLastZ(String instructions, Map<String, Node> nodes) {
        List<Node> actuals = nodes.values().stream().filter(node -> node.id.charAt(2) == 'A').collect(Collectors.toList());
        List<Integer> numbers = actuals.stream().mapToInt(node -> reachZZZ(instructions, nodes, node)).boxed().collect(Collectors.toList());
        long one = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            one = lcm(one, (long) numbers.get(i));
        }
        return one;
    }

    public static long lcm(long number1, long number2) {
        if (number1 == 0 || number2 == 0) {
            return 0;
        }
        long absNumber1 = Math.abs(number1);
        long absNumber2 = Math.abs(number2);
        long absHigherNumber = Math.max(absNumber1, absNumber2);
        long absLowerNumber = Math.min(absNumber1, absNumber2);
        long lcm = absHigherNumber;
        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }
        return lcm;
    }
    
    private boolean allEndsWithZ(List<Node> actuals) {
        for (int i = 0; i < actuals.size(); i++) {
            if (!(actuals.get(i).id.charAt(2) == 'Z')) {
                return false;
            }
        }
        return true;
    }
    
    
    
    private int reachZZZ(String instructions, Map<String, Node> nodes, Node actual) {
        int steps = 0;
        while (!(actual.id.charAt(2) == 'Z')) {
            Character dir = instructions.charAt(steps % instructions.length());
            if (dir == 'R') {
                actual = actual.right;
            } else {
                actual = actual.left;
            }
            steps++;
        }
        return steps;
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        String instructions = lines.get(0);
        Map<String, Node> nodes = lines.subList(2, lines.size()).stream()
                .map(line -> new Node(line.split(" ")[0]))
                .collect(Collectors.toMap(node -> node.id, node -> node));
        for (int i = 2; i < lines.size(); i++) {
            String line = lines.get(i);
            var parts = line.split(" ");
            Node parent = nodes.get(parts[0]);
            parent.left = nodes.get(parts[2].substring(1, 4));
            parent.right = nodes.get(parts[3].substring(0, 3));

        }
        return reachLastZ(instructions, nodes);
    }

    @Override
    public int getDay() {
        return 8;
    }
}
