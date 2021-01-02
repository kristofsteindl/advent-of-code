package com.ksteindl.adventofcode.advent2020.day22.model;

public class FinalResult {

    private final boolean player2won;
    private final int winningScore;

    public FinalResult(int winningScore) {
        this.player2won = false;
        this.winningScore = winningScore;
    }

    public FinalResult(boolean player2won, int winningScore) {
        this.player2won = player2won;
        this.winningScore = winningScore;
    }

    public boolean isPlayer2won() {
        return player2won;
    }

    public int getWinningScore() {
        return winningScore;
    }


}
