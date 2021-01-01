package com.ksteindl.adventofcode.advent2020.day19.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BranchRule implements Rule{

    private final int ID;
    private List<List<Rule>> potentialRules = new ArrayList<>();
    private List<String> matchingStrings = new ArrayList<>();

    public BranchRule(int ID) {
        this.ID = ID;
    }

    public BranchRule(int ID, List<List<Rule>> potentialRules) {
        this.ID = ID;
        this.potentialRules = potentialRules;
    }


    public void setPotentialRules(List<List<Rule>> potentialRules) {
        this.potentialRules = potentialRules;
    }

    public void setMatchingStrings(List<String> matchingStrings) {
        this.matchingStrings = matchingStrings;
    }

    public void addRules(final List<Rule> rules) {
        potentialRules.add(rules);
    }

    @Override
    public MatchResult consume(String message) {
        return potentialRules.stream()
                .map(rules -> consume(rules, message))
                .filter(matchResult -> matchResult.isValid())
                .findAny()
                .orElse(new MatchResult(false, ""));
    }



    private MatchResult consume(List<Rule> rules, String message) {
        int i = 0;
        MatchResult matchResult;
        do {
            matchResult = rules.get(i).consume(message);
            message = matchResult.getLeftOver();
            i++;
        } while (i < rules.size() && matchResult.isValid());
        if (matchResult.isValid()) {
            return new MatchResult(true, message);
        } else {
            return new MatchResult(false, "");
        }
    }

    public int getID() {
        return ID;
    }

    public List<List<Rule>> getPotentialRules() {
        return potentialRules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BranchRule)) return false;
        BranchRule that = (BranchRule) o;
        return getID() == that.getID();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getID());
    }

    @Override
    public String toString() {
        return "BranchRule{" +
                "ID=" + ID +
                ", potentialRules=" + potentialRules.stream().map(rules -> rules.size()).collect(Collectors.toList()).toString() +
                '}';
    }
}
