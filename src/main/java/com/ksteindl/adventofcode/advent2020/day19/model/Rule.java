package com.ksteindl.adventofcode.advent2020.day19.model;

public interface Rule {

    MatchResult consume(String message);

}
