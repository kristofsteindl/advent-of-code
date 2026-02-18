package com.ksteindl.adventofcode.codingchallenge2025.codingchallenge2021.task1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.ksteindl.adventofcode.codingchallenge2025.codingchallenge2021.CoCha2025;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Day3 extends CoCha2025 {

    String RUNNABLE_JAR_PATH = "network-checker.jar";

    public static void main(String[] args) {
        var challange = new Day3(true);
        System.out.println(challange.getFirstSolution());
    }

    static class Transceiver {
        Integer id;
        Integer cost;
        Integer tx;
        Integer ty;
        String eq;
        Integer coverage;

        Transceiver(Integer id, Integer cost, Integer tx, Integer ty, String eq) {
            this.id = id;
            this.cost = cost;
            this.tx = tx;
            this.ty = ty;
            this.eq = eq;
//            coverage = calcCoverage(tx, ty, eq);
        }
    }

    private int calcCoverage(Integer tx, Integer ty, String rawEq) {
        String[] eqs = rawEq.split(",");
        boolean[][] coverages = new boolean[w][h];
        for (int i = 0; i < coverages.length; i++) {
            for (int j = 0; j < coverages[0].length; j++) {
                coverages[i][j] = true;
            }
        }
        for (String eq : eqs) {
            if (eq.startsWith("x")) {
                if (eq.substring(1, 2).equals("=")) {
                    int number = Integer.parseInt(eq.substring(2));
                    for (int i = 0; i < coverages.length; i++) {
                        for (int j = 0; j < coverages[0].length; j++) {
                            coverages[i][j] = i == tx;
                        }
                    }
                } else if (eq.substring(1, 3).equals("<=")) {
                    for (int i = 0; i < coverages.length; i++) {
                        for (int j = 0; j < coverages[0].length; j++) {
                            coverages[i][j] = i == tx;
                        }
                    }


                } else if (eq.substring(1, 3).equals(">=")) {

                }
            } else if (eq.startsWith("y")) {

            } else if (eq.startsWith("x+y")) {

            } else if (eq.startsWith("x-y")) {

            }
        }
        return 10;
    }
    

    @Override
    public Object getFirstSolution() {
        List<String> inputFileLines = fileManager.parseLines(fileName);
        String[] rawDims = inputFileLines.get(0).split(" ");
        w = Integer.parseInt(rawDims[0]);
        h = Integer.parseInt(rawDims[1]);
        List<Transceiver> transceivers = new ArrayList<>();
        for (int i = 1; i < inputFileLines.size(); i++) {
            String line = inputFileLines.get(i);
            String[] splitted = line.split(" ");
            int id = Integer.parseInt(splitted[0].substring(0, splitted[0].length() - 1));
            int cost = Integer.parseInt(splitted[1]);
            String coordinates = splitted[2].substring(1, splitted[2].length() - 1);
            int tx = Integer.parseInt(coordinates.split(",")[0]);
            int ty = Integer.parseInt(coordinates.split(",")[1]);
            transceivers.add(new Transceiver(id, cost, tx, ty, splitted[3]));
        }
        return getSumCost("output.txt");
    }

    private int getSumCost(String outputFile) {
        Process process = null;
        try {
            String jarFile = new File(Objects.requireNonNull(getClass().getClassLoader()
                            .getResource(RUNNABLE_JAR_PATH))
                    .toURI()).getAbsolutePath();

            ProcessBuilder processBuilder = new ProcessBuilder(
                    "java", "-jar", jarFile, "ksteindl",
                    "/home/ksteindl/coding-challenges/challenge-2025/network/input/example.in",
                    outputFile);
            processBuilder.redirectErrorStream(true);

            process = processBuilder.start();
            try (InputStream inputStream = process.getInputStream()) {
                byte[] outputBytes = inputStream.readAllBytes();
                String output = new String(outputBytes);
                String first = output.split("\nHash:")[0];
                String[] splitted = first.split(" ");
                Integer value = Integer.parseInt(splitted[splitted.length - 1]);
                System.out.println("Output: " + Integer.parseInt(splitted[splitted.length - 1]));
                return value;
            }

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }


    @Override
    public Object getSecondSolution() {
        return -1;
    }

    private static final Logger logger = LogManager.getLogger(Day3.class);

    private static final int DAY = 3;
    private static String INPUT_FILE = FOLDER_NAME + "cocha-input" + DAY + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "cocha-input" + DAY + "_test.txt";

    private final String fileName;
    private int w;
    private int h;

    public Day3(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;

    }

    @Override
    public int getDay() {
        return DAY;
    }
}
