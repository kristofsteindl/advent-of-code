package com.ksteindl.adventofcode.advent2020.day20;

import com.ksteindl.adventofcode.Puzzle2020;
import com.ksteindl.adventofcode.advent2020.day20.model.Tile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class ImageReconstruator extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(ImageReconstruator.class);

    private static final int DAY = 20;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";
    private static String INPUT_MONSTER_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_monster.txt";

    private final String fileName;
    private final List<Tile> tiles;
    private final Boolean[][] monster;

    public ImageReconstruator(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
        this.tiles = getTiles(fileManager.parseLines(fileName));
        this.monster = initMonster();

    }

    @Override
    public Number getFirstSolution() {
        return getProductOfTheCornerImageIds();
    }

    @Override
    public Number getSecondSolution() {
        return countWaterRoughness();
    }

    @Override
    public int getDay() {
        return DAY;
    }

    private int countWaterRoughness() {
        Boolean[][] bigPicture = createBigPicture();
        int waterPixelCount = countTruePixels(bigPicture);
        int monsterOccurrenceCount = countSummaMonsterOccurrence(bigPicture);
        return waterPixelCount - monsterOccurrenceCount;
    }

    private int countSummaMonsterOccurrence(Boolean[][] bigPicture) {
        return countMonsterOccurrenceInEveryRotation(bigPicture) + countMonsterOccurrenceInEveryRotation(TileUtil.mirrored(bigPicture));
    }

    private int countMonsterOccurrenceInEveryRotation(final Boolean[][] picture) {
        int monsterOccurrenceCount = countMonsterOccurrence(picture);
        Boolean[][] pic90 = TileUtil.getRotated90ClockWise(picture);
        monsterOccurrenceCount += countMonsterOccurrence(pic90);
        Boolean[][] pic180 = TileUtil.getRotated90ClockWise(pic90);
        monsterOccurrenceCount += countMonsterOccurrence(pic180);
        Boolean[][] pic270 = TileUtil.getRotated90ClockWise(pic180);
        monsterOccurrenceCount += countMonsterOccurrence(pic270);
        return monsterOccurrenceCount;
    }
    
    private int countTruePixels(Boolean[][] picture) {
        return (int) Arrays.stream(picture).flatMap(Arrays::stream).filter(pixel -> pixel == true).count();
    }

    private Boolean[][] createBigPicture() {
        List<Tile> notYetFounds = new ArrayList<>(tiles);
        LinkedList<Tile> horizontalAxis = buildHorizontalAxis(notYetFounds);
        List<LinkedList<Tile>> unifiedMosaic = buildVerticalAxises(horizontalAxis, notYetFounds);
        final Boolean[][] bigPicture = transformToOnePicture(unifiedMosaic);
        return bigPicture;

    }

    private int countMonsterOccurrence(final Boolean[][] bigPicture) {
        int monsterOccurrenceCount = 0;
        for (int i = 0; i < bigPicture.length; i++) {
            for (int j = 0; j < bigPicture[0].length; j++) {
                monsterOccurrenceCount += findMonsterFromHere(bigPicture, i, j);
            }
        }

        return monsterOccurrenceCount;
    }
    
    
    private int findMonsterFromHere(final Boolean[][] bigPicture, int i, int j) {
        if (i + monster.length > bigPicture.length || j + monster[0].length > bigPicture[0].length) {
            return 0;
        }
        for (int k = 0; k < monster.length; k++) {
            for (int l = 0; l < monster[0].length; l++) {
                if (monster[k][l] && !bigPicture[i + k][j + l]) {
                    return 0;
                }
            }
        }
        return countTruePixels(monster);
    }


    private List<LinkedList<Tile>> buildVerticalAxises(LinkedList<Tile> horizontalAxis, List<Tile> notYetFounds) {
        List<LinkedList<Tile>> unifiedMosaic = horizontalAxis.stream().map(tile -> getVerticalBaseAxis(tile)).collect(Collectors.toList());
        do {
            appendVerticalAxises(unifiedMosaic, notYetFounds);
        } while (!notYetFounds.isEmpty());
        logUnifiedMosaic(unifiedMosaic);
        return unifiedMosaic;
    }

    private Boolean[][] transformToOnePicture(List<LinkedList<Tile>> unifiedMosaic) {
        int bigPictureHeight = unifiedMosaic.get(0).size() * 8;
        int bigPictureWidth = unifiedMosaic.size() * 8;
        Boolean[][] bigPicture = new Boolean[bigPictureHeight][bigPictureWidth];
        for (int column = 0; column < bigPicture[0].length; column++) {
            LinkedList<Tile> verticalAxis = unifiedMosaic.get(column / 8);
            for (int row = 0; row < bigPicture.length; row++) {
                Tile tile = verticalAxis.get(row / 8);
                int tileRow = row % 8 + 1;
                int tileColumn = column % 8 + 1;
                boolean bigPictureValue = tile.getPixels()[tileRow][tileColumn];
                bigPicture[row][column] = bigPictureValue;
            }
        }
        logBigPicture(bigPicture);
        return bigPicture;
    }

    private void logBigPicture(Boolean[][] bigPicture) {
        StringBuilder builder = new StringBuilder("\n");
        for (int i = 0; i < bigPicture.length; i++) {
            for (int j = 0; j < bigPicture[0].length; j++) {
                builder.append(bigPicture[i][j] ? "#" : '.');
            }
            builder.append("\n");
        }
        logger.debug(builder.toString());
    }


    private static void logUnifiedMosaic(List<LinkedList<Tile>> unifiedMosaic) {
        for (int i = 0; i < unifiedMosaic.get(0).size(); i++) {
            for (int j = 0; j < unifiedMosaic.size(); j++) {
                Tile tile = unifiedMosaic.get(j).get(i);
                StringBuilder builder = new StringBuilder("\n" + tile.getId() + "\n");
                for (int k = 0; k < tile.getPixels().length; k++) {
                    for (int l = 0; l < tile.getPixels()[0].length; l++) {
                        builder.append(tile.getPixels()[k][l] ? '#' : '.');
                    }
                    builder.append("\n");
                }
                logger.debug(builder.toString());
            }
        }
    }

    private void appendVerticalAxises(List<LinkedList<Tile>> bigPicture, List<Tile> notYetFounds) {
        for (LinkedList<Tile> vericalAxis: bigPicture) {
            Tile top = vericalAxis.getFirst();
            Tile bottom = vericalAxis.getLast();
            notYetFounds.stream()
                    .map(tile -> TileUtil.topMatches(top, tile))
                    .filter(Optional::isPresent)
                    .findAny()
                    .ifPresent(optional -> vericalAxis.addFirst(optional.get()));
            notYetFounds.stream()
                    .map(tile -> TileUtil.bottomMatches(bottom, tile))
                    .filter(Optional::isPresent)
                    .findAny()
                    .ifPresent(optional -> vericalAxis.addLast(optional.get()));
            notYetFounds.remove(vericalAxis.getFirst());
            notYetFounds.remove(vericalAxis.getLast());
        }
    }
