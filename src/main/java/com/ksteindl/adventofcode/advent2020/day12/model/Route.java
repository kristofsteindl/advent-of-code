package com.ksteindl.adventofcode.advent2020.day12.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Route {

    private static final Logger logger = LogManager.getLogger(Route.class);

    private List<Command> commands;

    public Route(List<Command> commands) {
        this.commands = commands;
    }


    public double getManhattanDistance(Position position) {
        for (int i = 0; i <commands.size() ; i++) {
            Command command = commands.get(i);
            if (command.getAction() == Action.F) {
                position.moveFroward(command.getValue());
            }
            else if (!command.getAction().isMove) {
                position.turn(command);
            }
            else {
                position.moveDirection(command);
            }
        }
        return Math.abs(position.getX()) + Math.abs(position.getY());
    }


    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }
}
