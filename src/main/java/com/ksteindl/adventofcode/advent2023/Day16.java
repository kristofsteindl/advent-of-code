package com.ksteindl.adventofcode.advent2023;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day16 extends Puzzle2023 {

    public static void main(String[] args) {
        new Day16().printSolutions();
    }
    
    @Override
    protected Number getFirstSolution(List<String> lines) {
        Board board = new Board(lines);
        board.walk(0);
        return board.countEnergized();
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        int max = 0;
        for (int i = 0; i < 2 * (lines.size() + lines.get(0).length()); i++) {
            Board board = new Board(lines);
            board.walk(i);
            var count = board.countEnergized();
            if (count > max) {
                max = count;
            }
        }

        return max;
    }
    
    private static class Beam {
        Board board;
        int x;
        int y;
        int direction; // 0-NORTH, 1-EAST, 2-SOUTH, 3-WEST 

        public Beam(Board board, int x, int y, int direction) {
            this.board = board;
            this.x = x;
            this.y = y;
            this.direction = direction;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Beam beam = (Beam) o;

            if (x != beam.x) return false;
            if (y != beam.y) return false;
            return direction == beam.direction;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + direction;
            return result;
        }

        List<Beam> advanceTillSplit() {
            while (true) {
                if (!advanceOneStep()) {
                    return List.of();
                } else {
                    var tile = board.rows.get(x).get(y);
                    char type = tile.type;
                    if (direction % 2 == 1) {
                        if (type == '|') {
                            return newBeams(x, y, 0);
                        } else if (type == '/') {
                            direction -= 1; 
                        } else if (type == '\\') {
                            direction = (direction + 1) % 4;
                        }
                    } else if (direction % 2 == 0) {
                        if (type == '-') {
                            return newBeams(x, y, 1);
                        } else if (type == '/') {
                            direction += 1;
                        } else if (type == '\\') {
                            direction = direction == 0 ? 3 : 1;
                        }
                        
                    } 
                }
            }
        }
        
        List<Beam> newBeams(int x, int y, int minDir) {
            return List.of(new Beam(board, x, y, minDir), new Beam(board, x, y, minDir + 2));
        }
        
        boolean advanceOneStep() {
            if (direction == 1) {
                if (y == board.rows.get(0).size() - 1) {
                    return false;
                }
                y++;
            }
            if (direction == 3) {
                if (y == 0) {
                    return false;
                }
                y--;
            }
            if (direction == 0) {
                if (x == 0) {
                    return false;
                }
                x--;
            }
            if (direction == 2) {
                if (x == board.rows.size() - 1) {
                    return false;
                }
                x++;
            }
            Tile newTile = board.rows.get(x).get(y);
            boolean newDirTypeAdded = newTile.directionVisited.add(direction % 2);
            if (newTile.type == '|') {
                return newDirTypeAdded || (direction % 2) == 0;
            }
            if (newTile.type == '-') {
                return newDirTypeAdded || (direction % 2) == 1;
            }
            return true;
        }
    }

    private static class Board {
        List<List<Tile>> rows;

        public Board(List<String> lines) {
            rows = IntStream.range(0, lines.size())
                    .mapToObj(row -> IntStream.range(0, lines.get(row).length())
                            .mapToObj(column -> new Tile(row, column, lines.get(row).charAt(column)))
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());
        }
        
        void walk(int n) {
            Beam startingBeam = getFirstBeam(n);
            List<Beam> activeBeams = new ArrayList<>();
            activeBeams.add(startingBeam);
            while (!activeBeams.isEmpty()) {
                Beam active = activeBeams.remove(0);
                activeBeams.addAll(active.advanceTillSplit());
            }
        }
        
        Beam getFirstBeam(int n) {
            int startingX;
            int startingY;
            int dir;
            if (n < rows.size()) {
                startingX = n;
                startingY = -1;
                dir = 1;
            } else if (n > rows.size() && n < (rows.size() + rows.get(0).size())) {
                startingX = rows.size();
                startingY = n - rows.size();
                dir = 0; 
            } else if (n > rows.get(0).size() && n < 2 * rows.size()) {
                startingX = n - rows.get(0).size() - rows.size();
                startingY = rows.get(0).size();
                dir = 3;
            } else if (n > rows.get(0).size() + 2 * rows.size()){
                startingX = -1;
                startingY = n - rows.get(0).size() - 2 * rows.size();
                dir = 2;
            } else {
                startingX = 0;
                startingY = -1;
                dir = 1;  
            }
            Beam startingBeam = new Beam(this, startingX, startingY, dir);
            return startingBeam;
        }
        
        int countEnergized() {
            return rows.stream().mapToInt(row -> row.stream().mapToInt(tile -> tile.directionVisited.isEmpty() ? 0 : 1).sum()).sum();
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < rows.size(); i++) {
                List<Tile> line = rows.get(i);
                int k = i + 1;
                builder.append(k > 99 ? k : k > 9 ? " " + k : "  " + k);
                for (int j = 0; j < line.size(); j++) {
                    Tile tile = line.get(j);
                    if (!tile.directionVisited.isEmpty() && line.get(j).type == '.') {
                        builder.append("#");
                    } else {
                        builder.append(line.get(j).type);
                    }
                    
                }
                builder.append("\n");
            }
            return builder.toString();
        }
    }
    
    private static class Tile {
        int x;
        int y;
        char type;
        Set<Integer> directionVisited = new HashSet<>();

        public Tile(int x, int y, char type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }
    
    @Override
    public int getDay() {
        return 16;
    }
}
