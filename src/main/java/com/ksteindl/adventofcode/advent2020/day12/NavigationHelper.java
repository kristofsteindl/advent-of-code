package com.ksteindl.adventofcode.advent2020.day12;

import com.ksteindl.adventofcode.Puzzle2020;
import com.ksteindl.adventofcode.advent2020.day12.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class NavigationHelper extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(NavigationHelper.class);

    private static final int DAY = 12;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;
    private final List<Command> initialCommands;

    public NavigationHelper(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
        initialCommands = parseInitialCommand(fileName);
    }


    @Override
    public Number getFirstSolution() {
        Route route = new Route(initialCommands);
        return route.getManhattanDistance(new PositionWithDegree());
    }

    @Override
    public Number getSecondSolution() {
        Route route = new Route(initialCommands);
        return route.getManhattanDistance(new PositionWithWaypoint(10, 1));
    }

    @Override
    public int getDay() {
        return DAY;
    }


    private List<Command> parseInitialCommand(String fileName) {
        return fileManager.parseLines(fileName).stream().map(line -> parseCommand(line)).collect(Collectors.toList());
    }

    private Command parseCommand(String line) {
        Action action = Action.valueOf("" + line.charAt(0));
        int vaule = Integer.parseInt(line.substring(1, line.length()));
        return new Command(action, vaule);
    }

    /*
    *
    * References
    *
    * https://stackoverflow.com/questions/3449826/how-do-i-find-the-inverse-tangent-of-a-line
    * https://www.youtube.com/watch?v=QdA9g59ybks&ab_channel=MathswithJay
    * https://stackoverflow.com/questions/15994194/how-to-convert-x-y-coordinates-to-an-angle
    *
    * */
}
