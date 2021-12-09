package com.ksteindl.adventofcode.advent2021.day09;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class HeightMapAnalyzer extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(HeightMapAnalyzer.class);

    private static final int DAY = 9;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;
    List<Pair> lowPoints;
    List<List<Integer>> map;

    public HeightMapAnalyzer(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
        List<String> lines = fileManager.parseLines(fileName);
        map = lines.stream().map(line -> convertLine(line)).collect(Collectors.toList());
        lowPoints = new ArrayList<>();
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(0).size(); j++) {
                if (isLowPoint(i,j,map)) {
                    lowPoints.add(new Pair(i,j));
                }
            }
        }
    }

    public static void main(String[] args) {
        HeightMapAnalyzer day = new HeightMapAnalyzer(false);
        logger.info(day.getFirstSolution());
        logger.info(day.getSecondSolution());
    }

    @Override
    public Number getFirstSolution() {
        return lowPoints.stream().mapToInt(pair -> map.get(pair.i).get(pair.j)+1).sum();
    }

    @Override
    public Number getSecondSolution() {
        List<Set<Pair>> basins = new ArrayList<>();
        for (int k = 0; k < lowPoints.size(); k++) {
            Set<Pair> basin = new HashSet<>();
            basin.add(lowPoints.get(k));
            Set<Pair> prevExtensions = new HashSet<>();
            prevExtensions.add(new Pair(lowPoints.get(k).i, lowPoints.get(k).j));
            
            while (prevExtensions.size() > 0) {
                Set<Pair> nextRound = new HashSet<>();
                for (Pair newExt : prevExtensions) {
                    Set<Pair> newExtensions = getExtensions(newExt.i, newExt.j, map);
                    newExtensions.removeAll(basin);
                    basin.addAll(newExtensions);
                    nextRound.addAll(newExtensions);
                }
                prevExtensions = nextRound;
            }
            basins.add(basin);
        }
        basins.sort((list1, list2) -> list2.size() > list1.size() ? 1 : -1);
        return basins.get(0).size() * basins.get(1).size() * basins.get(2).size();
    }

    private Set<Pair> getExtensions(Integer i, Integer j, List<List<Integer>> map) {
        Set<Pair> extensions = new HashSet<>();
        if (i > 0 && map.get(i - 1).get(j) < 9) {
            extensions.add(new Pair(i - 1, j));
        }
        if (j < map.get(0).size()-1 && map.get(i).get(j+1) < 9) {
            extensions.add(new Pair(i, j+1));
        }
        if (i < map.size()-1 && map.get(i + 1).get(j) < 9) {
            extensions.add(new Pair(i + 1, j));
        }
        if (j > 0 && map.get(i).get(j-1) < 9) {
            extensions.add(new Pair(i, j - 1));
        }
        return extensions;
    }
    
    private boolean isLowPoint(Integer i,Integer j,List<List<Integer>> map) {
        Integer point = map.get(i).get(j);
        if (i > 0 && point >= map.get(i - 1).get(j)) {
            return false;
        }
        if (j < map.get(0).size()-1 && point >=  map.get(i).get(j + 1)) {
            return false;
        }
        if (i < map.size()-1 && point >=  map.get(i + 1).get(j)) {
            return false;
        }
        if (j > 0 && point >=  map.get(i).get(j - 1)) {
            return false;
        }
        return true;
    }
    
    private List<Integer> convertLine(String line) {
        char[] ch = line.toCharArray();
        List<Integer> list = new ArrayList<>();
        for (char c : ch) {
            list.add(Integer.parseInt("" + c));
        }
        return list;
    }
    

    @Override
    public int getDay() {
        return DAY;
    }
    
    static class Pair {
        Integer i;
        Integer j;

        public Pair(Integer i, Integer j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair pair = (Pair) o;

            if (i != null ? !i.equals(pair.i) : pair.i != null) return false;
            return j != null ? j.equals(pair.j) : pair.j == null;
        }

        @Override
        public int hashCode() {
            int result = i != null ? i.hashCode() : 0;
            result = 31 * result + (j != null ? j.hashCode() : 0);
            return result;
        }
    }

   


}
