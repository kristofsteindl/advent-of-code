package com.ksteindl.adventofcode.advent2020.day12.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PositionWithWaypoint implements Position{

    private static final Logger logger = LogManager.getLogger(PositionWithWaypoint.class);

    private double xWayPoint = 0;
    private double yWayPoint = 0;
    private double x = 0;
    private double y = 0;

    public PositionWithWaypoint() {
    }

    public PositionWithWaypoint(double xWayPoint, double yWayPoint) {
        this.xWayPoint = xWayPoint;
        this.yWayPoint = yWayPoint;
    }

    @Override
    public void moveFroward(int value) {
        this.x = x + getxWayPoint() * value;
        this.y = y + getyWayPoint() * value;
    }

    @Override
    public void moveDirection(Command command) {
        switch (command.getAction()){
            case N:
                this.yWayPoint += command.getValue();
                break;
            case S:
                this.yWayPoint -= command.getValue();
                break;
            case W:
                this.xWayPoint -= command.getValue();
                break;
            case E:
                this.xWayPoint += command.getValue();
                break;
            default: throw new RuntimeException("No direction to move!");
        }
    }

    @Override
    public void turn(Command command) {
       int degree = command.getValue();
        if (degree == 180) {
            this.xWayPoint = -this.xWayPoint;
            this.yWayPoint = -this.yWayPoint;
        }
        else if ((degree == 90 && command.getAction() == Action.R) || (degree == 270 && command.getAction() == Action.L)) {
            double xTmp = this.xWayPoint;
            this.xWayPoint = this.yWayPoint;
            this.yWayPoint = -xTmp;
        } else if ((degree == 90 && command.getAction() == Action.L) || (degree == 270 && command.getAction() == Action.R)) {
            double xTmp = this.xWayPoint;
            this.xWayPoint = -this.yWayPoint;
            this.yWayPoint = xTmp;
        } else {
            throw new RuntimeException("degree differs from 90, 180 or 270");
        }
    }

    public void turnGeneric(Command command) {
        double wayPointLength = Math.sqrt(Math.pow(this.xWayPoint, 2) + Math.pow(this.yWayPoint, 2));
        double oldDegree = Math.toDegrees(Math.atan(xWayPoint/yWayPoint));
        double turningDegree = command.getAction() == Action.R ? command.getValue() : command.getAction() == Action.L ? -command.getValue() : 0;
        double newDegree = oldDegree + turningDegree;
        if (newDegree < 0) {
            newDegree = newDegree + 360;
        } else if (newDegree >= 360) {
            newDegree = newDegree - 360;
        }
        double newRad = newDegree * Math.PI / 180;
        this.xWayPoint = Math.sin(newRad) * wayPointLength;
        this.yWayPoint = Math.cos(newRad) * wayPointLength;
    }

    public double getxWayPoint() {
        return xWayPoint;
    }

    public void setxWayPoint(double xWayPoint) {
        this.xWayPoint = xWayPoint;
    }

    public double getyWayPoint() {
        return yWayPoint;
    }

    public void setyWayPoint(double yWayPoint) {
        this.yWayPoint = yWayPoint;
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
