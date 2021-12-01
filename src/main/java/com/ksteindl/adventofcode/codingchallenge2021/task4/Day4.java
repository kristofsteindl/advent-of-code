package com.ksteindl.adventofcode.codingchallenge2021.task4;

import com.ksteindl.adventofcode.codingchallenge2021.CoCha2021;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day4 extends CoCha2021 {

    private static final Logger logger = LogManager.getLogger(Day4.class);

    private static final int DAY = 4;
    private static String INPUT_FILE = FOLDER_NAME + "cocha-input" + DAY + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "cocha-input" + DAY + "_test.txt";

    private final String fileName;

    public Day4(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;

    }

    @Override
    public Object getFirstSolution() {
        List<String> lines = fileManager.parseLines(fileName);
        int sum = 0;
        for (String line : lines) {
            String[] components = line.split("-");
            Map<Character, AtomicInteger> chars = new TreeMap<>();
            for (int i = 0; i < components.length - 1; i++) {
                for (int j = 0; j < components[i].length(); j++) {
                    Character character = components[i].charAt(j);
                    if (chars.get(character) == null) {
                        chars.put(character, new AtomicInteger(0));
                    }
                    chars.get(character).addAndGet(1);
                }
            }
            List<Character> sortedChars = chars.entrySet().stream()
                    //.peek(entry1 -> System.out.println(entry1.getKey() + ", " + (int)(entry1.getValue().get() * 1000 + entry1.getKey().charValue())))
                    .sorted((entry1, entry2) -> (entry1.getValue().get() * 1000 - entry1.getKey()) < (entry2.getValue().get() * 1000 - entry2.getKey()) ? 1 : -1)
                    .map(entry -> entry.getKey())
                    .collect(Collectors.toList());
            //logger.info(sortedChars);
            String splitted = components[components.length - 1].split("\\[")[1];
            //logger.info(splitted);
            boolean match = true;
            for (int i = 0; i < 5; i++) {
                match = match && sortedChars.get(i).equals(splitted.charAt(i));
            }
            if (match) {
                sum += Integer.parseInt(components[components.length - 1].split("\\[")[0]);
            }
        }

        return sum;
    }


    @Override
    public Object getSecondSolution() {
        List<String> lines = fileManager.parseLines(fileName);
        for (String line : lines) {
            String[] components = line.split("-");
            Map<Character, AtomicInteger> chars = new TreeMap<>();
            for (int i = 0; i < components.length - 1; i++) {
                for (int j = 0; j < components[i].length(); j++) {
                    Character character = components[i].charAt(j);
                    if (chars.get(character) == null) {
                        chars.put(character, new AtomicInteger(0));
                    }
                    chars.get(character).addAndGet(1);
                }
            }
            List<Character> sortedChars = chars.entrySet().stream()
                   // .peek(entry1 -> System.out.println(entry1.getKey() + ", " + (int)(entry1.getValue().get() * 1000 + entry1.getKey().charValue())))
                    .sorted((entry1, entry2) -> (entry1.getValue().get() * 1000 - entry1.getKey()) < (entry2.getValue().get() * 1000 - entry2.getKey()) ? 1 : -1)
                    .map(entry -> entry.getKey())
                    .collect(Collectors.toList());
            //logger.info(sortedChars);
            String splitted = components[components.length - 1].split("\\[")[1];
            //logger.info(splitted);
            boolean match = true;
            for (int i = 0; i < 5; i++) {
                match = match && sortedChars.get(i).equals(splitted.charAt(i));
            }
            if (match) {
                String last = line.split("-")[line.split("-").length - 1];

                Integer sectorId = Integer.parseInt(last.split("\\[")[0]);
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < line.length(); i++) {
                    char character = line.charAt(i);

                    if (character == '-') {
                        builder.append(' ');
                    } else {
                        int newC = character + sectorId % 26;
                        if (newC > 122) {
                            newC -= 26;
                        }
                        builder.append((char)newC);
                    }

                }
                if (builder.toString().contains("northpole object storage")) {
                    logger.info(builder.toString());
                    logger.info(sectorId);
                }

            }
        }
        return -1;

    }

    @Override
    public int getDay() {
        return DAY;
    }
}
