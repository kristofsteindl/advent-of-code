package com.ksteindl.adventofcode.advent2021.day15;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import com.ksteindl.adventofcode.advent2021.day14.Polymerizator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ChitonAvoider extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(ChitonAvoider.class);

    private static final int DAY = 15;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;
    final Integer[][] cave;
    final Integer height;
    final Integer width;

    public ChitonAvoider(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
        List<String> lines = fileManager.parseLines(fileName);
        cave = new Integer[lines.size()][lines.get(1).length()];
        for (int i = 0; i < lines.size(); i++) {
            String row = lines.get(i);
            for (int j = 0; j < row.length(); j++) {
                cave[i][j] = Integer.parseInt("" + row.charAt(j));
            }
        }
        height = cave.length;
        width = cave[0].length;
    }

    public static void main(String[] args) {
        ChitonAvoider day = new ChitonAvoider(false);
        logger.info(day.getFirstSolution());
        logger.info(day.getSecondSolution());
    }

    @Override
    public Number getFirstSolution() {
        List<Route> terminals = new ArrayList<>();
        Route start = new Route(cave.length, cave[0].length);
        List<Route> prevRoutes = new ArrayList<>();
        prevRoutes.add(start);
        for (int i = 0; i < cave.length + cave[0].length - 2; i++) {
            List<Route> nextRoutes = new ArrayList<>();
            for (Route route : prevRoutes) {
                if (route.headColumn < route.map[0].length - 1) {
                    Route next = route.cloned();
                    next.mark(next.headRow, next.headColumn + 1);
                    next.headColumn = next.headColumn + 1;
                    nextRoutes.add(next);
                }
                if (route.headRow < route.map.length - 1) {
                    Route next = route.cloned();
                    next.mark(next.headRow + 1, next.headColumn);
                    next.headRow = next.headRow + 1;
                    nextRoutes.add(next);
                }
            }
            prevRoutes = nextRoutes;
        }
        return -1;
    }
    
    private static class Route {
        boolean[][] map;
        Integer headRow = 0;
        Integer headColumn = 0;

        public Route(int height, int width) {
            map = new boolean[height][width];
            map[0][0] = true; 
        }

        public Route(boolean[][] map) {
            this.map = map;
        }

        void mark(int row, int column) {
            map[row][column] = true;
        }

        List<Route> expand() {
            List<Route> expandedRoutes = new ArrayList<>();
            //LEFT
            if (headColumn > 0 && !map[headRow][headColumn - 1]) {
                Route expanded = cloned();
                expanded.mark(headRow, headColumn - 1);
                expanded.headColumn = headColumn - 1;
                expandedRoutes.add(expanded);
            }
            //UP
            if (headRow > 0 && !map[headRow - 1][headColumn]) {
                Route expanded = cloned();
                expanded.mark(headRow - 1, headColumn);
                expanded.headRow = headRow - 1;
                expandedRoutes.add(expanded);
            }
            //RIGHT
            if (headColumn < map[0].length - 1 
                    && !map[headRow][headColumn + 1]) {
                Route expanded = cloned();
                expanded.mark(headRow, headColumn + 1);
                expanded.headColumn = headColumn + 1;
                expandedRoutes.add(expanded);
            }
            //DOWN
            if (headRow < map.length - 1 && !map[headRow + 1][headColumn]) {
                Route expanded = cloned();
                expanded.mark(headRow + 1, headColumn);
                expanded.headRow = headRow + 1;
                expandedRoutes.add(expanded);
            }
            return expandedRoutes;
        }
        
        public Route cloned() {
            boolean[][] clonedMap = new boolean[map.length][map[0].length];
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    if (map[i][j]) {
                        clonedMap[i][j] = true;    
                    }
                }
            }
            Route clonedRoute = new Route(clonedMap);
            clonedRoute.headRow = headRow;
            clonedRoute.headColumn = headColumn;
            return clonedRoute;
        }
    }

    @Override
    public Number getSecondSolution() {
        return getDay();
    }
    

    @Override
    public int getDay() {
        return DAY;
    }

   


}
