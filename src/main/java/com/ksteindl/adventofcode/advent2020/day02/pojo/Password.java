package com.ksteindl.adventofcode.advent2020.day02.pojo;

public class Password {

    private final int firstNumber;
    private final int secondNumber;
    private final char character;
    private final String password;

    public Password(int firstNumber, int secondNumber, char character, String password) {
        this.firstNumber = firstNumber;
        this.secondNumber = secondNumber;
        this.character = character;
        this.password = password;
    }

    public int getFirstNumber() {
        return firstNumber;
    }

    public int getSecondNumber() {
        return secondNumber;
    }

    public char getCharacter() {
        return character;
    }

    public String getPassword() {
        return password;
    }

}
