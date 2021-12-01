package com.ksteindl.adventofcode.advent2020.day16.model;

public class Rule {

    private final String key;
    private final Pair firstPair;
    private final Pair secondPair;

//    public static Comparator<Rule> FIRST_PAIR_COMPARABLE = (pair1, pair2) -> {
//        if (pair1.firstPair > pair2)
//        return 0;
//    };


    public Rule(String key, Pair firstPair, Pair secondPair) {
        this.key = key;
        this.firstPair = firstPair;
        this.secondPair = secondPair;
    }


    public String getKey() {
        return key;
    }

    public Pair getFirstPair() {
        return firstPair;
    }

    public Pair getSecondPair() {
        return secondPair;
    }

}
