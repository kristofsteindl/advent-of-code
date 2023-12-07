package com.ksteindl.adventofcode.advent2022;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Day5CrateCrane extends Puzzle2022{

    private List<Stack<Character>> stacks = new ArrayList<>();
    private int maxHeight;
    private int width;

    public static void main(String[] args) {
        new Day5CrateCrane().printSolutions();
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        initStack(lines);
        for (int i = maxHeight + 2; i < lines.size(); i++) {
            reArrangeColumn9001(lines.get(i));
        }
        StringBuilder tops = new StringBuilder("");
        stacks.forEach(column -> tops.append(column.peek()));
        System.out.println(tops);
        return stacks.size();
    }

    private void reArrangeColumn9001(String line) {
        String[] commands = line.split(" ");
        int pieces = Integer.parseInt(commands[1]);
        int fromColumnNr = Integer.parseInt(commands[3]);
        int toColumnNr = Integer.parseInt(commands[5]);
        Stack<Character> fromColumn = stacks.get(fromColumnNr - 1);
        Stack<Character> toColumn = stacks.get(toColumnNr - 1);
        Stack<Character> tempStack = new Stack<>();
        for (int i = 0; i < pieces; i++) {
            tempStack.add(fromColumn.pop());
            
        }
        for (int i = 0; i < pieces; i++) {
            toColumn.add(tempStack.pop());
        }
    }

    @Override
    protected Number getFirstSolution(List<String> lines) {
        initStack(lines);
        for (int i = maxHeight + 2; i < lines.size(); i++) {
            reArrangeColumn(lines.get(i));
        }
        StringBuilder tops = new StringBuilder("");
        stacks.forEach(column -> tops.append(column.peek()));
        System.out.println(tops);
        return stacks.size();
    }
    
    private void reArrangeColumn(String line) {
        String[] commands = line.split(" ");
        int pieces = Integer.parseInt(commands[1]);
        int fromColumnNr = Integer.parseInt(commands[3]);
        int toColumnNr = Integer.parseInt(commands[5]);
        Stack<Character> fromColumn = stacks.get(fromColumnNr - 1);
        Stack<Character> toColumn = stacks.get(toColumnNr - 1);
        for (int i = 0; i < pieces; i++) {
            toColumn.add(fromColumn.pop());
        }
    }
    
    private void initStack(List<String> lines) {
        stacks = new ArrayList<>();
        int i = 0;
        while (!lines.get(i).equals("")) {
            i++;
        }
        maxHeight = i - 1;
        width = (lines.get(maxHeight).length() - 3) / 4 + 1;
        for (int j = 0; j < width; j++) {
            Stack<Character> column = new Stack<>();
            for (int k = maxHeight - 1; k >= 0; k--) {
                Character crate = lines.get(k).charAt(j * 4 + 1);
                if (crate != ' ') {
                    column.add(crate);
                }

            }
            stacks.add(column);
        }
    }

    @Override
    public int getDay() {
        return 5;
    }
}
