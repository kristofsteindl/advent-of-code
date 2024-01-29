package com.ksteindl.adventofcode.advent2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day15 extends Puzzle2023 {

    public static void main(String[] args) {
        new Day15().printSolutions();
    }
    
    private static class Box {
        Integer id;
        List<Lens> lenses = new ArrayList<>();

        public Box(Integer id) {
            this.id = id;
        }
    }
    
    private static class Lens {
        Integer focalLength;
        String label;

        public Lens(Integer focalLength, String label) {
            this.focalLength = focalLength;
            this.label = label;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Lens lens = (Lens) o;

            return label.equals(lens.label);
        }

        @Override
        public int hashCode() {
            return label.hashCode();
        }
    }


    @Override
    protected Number getSecondSolution(List<String> lines) {
        Map<Integer, Box> boxes = new HashMap<>();
        List<String> steps = Arrays.stream(lines.get(0).split(",")).collect(Collectors.toList());
        for(String step : steps) {
            if (step.contains("-")) {
                var label = step.substring(0, step.length() - 1);
                var hash = calcHash(label);
                Box box = boxes.get(hash);
                if (box != null) {
                    boxes.get(hash).lenses.remove(new Lens(1, label));
                }
            } else {
                var parts = step.split("=");
                var label = parts[0];
                var hash = calcHash(label);
                var newFocalLength = Integer.valueOf(parts[1]);
                Box box = boxes.get(hash);
                Lens lens = new Lens(newFocalLength, label);
                if (box != null) {
                    Optional<Lens> optLens = boxes.get(hash).lenses.stream()
                            .filter(lensInBox -> lensInBox.equals(lens))
                            .findAny();
                    if (optLens.isEmpty()) {
                        box.lenses.add(lens);
                    } else {
                        optLens.get().focalLength = newFocalLength;
                    }
                } else {
                    box = new Box(hash);
                    box.lenses.add(lens);
                    boxes.put(hash, box);
                }
                
            }
        }
        
        
        return boxes.values().stream().mapToInt(this::getPower).sum();
    }
    
    private int getPower(Box box) {
        return IntStream.range(0, box.lenses.size())
                .map(i -> (box.id + 1) * (i + 1) * box.lenses.get(i).focalLength)
                .sum();
    }
    
    @Override
    protected Number getFirstSolution(List<String> lines) {
        List<String> steps = Arrays.stream(lines.get(0).split(",")).collect(Collectors.toList());
        return steps.stream().mapToInt(this::calcHash).sum();
    }
    
    private int calcHash(String string) {
        int hash = 0;
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            hash += c;
            hash *= 17;
            hash %= 256;
        }
        return hash;
    }


    @Override
    public int getDay() {
        return 15;
    }
}
