package com.ksteindl.adventofcode.advent2021.day13;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ThermometerManual extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(ThermometerManual.class);

    private static final int DAY = 13;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;
    boolean[][] page;
    List<Fold> folds = new ArrayList<>();

    public ThermometerManual(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
        List<String> lines = fileManager.parseLines(fileName);
        page = new boolean[isTest ? 11 : 1311][isTest ? 15 : 895];
        lines.stream().forEach(line -> {
            String[] coordinates = line.split(",");
            if (!coordinates[0].equals("") && !line.contains("fold")) {
                page[Integer.parseInt(coordinates[0])][Integer.parseInt(coordinates[1])] = true;
            }
        });
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("fold")) {
                String[] strFolds = line.split(" ")[2].split("=");
                Fold fold = new Fold(strFolds[0], Integer.parseInt(strFolds[1]) );
                logger.info(fold);
                folds.add(fold);
            }
        }
    }

    public static void main(String[] args) {
        ThermometerManual day = new ThermometerManual(false);
        logger.info(day.getFirstSolution());
        logger.info(day.getSecondSolution());
    }

    @Override
    public Number getSecondSolution() {
        boolean[][] folded = page;
        boolean[][] prev;
        int i = 0;
        do {
            prev = folded;
            folded = fold(folds.get(i), prev);
            i++;
        } while (i < folds.size());
        printPage(folded);
        return -1;
    }

    @Override
    public Number getFirstSolution() {
        System.out.println(countDots(page));
        boolean[][] folded = fold(folds.get(0), page);
        return countDots(folded);
    }
    
    private Integer countDots(boolean[][] page) {
        int sum = 0;
        for (int y = 0; y < page[0].length; y++) {
            for (int x = 0; x < page.length; x++) {
                if (page[x][y]) {
                    sum++;
                }
            }
        }
        return sum;
    }
    
    private boolean[][] fold(Fold fold, boolean[][] page) {
        return fold.coor.equals("y") ? foldHorizontally(fold.amount, page) : foldVertically(fold.amount, page);
    }

    private boolean[][] foldHorizontally(Integer newHeight, boolean[][] page) {
        boolean[][] folded = new boolean[page.length][newHeight];
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < page.length; x++) {
                Integer mirroredCoor = 2 * newHeight - y;
                folded[x][y] = page[x][y]|| (mirroredCoor >  newHeight &&  page[x][page[0].length - 1 - y]);
            }
        }
        return folded;
    }

    private boolean[][] foldVertically(Integer newWidth, boolean[][] page) {
        boolean[][] folded = new boolean[newWidth][page[0].length];
        for (int y = 0; y < page[0].length; y++) {
            for (int x = 0; x < newWidth; x++) {
                Integer mirroredCoor = 2 * newWidth - x;
                folded[x][y] = page[x][y] || (mirroredCoor >  newWidth &&  page[mirroredCoor][y]);
            }
        }
        return folded;
    }
    
    private void printPage(boolean[][] page) {
        System.out.println("-----------------------------");
        for (int y = 0; y < (page[0].length); y++) {
            for (int x = 0; x < (page.length); x++) {
                System.out.print(page[x][y] ? "#" : ".");
            }
            System.out.println();
            
        }
        System.out.println("++++++++++++++++++++++++++++");
    }
    
    private static class Fold {
        final String coor;
        final Integer amount;

        public Fold(String coordinate, Integer amount) {
            this.coor = coordinate;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "Fold{" +
                    "coordinate='" + coor + '\'' +
                    ", amount=" + amount +
                    '}';
        }
    }
    @Override
    public int getDay() {
        return DAY;
    }

   


}
