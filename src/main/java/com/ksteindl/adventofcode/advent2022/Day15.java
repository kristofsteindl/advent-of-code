package com.ksteindl.adventofcode.advent2022;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day15 extends Puzzle2022 {
    
    private int maxX = 0;
    private int maxY = 0;
    private int minX = 0;
    private int minY = 0;
    private List<Measure> measures;

    public static void main(String[] args) {
        new Day15().printSolutions();
    }
    
    @Override
    protected Number getFirstSolution(List<String> lines) {
        measures = lines.stream().map(this::getMeasure).collect(Collectors.toList());
        calcExtrenums(measures);
        int calcRowTest = 10;
        System.out.println("At row " + calcRowTest + ": " + calcRow(calcRowTest));
        return calcRow(2000000);
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        measures = lines.stream().map(this::getMeasure).collect(Collectors.toList());
        calcExtrenums(measures);
        int dimension = 20;
        for (int i = 0; i < dimension; i++) {
            RangeSet<Integer> rangeSet = getRangeSetForRow(i);
            rangeSet.add(Range.atMost(-1));
            rangeSet.add(Range.atLeast(dimension + 1));
            Range<Integer> complement = rangeSet.complement().span();
            if (!complement.isEmpty()) {
                int x = complement.lowerEndpoint();
                return x * 4000000 + i;
            }
        }
        
        return -1;
    }
    
    private int calcRow(int rowNo) {
        if (rowNo >= maxX) {
            return -1;
        }
        RangeSet<Integer> rangeSet = getRangeSetForRow(rowNo);
        int count = 0;
        for (int i = minX; i <= maxX; i++) {
            if (rangeSet.contains(i)) {
                count++;
            }
        }

        int beaconInThisRow = measures.stream()
                .filter(measure -> measure.beaconY == rowNo )
                .map(measure -> measure.beaconX)
                .collect(Collectors.toSet())
                .size();
        int sensorInThisRow = measures.stream()
                .filter(measure -> measure.sensorY == rowNo)
                .map(measure -> measure.sensorX)
                .collect(Collectors.toSet())
                .size();
        return count - beaconInThisRow - sensorInThisRow;
        //###S#############.###########
        //####B######################
    }

    private RangeSet<Integer> getRangeSetForRow(int rowNo) {
        RangeSet<Integer> rangeSet = TreeRangeSet.create();
        List<Section> sections = new ArrayList<>();
        for (Measure measure : measures) {
            int x = measure.sensorX;
            int distance = measure.getManDist();
            int z = distance - Math.abs(measure.sensorY - rowNo);
            if (z >= 0) {
                sections.add(new Section(x - z, x + z));
                rangeSet.add(Range.closed(x - z, x + z));
            }
        }
        return rangeSet;
    }
    
    
    private void calcExtrenums(List<Measure> measures) {
        for (Measure measure : measures) {
            int possMaxX = measure.sensorX + measure.getManDist();
            if (possMaxX > maxX) {
                maxX = possMaxX;
            }
            int possMaxY = measure.sensorY + measure.getManDist();
            if (possMaxY > maxY) {
                maxY = possMaxY;
            }
            int possMinX = measure.sensorX - measure.getManDist();
            if (possMinX < minX) {
                minX = possMinX;
            }
            int possMinY = measure.sensorY - measure.getManDist();
            if (possMinY < minY) {
                minY = possMinY;
            }
        }
    }
    
    private Measure getMeasure (String line) {
        Measure measure = new Measure();
        String[] afterEquals = line.split("=");
        measure.sensorX = Integer.parseInt(afterEquals[1].split(",")[0]);
        measure.sensorY = Integer.parseInt(afterEquals[2].split(":")[0]);
        measure.beaconX = Integer.parseInt(afterEquals[3].split(",")[0]);
        measure.beaconY = Integer.parseInt(afterEquals[4].split(",")[0]);
        return measure;
    }
    
    static class Section implements Comparable {
        Integer start;
        Integer end;

        public Section(Integer start, Integer end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public int compareTo(Object o) {
            Section section2 = (Section) o;
            return this.start.compareTo(section2.start);
        }
    }
    
    static class Measure {
        Integer sensorX;
        Integer sensorY;
        Integer beaconX;
        Integer beaconY;
        
        public Integer getManDist() {
            return Math.abs(sensorX - beaconX) + Math.abs(sensorY - beaconY);
        }
    }

    @Override
    public int getDay() {
        return 15;
    }
}
