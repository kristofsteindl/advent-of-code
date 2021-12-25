package com.ksteindl.adventofcode.advent2021;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tester {
    
    private static Map<Integer, List<Integer>> numberMap = new HashMap<>();

    public static void main(String[] args) {
        List<Integer> numbers = numberMap.get(7);
        if (null == numbers) {
            numbers = new ArrayList<>();
        }
        numbers.add(14);
        numberMap.put(7, numbers);
        System.out.println("Hello " + numberMap.get(7).get(0));
    }
}
