package com.ksteindl.adventofcode.advent2021.day20;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ImageEnhancer extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(ImageEnhancer.class);

    private static final int DAY = 20;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;
    private Boolean[] pixelCodes;
    private Boolean[][] originalImage;
    boolean def = false;

    public ImageEnhancer(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
        List<String> lines = fileManager.parseLines(fileName);
        char[] pixelCodeLine = lines.get(0).toCharArray();
        pixelCodes = new Boolean[pixelCodeLine.length];
        for (int i = 0; i < pixelCodeLine.length; i++) {
            pixelCodes[i] = pixelCodeLine[i] == '#';
        }

        originalImage = new Boolean[lines.size() - 2][lines.get(2).length()];
        for (int row = 0; row < lines.size() - 2; row++) {
            for (int column = 0; column < lines.get(2).length(); column++) {
                originalImage[row][column] = lines.get(row + 2).charAt(column) == '#';
            }
        }
    }

    public static void main(String[] args) {
        ImageEnhancer day = new ImageEnhancer(false);
        logger.info(day.getFirstSolution());
        logger.info(day.getSecondSolution());
    }

    @Override
    public Number getFirstSolution() {
        boolean[][] encodedImage = new boolean[originalImage.length][originalImage[0].length];
        for (int i = 0; i < originalImage.length; i++) {
            for (int j = 0; j < originalImage[0].length; j++) {
                encodedImage[i][j] = originalImage[i][j];
            }
            
        }
        for (int i = 0; i < 50; i++) {
            boolean[][] enhancedImage = new boolean[encodedImage.length + 2][encodedImage[0].length + 2];
            boolean[][] extendedEncoded = new boolean[encodedImage.length + 2][encodedImage[0].length + 2];
            for (int newRow = 0; newRow < extendedEncoded.length; newRow++) {
                for (int newColumn = 0; newColumn < extendedEncoded[0].length; newColumn++) {
                    if (newRow < 1 || newColumn < 1 || newRow == encodedImage.length + 1 || newColumn == encodedImage[0].length + 1) {
                        extendedEncoded[newRow][newColumn] = def;
                    } else {
                        extendedEncoded[newRow][newColumn] = encodedImage[newRow - 1][newColumn - 1];
                    }
                    
                }
            }
            for (int newRow = 0; newRow < extendedEncoded.length; newRow++) {
                for (int newColumn = 0; newColumn < extendedEncoded[0].length; newColumn++) {
                    enhancedImage[newRow][newColumn] = getPixel2(extendedEncoded, newRow, newColumn);
                }
            }
            print(enhancedImage);
            encodedImage = enhancedImage;
            setDef();
        }
        int lit = 0;
        for (int row = 0; row < encodedImage.length; row++) {
            for (int column = 0; column < encodedImage[0].length; column++) {
                if (encodedImage[row][column]) {
                    lit++;
                }
            }
        }
        return lit;

    }
    
    private void setDef() {
        if (pixelCodes[0] && !pixelCodes[pixelCodes.length - 1]) {
            if (pixelCodes[pixelCodes.length - 1]) {
                def = true;
            } else {
                def = !def;
            }
        }
    }
    

    private Boolean getPixel2(boolean[][] encodedImage, Integer newRow, Integer newColumn) {
        Boolean[] exps = new Boolean[9];

        if (newRow < encodedImage.length - 1 && newColumn < encodedImage[0].length - 1) {
            exps[0] = encodedImage[newRow + 1][newColumn + 1];
        } else {
            exps[0] = def;
        }

        if (newRow < encodedImage.length - 1) {
            exps[1] = encodedImage[newRow + 1][newColumn];
        } else {
            exps[1] = def;
        }

        if (newRow < encodedImage.length - 1 && newColumn > 0) {
            exps[2] = encodedImage[newRow + 1][newColumn - 1];
        } else {
            exps[2] = def;
        }

        if (newColumn < encodedImage[0].length - 1) {
            exps[3] = encodedImage[newRow][newColumn + 1];
        } else {
            exps[3] = def;
        }
        
        exps[4] = encodedImage[newRow][newColumn];

        if (newColumn > 0) {
            exps[5] = encodedImage[newRow][newColumn - 1];
        } else {
            exps[5] = def;
        }

        if (newRow > 0 && newColumn < encodedImage[0].length - 1) {
            exps[6] = encodedImage[newRow - 1][newColumn + 1];
        } else {
            exps[6] = def;
        }

        if (newRow > 0) {
            exps[7] = encodedImage[newRow - 1][newColumn];
        } else {
            exps[7] = def;
        }
        
        if (newRow > 0 && newColumn > 0) {
            exps[8] = encodedImage[newRow - 1][newColumn - 1];
        } else {
            exps[8] = def;
        }


        
        
        int key = 0;
        for (int i = 0; i < exps.length; i++) {
            key += exps[i] ? (int)Math.pow(2, i) : 0;
        }
        return pixelCodes[key];
    }
    
    private void print(boolean[][] image) {
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                System.out.print(image[i][j] ? "#" : ".");
            }
            System.out.println("");
        }
        System.out.println("-------------------------------------------------");
    }

    @Override
    public Number getSecondSolution() {
        return getDay();
    }
    

    @Override
    public int getDay() {
        return DAY;
    }

   


}
