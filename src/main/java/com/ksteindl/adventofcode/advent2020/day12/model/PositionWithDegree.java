package com.ksteindl.adventofcode.advent2020.day12.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PositionWithDegree implements Position{

    private static final Logger logger = LogManager.getLogger(PositionWithDegree.class);

    private int degree = 90;
    private double x = 0;
    private double y = 0;

    public PositionWithDegree() {
    }

    public PositionWithDegree(int degree) {
        this.degree = degree;
    }


    @Override
    public void moveDirection(Command command) {
        switch (command.getAction()){
            case N:
                this.y += command.getValue();
                break;
            case S:
                this.y -= command.getValue();
                break;
            case W:
                this.x -= command.getValue();
                break;
            case E:
                this.x += command.getValue();
                break;
            default: throw new RuntimeException("No direction to move!");
        }
    }

    @Override
    public void moveFroward(int value) {
        double radian = degree * Math.PI / 180;
        this.x = x + Math.sin(radian) * value;
        this.y = y + Math.cos(radian) * value;
    }

    @Override
    public void turn(Command command) {
        int turningDegree = command.getAction() == Action.R ? command.getValue() : command.getAction() == Action.L ? -command.getValue() : 0;
        int newDegree = this.degree + turningDegree;
        if (newDegree < 0) {
            newDegree = newDegree + 360;
        } else if (newDegree >= 360) {
            newDegree = newDegree - 360;
        }

        this.degree = newDegree;
    }

    public int getDegree() {
        return degree;
    }

    @Override
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
