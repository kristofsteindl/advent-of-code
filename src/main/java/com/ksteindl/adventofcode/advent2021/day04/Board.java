package com.ksteindl.adventofcode.advent2021.day04;

import java.util.*;
import java.util.stream.Collectors;

public class Board {
    
    List<List<Integer>> rows = new ArrayList<>();
    Set<Integer> numbers = new HashSet<>();
    Integer winningNumber;

    public Board(List<String> lines) {
        for (String line : lines) {
            List<Integer> row = Arrays.stream(line.split(" "))
                    .map(string -> string.trim())
                    .filter(string -> !string.equals(""))
                    .map(string -> Integer.parseInt(string))
                    .collect(Collectors.toList());
            rows.add(row);
        }
    }
    
    
    public void play(Integer number) {
        numbers.add(number);
        for (int i = 0; i < rows.size(); i++) {
            Set<Integer> row = new HashSet<>(rows.get(i));
            if (numbers.containsAll(row)) {
                winningNumber = number;
            }
        }
        for (int i = 0; i < rows.get(0).size(); i++) {
            Set<Integer> column = new HashSet<>();
            for (int j = 0; j < rows.size(); j++) {
                column.add(rows.get(j).get(i));
            }
            if (numbers.containsAll(column)) {
                winningNumber = number;
            }
        }
    }
    
    
    public Integer getScore() {
        Integer sum = rows.stream().flatMap(row -> row.stream()).filter(number -> !numbers.contains(number)).mapToInt(i-> i).sum();
        return sum * winningNumber;
    }

    @Override
    public boolean equals(Object o) {
        Board board1 = (Board) o;
        for (int i = 0; i < rows.size(); i++) {
            for (int j = 0; j < rows.get(0).size(); j++) {
                if (board1.rows.get(i).get(j) != rows.get(i).get(j)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows);
    }
}
