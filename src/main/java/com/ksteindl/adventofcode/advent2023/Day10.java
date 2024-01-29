package com.ksteindl.adventofcode.advent2023;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day10 extends Puzzle2023 {

    public static void main(String[] args) {
        new Day10().printSolutions();
    }

    @Override
    protected Number getSecondSolution(List<String> lines) {
        List<List<Character>> charRows = lines.stream()
                .map(line -> line.chars().mapToObj(e->(char)e).collect(Collectors.toList()))
                .collect(Collectors.toList());
        Maze maze = new Maze(charRows);
        return countIns(maze);
    }

    @Override
    protected Number getFirstSolution(List<String> lines) {
        List<List<Character>> charRows = lines.stream()
                .map(line -> line.chars().mapToObj(e->(char)e).collect(Collectors.toList()))
                .collect(Collectors.toList());
        Maze maze = new Maze(charRows);
        maze.print();
        return maze.longestDistance;
    }
    
    private int countIns(Maze maze) {
        int count = 0;
        for (List<Maze.Tile> row: maze.rows) {
            Counter counter = new Counter();
            while (counter.i < row.size()) {
                if (row.get(counter.i).c == '.' && counter.in) {
                    count++;
                } else {
                    tryToggle(row, counter);
                }
                counter.i++;
            }
        }
        return count;
    }
    
    private static class Counter {
        int i;
        private boolean in;
        
        void toggle() {
            in = !in;
        }
    }
    
    private void tryToggle(List<Maze.Tile> row, Counter counter) {
//        Character potTogInChar1 = counter.in ? 'X' : 'L';
//        Character potTogInChar2 = counter.in ? 'X' : 'F';
//        Character potTogouChar1 = counter.in ? 'X' : '7';
//        Character potTogOutChar2 = counter.in ? 'X' : 'J';
        if (row.get(counter.i).c == '|') {
            counter.toggle();
        } else if (row.get(counter.i).c == 'L') {
            tryToggle(row, counter, '7');
        } else if (row.get(counter.i).c == 'F') {
            tryToggle(row, counter, 'J');
        }
    }
    
    private void tryToggle(List<Maze.Tile> row, Counter counter, Character toggleChar) {
        counter.i++;
        while (row.get(counter.i).c == '-') {
            counter.i++;
        }
        if (row.get(counter.i).c == toggleChar) {
            counter.toggle();
        }
    }
    
    private static class Maze {
        int width;
        int height;
        Tile start;
        List<List<Tile>> rows;
        Graph<Tile, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        int longestDistance = 0;
        

        public Maze(List<List<Character>> charRows) {
            height = charRows.size();
            width = charRows.get(0).size();
            rows = new ArrayList<>();
            for (int i = 0; i < charRows.size(); i++) {
                List<Tile> row = new ArrayList<>();
                for (int j = 0; j < charRows.get(i).size(); j++) {
                    char c = charRows.get(i).get(j);
                    var tile = new Tile(c, j, i);
                    row.add(tile);
                    if (tile.c == 'S') {
                        start = tile;
                    }
                    
                }
                rows.add(row);
            }
            for (List<Tile> row: rows) {
                for (Tile tile : row) {
                    if (tile.c != '.') {
                        graph.addVertex(tile);
                    }
                    
                }
            }
            for (List<Tile> row: rows) {
                for (Tile tile : row) {
                    addNeighbours(tile);
                }
            }
            normalizeAndCountLongestDistance();
        }
        
        void print() {
            for (List<Maze.Tile> row: rows) {
                for (Maze.Tile tile : row) {
                    System.out.print(tile.c);
                }
                System.out.println("");
            }
        }
        
        void normalizeAndCountLongestDistance() {
            DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
            for (List<Maze.Tile> row: rows) {
                for (Maze.Tile tile : row) {
                    if (tile.c != '.') {
                        var path = dijkstraShortestPath.getPath(start,tile);
                        if (path != null) {
                            var shortest = path.getVertexList().size() - 1;
                            if (shortest > longestDistance) {
                                longestDistance = shortest;
                            }
                        } else {
                            tile.c = '.';
                        }
                    }
                }
            }
        }
        
        private void addNeighbours(Tile vertex) {
            if (vertex.c == 'c') {
                return;
            }
            // north
            if (vertex.y > 0) {
                var north = rows.get(vertex.y - 1).get(vertex.x);
                if (north.c == 'S' || north.c == '|' || north.c == '7' || north.c == 'F' ) {
                    if (vertex.c == 'S' || vertex.c == 'J' ||vertex.c == 'L'|| vertex.c == '|' ) {
                        vertex.north = north;
                        graph.addEdge(vertex, north);
                    }
                }
            }
            // south
            if (vertex.y < height - 1) {
                var south = rows.get(vertex.y + 1).get(vertex.x);
                if (south.c == 'S' || south.c == '|' || south.c == 'J' || south.c == 'L' ) {
                    if (vertex.c == 'S' || vertex.c == '7' ||vertex.c == 'F'|| vertex.c == '|' ) {
                        vertex.south = south;
                        graph.addEdge(vertex, south);
                    }
                }
            }
            // east
            if (vertex.x < width - 1) {
                var east = rows.get(vertex.y).get(vertex.x + 1);
                if (east.c == 'S' || east.c == '-' || east.c == 'J' || east.c == '7' ) {
                    if (vertex.c == 'S' || vertex.c == 'F' ||vertex.c == 'L'|| vertex.c == '-' ) {
                        vertex.east = east;
                        graph.addEdge(vertex, east);
                    }
                }
            }
            // west
            if (vertex.x > 0) {
                var west = rows.get(vertex.y).get(vertex.x - 1);
                if (west.c == 'S' || west.c == '-' || west.c == 'F' || west.c == 'L' ) {
                    if (vertex.c == 'S' || vertex.c == 'J' ||vertex.c == '7' || vertex.c == '-' ) {
                        vertex.west = west;
                        graph.addEdge(vertex, west);
                    }
                }
            }
            
        }
        
        private static class Tile {
            Character c;
            int x;
            int y;
            
            Tile north;
            Tile east;
            Tile south;
            Tile west;

            public Tile(Character c, int x, int y) {
                this.c = c;
                this.x = x;
                this.y = y;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Tile tile = (Tile) o;

                if (x != tile.x) return false;
                if (y != tile.y) return false;
                return c.equals(tile.c);
            }

            @Override
            public int hashCode() {
                int result = c.hashCode();
                result = 31 * result + x;
                result = 31 * result + y;
                return result;
            }
        }
    }
    

    @Override
    public int getDay() {
        return 10;
    }
}
