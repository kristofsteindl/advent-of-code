package com.ksteindl.adventofcode.advent2020.day24;

import com.ksteindl.adventofcode.Puzzle2020;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TileFlipper extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(TileFlipper.class);

    private static final int DAY = 24;
    public final static int DIMENSION = 200;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;
    private boolean[][] tiles = new boolean[DIMENSION * 2][DIMENSION * 2];

    public TileFlipper(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
        initTiles(fileName);
    }


    @Override
    public Number getFirstSolution() {
        return getBlackTileCount();
    }

    @Override
    public Number getSecondSolution() {
        return getBlackTileCountAfterDays(100);
    }

    @Override
    public int getDay() {
        return DAY;
    }

    private int getBlackTileCountAfterDays(int days) {
        logger.debug("day " + 0 + ", black tile count: " + getBlackTileCount());
        for (int day = 1; day < days + 1; day++) {
            tiles = flipTiles();
            logger.debug("day " + day + ", black tile count: " + getBlackTileCount());
        }
        return getBlackTileCount();
    }

    private boolean[][] flipTiles() {
        boolean[][] newTiles = cloneTiles();
        for (int row = 2; row < tiles.length - 2; row++) {
            for (int column = 2; column < tiles[0].length -2; column++) {
                if (row % 2 == 0 && column % 2 == 0) {
                    flipTiles(row, column, newTiles);
                } else if (row % 2 != 0 && column % 2 != 0) {
                    flipTiles(row, column, newTiles);
                }
            }
        }
        return newTiles;
    }

    private void flipTiles(int row, int column, boolean[][] newTiles) {
        int blackAdjecants = getBlackAdjecantCount(row, column);
        if (tiles[row][column] && (blackAdjecants == 0 || blackAdjecants > 2)) {
            newTiles[row][column] = false;
        } else if (!tiles[row][column] && blackAdjecants == 2) {
            newTiles[row][column] = true;
        }
    }

    private int getBlackAdjecantCount(int row, int column) {
        int count = 0;
        // east
        if (tiles[row][column + 2]) {
            count++;
        }
        // west
        if (tiles[row][column - 2]) {
            count++;
        }
        // south-east
        if (tiles[row + 1][column + 1]) {
            count++;
        }
        // south-west
        if (tiles[row + 1][column - 1]) {
            count++;
        }
        // north-east
        if (tiles[row - 1][column + 1]) {
            count++;
        }
        // north-west
        if (tiles[row - 1][column - 1]) {
            count++;
        }
        return count;
    }

    private int getBlackTileCount() {
        int blackTileCount = 0;
        for (int row = 0; row < tiles.length ; row++) {
            for (int column = 0; column < tiles.length; column++) {
                blackTileCount += tiles[row][column] ? 1 : 0;
            }
        }
        return blackTileCount;
    }

    private void initTiles(String fileName) {
        Set<Tile> blackTiles = getBlackTilesAtStart();
        for (Tile tile : blackTiles) {
            tiles[tile.getX()][tile.getY()] = true;
        }
    }

    private Set<Tile> getBlackTilesAtStart() {
        List<String> lines = fileManager.parseLines(fileName);
        List<Tile> everyTiles = lines.stream().map(line -> getTile(line)).collect(Collectors.toList());
        Set<Tile> blackTiles = new HashSet<>();
        for (Tile tile : everyTiles) {
            if (blackTiles.contains(tile)) {
                blackTiles.remove(tile);
            } else {
                blackTiles.add(tile);
            }
        }
        return blackTiles;
    }


    private boolean[][] cloneTiles() {
        boolean[][] newTiles = new boolean[tiles.length][tiles[0].length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                newTiles[i][j] = tiles[i][j];
            }
        }
        return newTiles;
    }

    private Tile getTile(String line) {
        int x = DIMENSION;
        int y = DIMENSION;
        for (int i = 0; i < line.length(); i++) {
            char c = line.toCharArray()[i];
            switch (c) {
                case 'e': y += 2; break;
                case 'w': y -= 2; break;
                case 's':
                    i++;
                    if (line.toCharArray()[i] == 'e') {
                        x++;
                        y++;
                    } else {
                        x++;
                        y--;
                    }
                    break;
                case 'n':
                    i++;
                    if (line.toCharArray()[i] == 'e') {
                        x--;
                        y++;
                    } else {
                        x--;
                        y--;
                    }
                    break;
            }
        }
        return new Tile(x, y);
    }


    /*
    *
    * References
    *
    * */
}
