package com.ksteindl.adventofcode.advent2020.day19.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LeafRule implements Rule {

    private final int ID;
    private final char letter;
    private List<String> matchingStrings = new ArrayList<>();

    public LeafRule(int ID, char letter) {
        this.ID = ID;
        this.letter = letter;
        this.matchingStrings.add(String.valueOf(letter));
    }

    @Override
    public MatchResult consume(String message) {
        if (!message.isEmpty() && message.charAt(0) == letter) {
            return new MatchResult(message.charAt(0) == letter, message.substring(1));
        }
        return new MatchResult(false, "");
    }

    public int getID() {
        return ID;
    }

    public char getLetter() {
        return letter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LeafRule)) return false;
        LeafRule leafRule = (LeafRule) o;
        return getID() == leafRule.getID() &&
                getLetter() == leafRule.getLetter();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getID(), getLetter());
    }

    @Override
    public String toString() {
        return "LeafRule{" +
                "ID=" + ID +
                ", letter=" + letter +
                '}';
    }
}
