package com.ksteindl.adventofcode.advent2020.day07.pojo;

public class Bags {

    private final int quantity;
    private final Bag bag;

    public Bags(int quantity, String tone, String color) {
        this.quantity = quantity;
        this.bag = new Bag(tone, color);
    }

    public Bags(int quantity, Bag bag) {
        this.quantity = quantity;
        this.bag = bag;
    }

    public int getQuantity() {
        return quantity;
    }

    public Bag getBag() {
        return bag;
    }

    @Override
    public String toString() {
        return "Bags{" +
                "quantity=" + quantity +
                ", bag=" + bag +
                '}';
    }
}
