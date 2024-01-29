package com.ksteindl.adventofcode.advent2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 extends Puzzle2023{

    public static void main(String[] args) {
        new Day12().printSolutions();
    }
    
    @Override
    protected Number getFirstSolution(List<String> lines) {
        return lines.stream()
                .map(Row::new)
                .mapToLong(Row::countVariation)
                .sum();
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        return lines.stream()
//                .map(this::unfoldLine)
                .map(Row::new)
                .mapToLong(Row::countVariation)
                .sum();
    }
    
    private String unfoldLine(String line) {
        var parts = line.split(" ");
        var sb = new StringBuilder(parts[0]);
        for (int i = 0; i < 4; i++) {
            sb.append("?");
            sb.append(parts[0]);
        }
        sb.append(" ").append(parts[1]);
        for (int i = 0; i < 4; i++) {
            sb.append(",");
            sb.append(parts[1]);
        }
        return sb.toString();
    }
    
    private static class Row {
        List<Integer> damagedGroups;
        String row;
        List<Integer> operationals = new ArrayList<>();
        List<Integer> damaged = new ArrayList<>();
        List<Integer> unknowns = new ArrayList<>();

        Row() {}
        
        Row(String line) {
            String[] splitted = line.split(" ");
            this.row = splitted[0];
            this.damagedGroups = Arrays.stream(splitted[1].split(",")).mapToInt(Integer::valueOf).boxed().collect(Collectors.toList());
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '.') {
                    operationals.add(i);
                }
                if (line.charAt(i) == '#') {
                    damaged.add(i);
                }
                if (line.charAt(i) == '?') {
                    unknowns.add(i);
                }
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < row.length(); i++) {
                sb.append(unknowns.contains(i) ? '?' : damaged.contains(i) ? '#' : '.');
            }
            return sb.toString();
        }

        static Row copyRow(Row row) {
            var copied = new Row();
            copied.row = row.row;
            copied.operationals = new ArrayList<>(row.operationals);
            copied.damaged = new ArrayList<>(row.damaged);
            copied.unknowns = new ArrayList<>(row.unknowns);
            copied.damagedGroups = row.damagedGroups;
            return copied;
        }
        
        static long countVariation(Row row) {
            List<Row> enumeratedRows = enumareteRows(row);
            long sum = 0;
            for (int i = 0; i < enumeratedRows.size(); i++) {
                Row enumerated = enumeratedRows.get(i);
                if (isValid(enumerated)) {
                    sum++;
                }
            }
            return sum;
        }
        
        static boolean isValid(Row row) {
            int damagedCount = 0;
            int actualDamageGroupIndex = 0;
            int i = 0;
            while (i < row.row.length()) {
                if (row.damaged.contains(i)) {
                    if (actualDamageGroupIndex == row.damagedGroups.size()) {
                        return false;
                    }
                    damagedCount++;
                }
                else if (damagedCount > 0) {
                    if (damagedCount != row.damagedGroups.get(actualDamageGroupIndex)) {
                        return false;
                    } else {
                        damagedCount = 0;
                        actualDamageGroupIndex++;
                    }
                }
                i++;
            }
            if (actualDamageGroupIndex == row.damagedGroups.size()) {
                return true;
            } else if (damagedCount != 0) {
                if (actualDamageGroupIndex != row.damagedGroups.size() - 1) {
                    return false;
                }
                var lastDamagedGroupNumber = row.damagedGroups.get(actualDamageGroupIndex);
                if (damagedCount != lastDamagedGroupNumber) {
                    return false;
                }
                return true;
            }
            return false;
        }

        static boolean isPossibleValid(Row row) {
            List<Integer> daunList = Stream
                    .concat(row.damaged.stream(), row.unknowns.stream())
                    .sorted()
                    .collect(Collectors.toList());
            int damagedGroupIndex = 0;
            Integer prevDaunIndex = daunList.get(0);
            while (damagedGroupIndex < row.damagedGroups.size()) {
                Integer damagedGroupSize = row.damagedGroups.get(damagedGroupIndex);
                int i = 1;
                while (i < daunList.size() && daunList.get(i) - prevDaunIndex == 1 && i > damagedGroupSize) {
                    prevDaunIndex = daunList.get(i);
                    i++;
                }
                if (i == damagedGroupSize) {
                    damagedGroupIndex++;
                }
                
                
            }
            return damagedGroupIndex == row.damagedGroups.size();
        }

        private static List<Row> enumareteRows(Row thisRow) {
            List<Row> feed = new ArrayList<>();
            feed.add(thisRow);
            List<Row> enumerated = null;
            for (int i = 0; i < thisRow.unknowns.size(); i++) {
                enumerated = new ArrayList<>();
                for (int j = 0; j < feed.size(); j++) {
                    Row row = feed.get(j);

                    var one = copyRow(row);
                    int index = one.unknowns.remove(0);
                    one.operationals.add(index);
                    one.operationals.sort(Integer::compareTo);

                    var two = copyRow(row);
                    two.unknowns.remove(0);
                    two.damaged.add(index);
                    two.operationals.sort(Integer::compareTo);

                    if (isPossibleValid(one)) {
                        enumerated.add(one);
                    }
                    if (isPossibleValid(two)) {
                        enumerated.add(two);
                    }
                }
                feed = enumerated;

            }
            return enumerated;
        }
    }

    @Override
    public int getDay() {
        return 12;
    }
}
