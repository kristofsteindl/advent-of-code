package com.ksteindl.adventofcode.advent2020.day20.model;

import java.util.Objects;

public class Tile {

    private final int id;
    private final boolean[][] pixels;

    public Tile(int id, boolean[][] pixels) {
        this.id = id;
        this.pixels = pixels;
    }

    public int getId() {
        return id;
    }

    public boolean[][] getPixels() {
        return pixels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;
        Tile tile = (Tile) o;
        return getId() == tile.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Tile{" +
                "id=" + id +
                '}';
    }
}
