package com.ksteindl.adventofcode.advent2023;

import java.util.ArrayList;
import java.util.List;

public class Day3 extends Puzzle2023 {
    
    List<Integer> partNumbers = new ArrayList<>();
    List<Gear> gears = new ArrayList<>();

    public static void main(String[] args) {
        var puzzle = new Day3();
        puzzle.printSolutions();
    }
    
    class Gear {
        Integer one;
        Integer two;
        Integer row;
        Integer column;
        
        boolean isGear(int row, int column) {
            return this.row == row && this.column == column;
        }
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        partNumbers = new ArrayList<>();
        gears = new ArrayList<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            List<Integer> potentialNumbers = new ArrayList<>();
            for (int column = line.length() - 1; column >= 0; column--) {
                if (Character.isDigit(line.charAt(column))) {
                    potentialNumbers.add(Character.getNumericValue(line.charAt(column)));
                } else if (!potentialNumbers.isEmpty()) {
                    addGear(harvestNumber(potentialNumbers), row, column, lines);
                    potentialNumbers =  new ArrayList<>();
                }
                if (column == 0) {
                    addGear(harvestNumber(potentialNumbers), row, -1, lines);
                }
            }
        }
        return gears.stream().filter(gear -> gear.two != null).mapToInt(gear -> gear.one * gear.two).sum();
    }

    private void addGear(int potentialPartNumber, int row, int column, List<String> lines) {
        if (potentialPartNumber == 0) {
            return;
        }
        if (row > 0) {
            for (int j = column; j < (column + String.valueOf(potentialPartNumber).length() + 2); j++) {
                if (j >= 0 && j < lines.get(0).length()) {
                    handleGears(potentialPartNumber, row - 1, j, lines);
                }
            }
        }
        if (row < lines.size() - 1) {
            for (int j = column; j < (column + String.valueOf(potentialPartNumber).length()) + 2; j++) {
                if (j >= 0 && j < lines.get(0).length()) {
                    handleGears(potentialPartNumber, row + 1, j, lines);
                }
            }
        }
        if (column >= 0) {
            handleGears(potentialPartNumber, row, column, lines);
        }
        int rightEnd = column + String.valueOf(potentialPartNumber).length() + 1;
        if (rightEnd < lines.get(0).length()) {
            handleGears(potentialPartNumber, row, rightEnd, lines);
        }
    }
    
    private void handleGears(Integer potentialPartNumber, int potentialRow, int potentialColumn, List<String> lines) {
        char character = lines.get(potentialRow).charAt(potentialColumn);
        if ('*' == character) {
            var optGear = gears.stream()
                    .filter(gear -> gear.isGear(potentialRow, potentialColumn))
                    .findAny();
            if (optGear.isPresent()) {
                var gear = optGear.get();
                gear.two = potentialPartNumber;
            } else {
                var gear = new Gear();
                gear.row = potentialRow;
                gear.column = potentialColumn;
                gear.one = potentialPartNumber;
                gears.add(gear);
            }
            partNumbers.add(potentialPartNumber);
        }
    }
    
    @Override
    protected Number getFirstSolution(List<String> lines) {
        partNumbers = new ArrayList<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            List<Integer> potentialNumbers = new ArrayList<>();
            for (int column = line.length() - 1; column >= 0; column--) {
                if (Character.isDigit(line.charAt(column))) {
                    potentialNumbers.add(Character.getNumericValue(line.charAt(column)));
                } else if (!potentialNumbers.isEmpty()) {
                    addPartNumber(harvestNumber(potentialNumbers), row, column, lines);
                    potentialNumbers =  new ArrayList<>();
                }
                if (column == 0) {
                    addPartNumber(harvestNumber(potentialNumbers), row, -1, lines);
                }
            }
        }
        return partNumbers.stream().mapToInt(x -> x).sum();
    }
    
    private void addPartNumber(int potentialPartNumber, int row, int column, List<String> lines) {
        if (potentialPartNumber == 0) {
            return;
        }
//        partNumbers.add(potentialPartNumber);
        if (row > 0) {
            for (int j = column; j < (column + String.valueOf(potentialPartNumber).length() + 2); j++) {
                if (j >= 0 && j < lines.get(0).length()) {
                    char character = lines.get(row - 1).charAt(j);
                    if (!Character.isDigit(character) && '.' != character) {
                        partNumbers.add(potentialPartNumber);
                        return;
                    }
                }
            }
        }
        if (row < lines.size() - 1) {
            for (int j = column; j < (column + String.valueOf(potentialPartNumber).length()) + 2; j++) {
                if (j >= 0 && j < lines.get(0).length()) {
                    char character = lines.get(row + 1).charAt(j);
                    if (!Character.isDigit(character) && '.' != character) {
                        partNumbers.add(potentialPartNumber);
                        return;
                    }
                }
            }
        }
        if (column >= 0) {
            char character = lines.get(row).charAt(column);
            if (!Character.isDigit(character) && '.' != character) {
                partNumbers.add(potentialPartNumber);
                return;
            }
        }
        int rightEnd = column + String.valueOf(potentialPartNumber).length() + 1;
        if (rightEnd < lines.get(0).length()) {
            char character = lines.get(row).charAt(rightEnd);
            if (!Character.isDigit(character) && '.' != character) {
                partNumbers.add(potentialPartNumber);
            }
        }
    }
    
    
    private int harvestNumber(List<Integer> potentialNumbers) {
        int potentialNumber = 0;
        for (int i = 0; i < potentialNumbers.size(); i++) {
            potentialNumber += potentialNumbers.get(i) * (int) Math.pow(10, i);
        }
        return potentialNumber;
    }



    @Override
    public int getDay() {
        return 3;
    }
}
