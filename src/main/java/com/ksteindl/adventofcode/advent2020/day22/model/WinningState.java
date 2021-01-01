package com.ksteindl.adventofcode.advent2020.day22.model;

public class WinningState {

    private boolean won = false;
    private boolean player2won = false;
    private int winningScore = -1;

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public boolean isPlayer2won() {
        return player2won;
    }

    public void setPlayer2won(boolean player2won) {
        this.player2won = player2won;
    }

    public int getWinningScore() {
        return winningScore;
    }

    public void setWinningScore(int winningScore) {
        this.winningScore = winningScore;
    }
}
