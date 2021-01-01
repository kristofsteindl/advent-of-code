package com.ksteindl.adventofcode.advent2020.day02;

import com.ksteindl.adventofcode.Puzzle2020;
import com.ksteindl.adventofcode.advent2020.day02.pojo.Password;

import java.util.List;
import java.util.function.Predicate;

public class TobogganPasswordValidator extends Puzzle2020 {

    private static final int DAY = 2;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;

    public TobogganPasswordValidator(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    @Override
    public Number getFirstSolution() {
        return (int) countValidPasswordsPartOne();
    }

    @Override
    public Number getSecondSolution() {
        return (int) countValidPasswordsPartTwo();
    }

    @Override
    public int getDay() {
        return DAY;
    }

    private long countValidPasswordsPartOne() {
        return countValidPasswords(line -> isValidPartOne(line));
    }

    private long countValidPasswordsPartTwo() {
        return countValidPasswords(line -> isValidPartTwo(line));
    }

    private long countValidPasswords(Predicate<String> passwordRule) {
        List<String> lines = fileManager.parseLines(fileName);
        long validPasswordcount = lines.stream()
                .filter(passwordRule)
                .count();
        return validPasswordcount;
    }

    private boolean isValidPartOne(String line) {
        Password password = createPassword(line);
        long count = password.getPassword().chars().filter(ch -> ch == password.getCharacter()).count();
        return count <= password.getSecondNumber() && count >= password.getFirstNumber();
    }

    private boolean isValidPartTwo(String line) {
        Password password = createPassword(line);
        boolean firstPositionMatch = password.getPassword().charAt(password.getFirstNumber() - 1) == password.getCharacter();
        boolean secondPositionMatch = password.getPassword().charAt(password.getSecondNumber() - 1) == password.getCharacter();
        return (firstPositionMatch && !secondPositionMatch) || (!firstPositionMatch && secondPositionMatch);
    }


    private Password createPassword(String line) {
        String[] toBeFirstNumber = line.split("-", 2);
        int firstNumber = Integer.parseInt(toBeFirstNumber[0]);
        String[] toBeSecondNumber = toBeFirstNumber[1].split(" ", 2);
        int secondNumber = Integer.parseInt(toBeSecondNumber[0]);
        String[] toBeCharacter = toBeSecondNumber[1].split(":", 2);
        char character  = toBeCharacter[0].charAt(0);
        String password = toBeCharacter[1].trim();
        return new Password(firstNumber, secondNumber, character, password);
    }


    /*
     *
     * References
     *
     * https://www.geeksforgeeks.org/split-string-java-examples/
     * https://www.baeldung.com/java-count-chars
     * https://stackoverflow.com/questions/7853502/how-to-convert-parse-from-string-to-char-in-java
     *
     * */


}
