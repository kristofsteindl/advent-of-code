package com.ksteindl.adventofcode.advent2023;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Day18 extends Puzzle2023{

    public static void main(String[] args) {
        new Day18().printSolutions();
    }
    
    private static class Command {
        Character dir;
        Integer value;

        public Command(Character dir, Integer value) {
            this.dir = dir;
            this.value = value;
        }
    }
    
    private static class RowCounter {
        int column;
        int counter;
        
        void incrCounter(){
            counter++;
        }

        void incrColumn(){
            column++;
        }
    }
    
    private enum Alignment {HORIZONTAL, VERTICAL}
    
    private static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    private static class Section implements Comparable<Section>{
        Alignment alignment;
        Point start;
        int value;

        public Section(Alignment alignment, Point start, int value) {
            this.alignment = alignment;
            this.start = start;
            this.value = value;
        }

        public Section(Point start, int value) {
            this.alignment = Alignment.VERTICAL;
            this.start = start;
            this.value = value;
        }

        @Override
        public int compareTo(Section other) {
            if (other.alignment == this.alignment) {
                if (other.start.y == this.start.y) {
                    return Integer.compare(this.start.x, other.start.x);
                }
                return Integer.compare(this.start.y, other.start.y);
            } 
            return this.alignment == Alignment.HORIZONTAL ? 1 : -1;
        }
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        return getSolution(lines, true);
    }

    @Override
    protected Number getFirstSolution(List<String> lines) {
        getFirstSolutionOld(lines);
        return getSolution(lines, false);
    }
    
    private Long getSolution(List<String> lines, boolean second) {
        List<Command> commands = lines.stream().map(line -> getCommand(line, second)).toList();
        List<Section> sections = getSections(commands);
        Map<Integer, List<Section>> sectionMap = sections.stream().collect(Collectors.groupingBy(section -> section.start.y));
        long counter = 0;
        List<Section> areas = new ArrayList<>();
        List<Integer> yList = sectionMap.keySet().stream().sorted().toList();
        for (int i = 0; i < yList.size(); i++) {
            List<Section> newSections = sectionMap.get(i);
            int y = yList.get(i);
            int yDiff = i == 0 ? 0 : y - yList.get(i - 1);
            List<Section> newAreas = new ArrayList<>();
            for (Section area : areas) {
                counter += (long) yDiff *  area.value + 1;
                newAreas.addAll(subtractNewSectionFromArea(area, newSections));

            }
            for (Section newSection : newSections) {
                addNewSection(areas, newSection).ifPresent(newAreas::add);
            }
            areas = newAreas;
        }
        return counter;
    }
    
    private Optional<Section> addNewSection(List<Section> areas, Section newSection) {
        for (Section area : areas) {
            if (area.start.x == newSection.start.x) {
                return Optional.empty();
            }
            if (area.start.x + area.value == newSection.start.x + newSection.value) {
                return Optional.empty();
            }
            if (area.start.x == newSection.start.x + newSection.value) {
                return Optional.of(new Section(new Point(newSection.start.x, newSection.start.y), newSection.value - 1));
            } if (area.start.x + area.value == newSection.start.x) {
                boolean otherEnd = areas.stream().anyMatch(a -> a.start.x == newSection.start.x + newSection.value);
                return Optional.of(new Section(new Point(newSection.start.x + 1, newSection.start.y), newSection.value - (otherEnd ? 2 : 1)));
            }

        }
        return Optional.of(newSection);
    }

    private List<Section> subtractNewSectionFromArea(Section area, List<Section> newSections) {
        Collections.sort(newSections);
        List<Section> subtracted = new ArrayList<>();
        int start = area.start.x;
        for (Section newSection : newSections) {
            if (start < newSection.start.x) {
                subtracted.add(new Section(new Point(start, newSection.start.y), newSection.start.x < area.start.x ? newSection.start.x - start - 1 : area.value));
            }
            start = newSection.start.x + newSection.value + 1;
        }
        if (area.start.x >= start - 1) {
            subtracted.add(new Section(new Point(area.start.x, newSections.get(0).start.y), area.value));
        }
        else if (start <= area.start.x + area.value) {
            subtracted.add(new Section(new Point(start, newSections.get(0).start.y), area.value - (start - area.start.x) - 1));
        }
        return subtracted;

    }
    
    private List<Section> getSections(List<Command> commands) {
        Point pointer = new Point(0,0);
        return commands.stream()
                .map(command -> {
                    Alignment alignment = command.dir == 'U' || command.dir == 'D' ? Alignment.VERTICAL : Alignment.HORIZONTAL;
                    Point start = new Point(pointer.x, pointer.y);
                    int[] vector = getVector(command, command.value);
                    pointer.x += vector[0];
                    pointer.y += vector[1];
                    Point end = new Point(pointer.x, pointer.y);
                    return new Section(alignment, smaller(start, end),command.value);
                })
                .sorted()
                .filter(section -> section.alignment == Alignment.VERTICAL)
                .toList();
    }

    private Point smaller(Point one, Point other) {
        return (one.x + one.y - other.x - other.y) > 0 ? other : one;
    }
    
    protected Number getFirstSolutionOld(List<String> lines) {
        List<Command> commands = lines.stream().map(line -> getCommand(line, false)).toList();
        int size = lines.size() > 100 ? 800 : 21;
        boolean[][] lagoon = new boolean[size][size];
        int[] pointer = new int[]{size / 2, size / 2};
        for (Command command : commands) {
            int[] vector = getVector(command);
            for (int i = 0; i < command.value; i++) {
                pointer[0] = pointer[0] + vector[0];
                pointer[1] = pointer[1] + vector[1];
                lagoon[pointer[0]][pointer[1]] = true;
            }
        }
        printLagoon(lagoon);
        RowCounter counter = new RowCounter();
        for (int row = 1; row < lagoon.length; row++) {
            counter.column = 0;
            while (counter.column < lagoon.length - 1) {
                increaseCounter(lagoon, counter, row);
            }
        }
        return counter.counter;
    }
    
    private void increaseCounter(boolean[][] lagoon, RowCounter counter, int row) {
        while (counter.column < lagoon[0].length && !lagoon[row][counter.column]) {
            counter.incrColumn();
        }
        if (counter.column == lagoon[0].length) {
            return;
        }
        boolean wallAbove = lagoon[row - 1][counter.column];
        boolean wallBelow = lagoon[row + 1][counter.column];
        while (lagoon[row][counter.column]) {
            counter.incrCounter();
            counter.incrColumn();
        }
        if ((wallBelow && wallAbove) || (wallBelow && lagoon[row - 1][counter.column - 1]) || (wallAbove && lagoon[row + 1][counter.column -1])) {
            boolean terminated = false;
            while (!terminated) {
                while (!lagoon[row][counter.column]) {
                    counter.incrCounter();
                    counter.incrColumn();
                }
                wallAbove = lagoon[row - 1][counter.column];
                wallBelow = lagoon[row + 1][counter.column];
                if (wallAbove && wallBelow) {
                    counter.incrColumn();
                    counter.incrCounter();
                    terminated = true;
                } else {
                    while (lagoon[row][counter.column]) {
                        counter.incrCounter();
                        counter.incrColumn();
                    }
                    terminated = (wallBelow && lagoon[row - 1][counter.column - 1]) || (wallAbove && lagoon[row + 1][counter.column -1]);
                }
            }
        }
    }
    
    private void printLagoon(boolean[][] lagoon) {
        for (boolean[] row : lagoon) {
          for (boolean cube : row) {
              System.out.print(cube ? "#" : ".");
            
          }
            System.out.println();
        }
    }

    private int[] getVector(Command command) {
        return getVector(command, 1);
    }

    private int[] getVector(Command command, int value) {
        if (command.dir == 'U') {
            return new int[]{-1 * value, 0};
        } else if (command.dir == 'D') {
            return new int[]{value, 0};
        } else if (command.dir == 'L') {
            return new int[]{0, -1 * value};
        } else if (command.dir == 'R') {
            return new int[]{0, value};
        }
        else throw new RuntimeException();
    }
    
    private Command getCommand(String line, boolean second) {
        var parts = line.split(" ");
        Character dir = second ? (parts[2].charAt(7) == '0' ? 'R' : parts[2].charAt(7) == '1' ? 'D' : parts[2].charAt(7) == '2' ? 'L' : 'U') : parts[0].charAt(0);
        Integer value = second ? Integer.parseInt(parts[2].substring(2, 7),16) : Integer.parseInt(parts[1]);
        return new Command(dir, value);
    }

    @Override
    public int getDay() {
        return 18;
    }

    //            // Above, no intersection 
//            if (area.start.x >= newSection.start.x + newSection.value) {
//                return List.of(area);
//            }
//            // below, no intersection
//            if (area.start.x + area.value <= newSection.start.x) {
//                return List.of(area);
//            }
//            // newSection totally masks area 
//            if (area.start.x >= newSection.start.x && area.start.x + area.value <= newSection.start.x + newSection.value) {
//                return Collections.emptyList();
//            }
//            // area masks newSection 
//            if (area.start.x < newSection.start.x && area.start.x + area.value > newSection.start.x + newSection.value) {
//                return List.of(
//                        new Section((new Point(area.start.x, newSection.start.y)), (newSection.start.x - area.start.x - 1)),
//                        new Section((new Point(newSection.start.x + newSection.value, newSection.start.y)), (area.value - newSection.value - (newSection.start.x - area.start.x) - 1)));
//            }
//            if (newSection.start.x < area.start.x) {
//                return List.of(new Section((new Point(area.start.x, newSection.start.y)), newSection.start.x + newSection.value - area.start.x));
//            } else {
//                return List.of(new Section((new Point(newSection.start.x, newSection.start.y)), area.start.x + area.value - newSection.start.x));
//            }
}
