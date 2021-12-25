package com.ksteindl.adventofcode.advent2021.day16;

import java.util.List;

public class Test {

    public static void main(String[] args) {
        int i = List.of(2,2).stream().mapToInt(n -> n).sum();
        System.out.println(i);
    }
}
