package com.ksteindl.adventofcode.codingchallenge2025.codingchallenge2021.task1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.ksteindl.adventofcode.codingchallenge2025.codingchallenge2021.CoCha2025;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Day2 extends CoCha2025 {

    public static void main(String[] args) {
        var challange = new Day2(false);
        System.out.println(challange.getFirstSolution());
    }

    @Override
    public Object getFirstSolution() {
        List<List<List<Tile>>> allRoutes = new ArrayList<>();
        List<String> lines = fileManager.parseLines(fileName);
        char[][] board = getBoard(lines);
        for (int startingPos = 0; startingPos < board[0].length; startingPos++) {
            List<List<Tile>> routesFromAStartingPos = getRoutesFromAStartingPos(startingPos, board);
            allRoutes.add(routesFromAStartingPos);
        }
        List<ValueHolder> valuesByRoute = allRoutes.stream()
                .flatMap(List::stream)
                .map(route -> new ValueHolder(route, calcVaule(route)))
                .toList();
//        int maxValue = valuesByRoute.entrySet()
//                .stream()
//                .sorted((entry1, entr2) -> entr2.getKey() - entry1.getKey())
//                .peek(System.out::println)
//                .mapToInt(entry -> entry.getValue())
//                .max().getAsInt();
        System.out.println(valuesByRoute.stream().filter(el -> el.value == 388).findAny().orElse(null));
        return valuesByRoute.stream().mapToInt(entry -> entry.value).max().getAsInt();
    }
    
    record ValueHolder(List<Tile> route, Integer value) {
        
    }
    
    int calcVaule(List<Tile> route) {
        int sumValue = 0;
        for (int i = 0; i < route.size(); i++) {
            Tile tile = route.get(i);
            char character = tile.value;
            int value = tile.value - 87;
            sumValue += value;
            if (i>0) {
                if (character == route.get(i-1).value) {
                    sumValue += value;
                }
            }
        }
        return sumValue;
    }

    private List<List<Tile>> getRoutesFromAStartingPos(int startingPos, char[][] board) {
        List<List<Tile>> routesFromAStartingPos = new ArrayList<>();
        Tile startingTile = new Tile(0, startingPos, board[0][startingPos]);
        List<Tile> firstRoute = new ArrayList<>();
        firstRoute.add(startingTile);
        routesFromAStartingPos.add(firstRoute);
        boolean iterationHappened = false;
        do {
            iterationHappened = iterate(routesFromAStartingPos, board);
        } while (iterationHappened);
        return routesFromAStartingPos.stream()
                .filter(routes -> routes.get(routes.size() - 1).row == board.length - 1)
                .toList();
    }

    private boolean iterate(List<List<Tile>> routesFromAStartingPos, char[][] board) {
        List<List<Tile>> afterIterationRoutes = new ArrayList<>();
        boolean iterationHappened = false;
        for (List<Tile> route : routesFromAStartingPos) {
            List<List<Tile>> enumeratedRoutes = enumerateRoutes(route, board);
            if (enumeratedRoutes.isEmpty()) {
                afterIterationRoutes.add(route);
            } else {
                iterationHappened = true;
                afterIterationRoutes.addAll(enumeratedRoutes);
            }
        }
        routesFromAStartingPos.clear();
        routesFromAStartingPos.addAll(afterIterationRoutes);
        return iterationHappened;
    }

    private List<List<Tile>> enumerateRoutes(List<Tile> route, char[][] board) {
        List<List<Tile>> newRoutes = new ArrayList<>();
        Tile lastTile = route.get(route.size() - 1);
        int baseRow = lastTile.row;
        int baseColumn = lastTile.col;
        //row +1, column -2
        int trialRow =  baseRow + 1;
        int trialColumn =  baseColumn - 2;
        if (trialRow < board.length && trialColumn >= 0) {
            List<Tile> newRoute = new ArrayList<>(route);
            newRoute.add(new Tile(trialRow, trialColumn, board[trialRow][trialColumn]));
            newRoutes.add(newRoute);
        }
        //row +1, column +2
        trialRow =  baseRow + 1;
        trialColumn =  baseColumn + 2;
        if (trialRow < board.length && trialColumn < board[0].length) {
            List<Tile> newRoute = new ArrayList<>(route);
            newRoute.add(new Tile(trialRow, trialColumn, board[trialRow][trialColumn]));
            newRoutes.add(newRoute);
        }
        //row +2, column -1 
        trialRow =  baseRow + 2;
        trialColumn =  baseColumn - 1;
        if (trialRow < board.length && trialColumn >= 0) {
            List<Tile> newRoute = new ArrayList<>(route);
            newRoute.add(new Tile(trialRow, trialColumn, board[trialRow][trialColumn]));
            newRoutes.add(newRoute);
        }
        //row +2, column +1 
        trialRow =  baseRow + 2;
        trialColumn =  baseColumn + 1;
        if (trialRow < board.length && trialColumn < board[0].length) {
            List<Tile> newRoute = new ArrayList<>(route);
            newRoute.add(new Tile(trialRow, trialColumn, board[trialRow][trialColumn]));
            newRoutes.add(newRoute);
        }
        return newRoutes;
    }

    private char[][] getBoard(List<String> lines) {
        char[][] board = new char[lines.size()][lines.get(0).length()];
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            for (int column = 0; column < line.length(); column++) {
                board[row][column] = line.charAt(column);
            }
        }
        System.out.println(toStringBoard(board));
        return board;
    }

    String toStringBoard(char[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < board.length; row++) {
            char[] line = board[row];
            for (int i = 0; i < line.length; i++) {
                sb.append(board[row][i]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    record Tile(int row, int col, char value) {
        @Override
        public String toString() {
            return "" + value;
        }
    }


    @Override
    public Object getSecondSolution() {
        return -1;
    }

    private static final Logger logger = LogManager.getLogger(Day2.class);

    private static final int DAY = 2;
    private static String INPUT_FILE = FOLDER_NAME + "cocha-input" + DAY + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "cocha-input" + DAY + "_test.txt";

    private final String fileName;

    public Day2(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;

    }

    @Override
    public int getDay() {
        return DAY;
    }
}
