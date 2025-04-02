package com.ksteindl.adventofcode.advent2024;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import com.google.common.collect.Lists;

public class Day4 extends Puzzle2024 {
    
    private static final List<Character> XMAS = List.of('X', 'M', 'A', 'S');
    private static final List<Character> SAMX = Lists.reverse(XMAS);

    public static void main(String[] args) {
        new Day4().printSolutions();
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        char[][] words = getWords(lines);
        int sum = 0;
        for (int row = 1; row < words.length - 1; row++) {
            for (int column = 1; column < words[0].length - 1; column++) {
                if (words[row][column] == 'A' && isX(row, column, words)) {
//                    System.out.println(row + "," + column);
                    sum++;
                }
            }
        }
        return sum;
    }
    
    private boolean isX(int row, int column, char[][] words) {
        var topLeft = words[row - 1][column - 1];
        var topRight = words[row - 1][column + 1];
        var bottomLeft = words[row + 1][column - 1];
        var bottomRight = words[row + 1][column + 1];
        return (topLeft == 'M' && bottomRight == 'S' || topLeft == 'S' && bottomRight == 'M')
                && (topRight == 'M' && bottomLeft == 'S' || topRight == 'S' && bottomLeft == 'M');
    }


    @Override
    protected Number getFirstSolution(List<String> lines) {
        char[][] words = getWords(lines);
        int vertical = countVertical(words);
        int horizontal = countHorizontal(words);
        int diagonalFromTopLeft = countDiagonalFromTopLeft(words);
        int diagonalFromTopRight = countDiagonalFromTopRight(words);
        return vertical + horizontal + diagonalFromTopLeft + diagonalFromTopRight;
    }

    private int countHorizontal(char[][] words) {
        int sum = 0;
        for (int row = 0; row < words.length; row++) {
            List<Character> line = new ArrayList<>();
            for (int index = 0; index < words[row].length; index++) {
                line.add(words[row][index]);
            }
            sum += countXmas(line);
        }
        return sum;
    }
    
    private int countVertical(char[][] words) {
        int sum = 0;
        for (int column = 0; column < words[0].length; column++) {
            List<Character> line = new ArrayList<>();
            for (int index = 0; index < words.length; index++) {
                line.add(words[index][column]);
            }
            sum += countXmas(line);
        }
        return sum;
    }

    private int countDiagonalFromTopLeft(char[][] words) {
        int sum = 0;
        for (int lineIndex = 0; lineIndex < words.length - 3; lineIndex++) {
            List<Character> line = new ArrayList<>();
            for (int index = 0; index < words.length - lineIndex; index++) {
                line.add(words[index][index + lineIndex]);
            }
            sum += countXmas(line);
            if (lineIndex != 0) {
                line = new ArrayList<>();
                for (int index = 0; index < words.length - lineIndex; index++) {
                    line.add(words[index + lineIndex][index]);
                }
                sum += countXmas(line);
            }

        }
        return sum;
    }

    private int countDiagonalFromTopRight(char[][] words) {
        int sum = 0;
        for (int lineIndex = words.length - 1; lineIndex >= 3; lineIndex--) {
            List<Character> line = new ArrayList<>();
            for (int index = 0; index <= lineIndex; index++) {
                line.add(words[index][lineIndex - index]);
            }
            sum += countXmas(line);
            if (lineIndex != words.length - 1) {
                line = new ArrayList<>();
                for (int index = 0; index <= lineIndex; index++) {
                    line.add(words[words.length - 1 - (lineIndex - index)][words.length - 1 -index]);
                }
                sum += countXmas(line);
            }
        }
        return sum;
    }
    
    private int countXmas(List<Character> line) {
        return (int) IntStream.range(3, line.size())
                .mapToObj(i -> line.subList(i - 3, i + 1))
                .filter(sublist -> sublist.equals(XMAS) || sublist.equals(SAMX))
                .count();
    }
    
    private char[][] getWords(List<String> lines) {
        char[][] words = new char[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(0).length(); j++) {
                words[i][j] = lines.get(i).charAt(j);
            }
        }
        return words;
    }

    @Override
    public int getDay() {
        return 4;
    }
}
