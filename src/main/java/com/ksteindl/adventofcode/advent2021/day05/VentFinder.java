package com.ksteindl.adventofcode.advent2021.day05;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import com.ksteindl.adventofcode.advent2021.day04.SquidBingo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class VentFinder extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(VentFinder.class);

    private static final int DAY = 5;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;
    private Map<Point, AtomicInteger> map;

    public VentFinder(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    public static void main(String[] args) {
        VentFinder finder = new VentFinder(false);
        logger.info(finder.getFirstSolution());
        logger.info(finder.getSecondSolution());
    }

    @Override
    public Number getFirstSolution() {
        return getSolution(false);
    }

    @Override
    public Number getSecondSolution() {
        return getSolution(true);
    }
    
    private Integer getSolution(boolean diagonal) {
        List<String> lines = fileManager.parseLines(fileName);
        map = new HashMap<>();
        for (String line : lines) {
            processLine(line, diagonal);
        }
        map.entrySet().stream().filter(entry -> entry.getValue().get() > 1).forEach(entry -> logger.debug(entry.getKey()));
        int finalScore = (int) map.values().stream().filter(score -> score.get() > 1).count();
        return finalScore;
    }
    
    private void processLine(String line, boolean diagonal) {
        String[] components = line.split(" -> ");
        String start = components[0];
        String end = components[1];
        int x1 = Integer.parseInt(start.split(",")[0]);
        int y1 = Integer.parseInt(start.split(",")[1]);
        int x2 = Integer.parseInt(end.split(",")[0]);
        int y2 = Integer.parseInt(end.split(",")[1]);
        if (x1 == x2) {
            for (int y = Math.min(y1, y2); y < Math.max(y1, y2) + 1; y++) {
                Point point = new Point(x1, y);
                add(point);
            }
        } else if (y1 == y2) {
            for (int x = Math.min(x1, x2); x < Math.max(x1, x2) + 1; x++) {
                Point point = new Point(x, y1);
                add(point);
            }
        } else if (diagonal) {
            if (x1 != x2 && y1 != y2 && ((x1 - x2) / (y1 - y2)) == 1.0000) {
                for (int i = 0; i < Math.abs(x1 - x2) + 1; i++) {
                    Point point = new Point(Math.min(x1, x2) + i, Math.min(y1, y2) + i);
                    add(point);
                }
            } else if (x1 != x2 && y1 != y2 && ((x1 - x2) / (y1 - y2)) == -1.0000) {
                for (int i = 0; i < Math.abs(x1 - x2) + 1; i++) {
                    Point point = new Point(Math.min(x1, x2) + i, Math.max(y1, y2) - i);
                    add(point);
                }
            }
        }
    }
    
    private void add(Point point) {
        if (map.get(point) == null) {
            AtomicInteger score = new AtomicInteger(0);
            map.put(point, score);
        }
        map.get(point).addAndGet(1);
    }
    
    
    @Override
    public int getDay() {
        return DAY;
    }

}