;
    private void appendHorizontalAxis(LinkedList<Tile> horizontalAxis, List<Tile> notYetFounds) {
        if (horizontalAxis.isEmpty()) {
            Tile tile = notYetFounds.remove(0);
            Tile normalized = TileUtil.getRotatedTile90Clockwise(TileUtil.mirrored(tile), 2);
            horizontalAxis.add(normalized);
        } else {
            Tile first = horizontalAxis.getFirst();
            Tile last = horizontalAxis.getLast();
            notYetFounds.stream()
                    .map(tile -> TileUtil.leftMatches(first, tile))
                    .filter(Optional::isPresent)
                    .findAny()
                    .ifPresent(optional -> horizontalAxis.addFirst(optional.get()));
            notYetFounds.stream()
                    .map(tile -> TileUtil.rightMatches(last, tile))
                    .filter(Optional::isPresent)
                    .findAny()
                    .ifPresent(optional -> horizontalAxis.addLast(optional.get()));
            notYetFounds.remove(horizontalAxis.getFirst());
            notYetFounds.remove(horizontalAxis.getLast());
        }
    }


    private LinkedList<Tile> getVerticalBaseAxis(Tile tile) {
        LinkedList<Tile> verticalBaseAxis = new LinkedList<>();
        verticalBaseAxis.add(tile);
        return verticalBaseAxis;
    }

    private LinkedList<Tile> buildHorizontalAxis(List<Tile> notYetFounds) {
        LinkedList<Tile> horizontalAxis = new LinkedList<>();
        int prevCount;
        do {
            prevCount = horizontalAxis.size();
            appendHorizontalAxis(horizontalAxis, notYetFounds);
        } while (prevCount < horizontalAxis.size());
        return horizontalAxis;
    }


    private long getProductOfTheCornerImageIds() {
        Set<Tile> cornerTiles = tiles.stream().filter(tile -> isCornerTile(tile, tiles)).collect(Collectors.toSet());
        return cornerTiles.stream().mapToLong(tile -> tile.getId()).reduce(1, (a, b) -> a * b);
    }

    private boolean isCornerTile(Tile thisTile, List<Tile> tiles) {
        long edgesWithNeighbour = tiles.stream()
                .filter(tile -> tile.getId() != thisTile.getId())
                .filter(tile -> TileUtil.hasSameEdges(thisTile, tile))
                .count();
        logger.debug("tileID: " + thisTile.getId() + ", edgesWithNeighbour: " + edgesWithNeighbour);
        return edgesWithNeighbour < 3;
    }

    private List<Tile> getTiles(List<String> lines) {
        List<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < lines.size(); i += 12) {
            int id = Integer.parseInt(lines.get(i).split("Tile ")[1].split(":")[0]);
            boolean[][] pixels = new boolean[10][10];
            for (int j = 0; j < 10; j++) {
                String line = lines.get(i + 1 + j);
                for (int k = 0; k < 10; k++) {
                    pixels[j][k] = line.charAt(k) == '#';
                }
            }
            Tile tile = new Tile(id, pixels);
            tiles.add(tile);
        }
        return tiles;
    }

    private Boolean[][] initMonster() {
        List<String> monsterList = fileManager.parseLines(INPUT_MONSTER_FILE);
        Boolean[][] monster = new Boolean[monsterList.size()][monsterList.get(0).length()];
        for (int i = 0; i < monsterList.size(); i++) {
            for (int j = 0; j < monsterList.get(0).length(); j++) {
                monster[i][j] = monsterList.get(i).charAt(j) == '#';
            }
        }
        return monster;
    }


    /*
    *
    * References
    *
    * */
}
