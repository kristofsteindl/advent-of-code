package com.ksteindl.adventofcode.advent2020.day06;

import com.ksteindl.adventofcode.advent2020.Puzzle2020;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomFormProcessor extends Puzzle2020 {

    private static final int DAY = 6;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;

    public CustomFormProcessor(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    @Override
    public Number getFirstSolution() {
        return getSumOfYesFromGroups(fileName);
    }

    @Override
    public Number getSecondSolution() {
        return getSumOfClearYesCountFromGroups(fileName);
    }

    @Override
    public int getDay() {
        return DAY;
    }

    private int getSumOfClearYesCountFromGroups(String fileName) {
        List<Set<Integer>> groupForms = fileManager.parseLinesAndGroupByEmptyLines(fileName).stream()
                .map(group -> getClearYesSet(group))
                .collect(Collectors.toList());
        return groupForms.stream().mapToInt( group -> group.size()).sum();
    }

    private Set<Integer> getClearYesSet(List<String> group) {
        Set<Integer> clearYesSet = group.get(0).chars().boxed().collect(Collectors.toSet());
        for (String passanger : group) {
            Set<Integer> passangerSet = passanger.chars().boxed().collect(Collectors.toSet());
            clearYesSet.retainAll(passangerSet);
        }
        return clearYesSet;
    }

    private int getSumOfYesFromGroups(String fileName) {
        List<Set<Integer>> groupForms = fileManager.parseLinesAndGroupByEmptyLines(fileName).stream()
                .map(groups -> groups.stream()
                        .flatMapToInt(form -> form.chars())
                        .boxed()
                        .collect(Collectors.toSet()))
                .collect(Collectors.toList());
        return groupForms.stream().mapToInt( group -> group.size()).sum();
    }


    /*
    *
    * https://www.baeldung.com/java-string-to-stream
    * https://howtodoinjava.com/java8/convert-intstream-collection-array/
    * https://www.tutorialspoint.com/get-the-intersection-of-two-sets-in-java
    * https://www.baeldung.com/java-stream-sum
    * */






}
