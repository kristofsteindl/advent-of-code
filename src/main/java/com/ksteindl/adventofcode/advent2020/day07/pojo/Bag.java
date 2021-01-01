package com.ksteindl.adventofcode.advent2020.day07.pojo;

import java.util.Objects;

public class Bag {

    private final String tone;
    private final String color;

    public Bag(String tone, String color) {
        this.tone = tone;
        this.color = color;
    }

    public String getTone() {
        return tone;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Bag{" +
                "tone='" + tone + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bag bag = (Bag) o;
        return tone.equals(bag.tone) &&
                color.equals(bag.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tone, color);
    }
}
