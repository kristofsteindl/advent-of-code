package com.ksteindl.adventofcode.advent2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day5 extends Puzzle2024 {

    private record Relation(Integer smaller, Integer bigger) {
        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;

            Relation relation = (Relation) object;
            return smaller.equals(relation.smaller) && bigger.equals(relation.bigger);
        }

        @Override
        public int hashCode() {
            int result = smaller.hashCode();
            result = 31 * result + bigger.hashCode();
            return result;
        }
    }

    public static void main(String[] args) {
        var day = new Day5();
        day.printSolutions();
    }

    private Set<Relation> relations;
    private List<List<Integer>> updates;

    @Override
    protected Number getSecondSolution(List<String> lines) {
        initUpdatesAndRelations(lines);
        List<Integer> orderedList = getOrderedList();
        return updates.stream()
                .filter(u -> !isCorrect(u))
                .map(update -> correctOrder(update, orderedList))
                .mapToInt(update -> update.get(update.size() / 2))
                .sum();
    }
    
    private List<Integer> getOrderedList() {
        return relations.stream()
                .collect(Collectors.groupingBy(rel -> rel.smaller, Collectors.counting()))
                .entrySet().stream()
                .sorted(Comparator.comparingLong(Map.Entry::getValue))
//                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .map(Map.Entry::getKey)
                .toList();
        
    }
    
    private List<Integer> correctOrder(List<Integer> update, List<Integer> orderedList) {
        return orderedList.stream()
                .filter(update::contains)
                .toList();
    }

    @Override
    protected Number getFirstSolution(List<String> lines) {
        initUpdatesAndRelations(lines);
        return updates.stream()
                .filter(this::isCorrect)
                .mapToInt(update -> update.get(update.size() / 2))
                .sum();
    }
    
    private void initUpdatesAndRelations(List<String> lines) {
        int i = 0;
        relations = new HashSet<>();
        while (!lines.get(i).isEmpty()) {
            String[] sRels = lines.get(i).split("\\|");
            Integer smaller = Integer.parseInt(sRels[0]);
            Integer bigger = Integer.parseInt(sRels[1]);
            relations.add(new Relation(smaller, bigger));
            i++;
        }
        i++;
        updates = new ArrayList<>();
        while (i < lines.size()) {
            updates.add(Arrays.stream(lines.get(i).split(",")).map(Integer::parseInt).toList());
            i++;
        }
    }

    private boolean isCorrect(List<Integer> update) {
        for (int i = 0; i < update.size(); i++) {
            Integer current = update.get(i);
            for (int j = 0; j < i; j++) {
                if (relations.contains(new Relation(current, update.get(j)))) {
                    return false;
                }
            }
            for (int j = i + 1; j < update.size(); j++) {
                if (relations.contains(new Relation(update.get(j), current))) {
                    return false;
                }
            }
        }
        return true;

    }



    @Override
    public int getDay() {
        return 5;
    }
}
