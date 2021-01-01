package com.ksteindl.adventofcode.advent2020.day24;

import java.util.Objects;

import static com.ksteindl.adventofcode.advent2020.day24.TileFlipper.DIMENSION;

public class Tile {

    private int x;
    private int y;

    public Tile() {
        this.x = DIMENSION;
        this.y = DIMENSION;
    }

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;
        Tile tile = (Tile) o;
        return getX() == tile.getX() &&
                getY() == tile.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}
