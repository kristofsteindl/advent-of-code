package com.ksteindl.adventofcode.advent2020.day20;

import com.ksteindl.adventofcode.advent2020.day20.model.Tile;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;

public class TileUtil {
    public static boolean hasSameEdges(final Tile reference, final Tile other) {
        return topMatches(reference, other).isPresent()
                || rightMatches(reference, other).isPresent()
                || bottomMatches(reference, other).isPresent()
                || leftMatches(reference, other).isPresent();
    }


    public static Optional<Tile> topMatches(final Tile reference, final Tile tile) {
        return getTileMatching(reference.getPixels()[0], tile, TileUtil::topEdgeMatchesTileBottomEdge);
    }

    private static boolean topEdgeMatchesTileBottomEdge(boolean[] topEdge, final Tile tile) {
        return Arrays.equals(topEdge, tile.getPixels()[9]);
    }


    public static Optional<Tile> rightMatches(final Tile reference, final Tile tile) {
        return getTileMatching(getRightEdge(reference), tile, TileUtil::rightEdgeMatchesTileLeftEdge);
    }

    private static boolean rightEdgeMatchesTileLeftEdge(boolean[] rightEdge, final Tile tile) {
        return Arrays.equals(rightEdge, getLeftEdge(tile));
    }


    public static Optional<Tile> bottomMatches(final Tile reference, final Tile tile) {
        return getTileMatching(reference.getPixels()[9], tile, TileUtil::bottomEdgeMatchesTileToptEdge);
    }

    private static boolean bottomEdgeMatchesTileToptEdge(boolean[] bottomEdge, final Tile tile) {
        return Arrays.equals(bottomEdge, tile.getPixels()[0]);
    }


    public static Optional<Tile> leftMatches(final Tile reference, final Tile tile) {
        return getTileMatching(getLeftEdge(reference), tile, TileUtil::leftEdgeMatchesTileRightEdge);
    }

    private static boolean leftEdgeMatchesTileRightEdge(boolean[] leftEdge, final Tile tile) {
        return Arrays.equals(leftEdge, getRightEdge(tile));
    }


    public static Optional<Tile> getTileMatching(boolean[] topEdge, final Tile tile, BiFunction<boolean[], Tile, Boolean> edgeChecker) {
        Tile potential = tile;
        if (edgeChecker.apply(topEdge, potential)) {
            return Optional.of(potential);
        }
        potential = getRotatedTile90Clockwise(tile, 1);
        if (edgeChecker.apply(topEdge, potential)) {
            return Optional.of(potential);
        }
        potential = getRotatedTile90Clockwise(tile, 2);
        if (edgeChecker.apply(topEdge, potential)) {
            return Optional.of(potential);
        }
        potential = getRotatedTile90Clockwise(tile, 3);
        if (edgeChecker.apply(topEdge, potential)) {
            return Optional.of(potential);
        }
        potential = mirrored(tile);
        if (edgeChecker.apply(topEdge, potential)) {
            return Optional.of(potential);
        }
        potential = getRotatedTile90Clockwise(mirrored(tile), 1);
        if (edgeChecker.apply(topEdge, potential)) {
            return Optional.of(potential);
        }
        potential = getRotatedTile90Clockwise(mirrored(tile), 2);
        if (edgeChecker.apply(topEdge, potential)) {
            return Optional.of(potential);
        }
        potential = getRotatedTile90Clockwise(mirrored(tile), 3);
        if (edgeChecker.apply(topEdge, potential)) {
            return Optional.of(potential);
        }
        return Optional.empty();
    }


    private static boolean[] getLeftEdge(Tile tile) {
        boolean[] leftEdge = new boolean[10];
        for (int i = 0; i < 10; i++) {
            leftEdge[i] = tile.getPixels()[i][0];
        }
        return leftEdge;
    }

    private static boolean[] getRightEdge(Tile tile) {
        boolean[] rightEdge = new boolean[10];
        for (int i = 0; i < 10; i++) {
            rightEdge[i] = tile.getPixels()[i][9];
        }
        return rightEdge;
    }

    public static Tile getRotatedTile90Clockwise(final Tile tile, int times) {
        Tile rotated = tile;
        for (int i = 0; i < times; i++) {
            rotated = getRotatedTile90Clockwise(rotated);
        }
        return rotated;
    }

    private static Tile getRotatedTile90Clockwise(final Tile tile) {
        boolean[][] rotatedPixels = new boolean[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                rotatedPixels[i][j] = tile.getPixels()[9 - j][i];
            }
        }
        Tile rotated = new Tile(tile.getId(), rotatedPixels);
        return rotated;
    }

    public static Boolean[][] getRotated90ClockWise(final Boolean[][] array) {
        Boolean[][] rotatedPixels = new Boolean[array[0].length][array.length];
        for (int i = 0; i < rotatedPixels.length; i++) {
            for (int j = 0; j < rotatedPixels[0].length; j++) {
                rotatedPixels[i][j] = array[rotatedPixels.length - 1 - j][i];
            }
        }
        return rotatedPixels;
    }

    public static Tile mirrored(Tile tile) {
        boolean[][] mirroredPixels = new boolean[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                mirroredPixels[i][j] = tile.getPixels()[i][9 - j];
            }
        }
        Tile mirrored = new Tile(tile.getId(), mirroredPixels);
        return mirrored;
    }

    public static Boolean[][] mirrored(Boolean[][] array) {
        Boolean[][] mirroredPixels = new Boolean[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                mirroredPixels[i][j] = array[i][array[0].length - 1 - j];
            }
        }
        return mirroredPixels;
    }

}
