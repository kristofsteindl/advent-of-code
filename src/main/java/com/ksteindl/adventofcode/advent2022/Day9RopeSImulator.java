package com.ksteindl.adventofcode.advent2022;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Day9RopeSImulator extends Puzzle2022 {
    
    private List<Step> moves;
    private Bridge bridge;

    public static void main(String[] args) {
        new Day9RopeSImulator().printSolutions();
    }


    @Override
    protected Number getSecondSolution(List<String> lines) {
        return getSolution(lines, 10);
    }
    
    @Override
    protected Number getFirstSolution(List<String> lines) {
        return getSolution(lines, 2);
    }
    
    private Number getSolution(List<String> lines, int ropeLength) {
        bridge = new Bridge(lines.size() > 50 ? 500 : 20, ropeLength);
        moves = lines.stream().map(line -> new Step(Direction.valueOf(line.split(" ")[0]), Integer.valueOf(line.split(" ")[1]))).collect(Collectors.toList());
        for (Step step : moves) {
            move(step);
        }
        int sumVisited = 0;
        for (int i = 0; i < bridge.visited.length; i++) {
            for (int j = 0; j < bridge.visited[0].length; j++) {
                if (bridge.visited[i][j]) {
                    sumVisited++;
                }
            }
        }
        return sumVisited;
    }
    

    private void move(Step step) {
        for (int i = 0; i < step.distance; i++) {
            Point newHead = bridge.rope.getFirst().clone();
            bridge.rope.removeFirst();
            bridge.rope.addFirst(newHead);
            switch (step.direction) {
                case D:
                    newHead.x++;
                    break;
                case U:
                    newHead.x--;
                    break;
                case R:
                    newHead.y++;
                    break;
                case L:
                    newHead.y--;
                    break;
            }
            int order = 1;
            while (moveRope(order++));
            if (bridge.visited.length < 50 ) {
                print();  
            }
            
        }

    }

    private boolean moveRope(int tailOrder) {
        Point newHead = bridge.rope.get(tailOrder - 1);
        Point tail = bridge.rope.get(tailOrder);
        boolean needHorizontal = Math.abs(newHead.x - tail.x) > 1;
        boolean needDiagonal = Math.abs(newHead.y - tail.y) > 1;
        if (!needHorizontal && !needDiagonal) {
            return false;
        }
        Point newTail = tail.clone();
        if (newHead.x == tail.x) {
            newTail.y = newHead.y + (newHead.y > tail.y ? - 1 : + 1);
        } else if (newHead.y == tail.y) {
            newTail.x = newHead.x + (newHead.x > tail.x ? - 1 : + 1);
        } else if (newHead.x - tail.x == 2 && newHead.y - tail.y == 2) {
            newTail.y = newHead.y - 1;
            newTail.x = newHead.x - 1;
        } else if (newHead.x - tail.x == 2 && newHead.y - tail.y == -2) {
            newTail.y = newHead.y + 1;
            newTail.x = newHead.x - 1;
        } else if (newHead.x - tail.x == - 2 && newHead.y - tail.y == 2) {
            newTail.y = newHead.y - 1;
            newTail.x = newHead.x + 1;
        } else if (newHead.x - tail.x == - 2 && newHead.y - tail.y == - 2) {
            newTail.y = newHead.y + 1;
            newTail.x = newHead.x + 1;
        
    } else if (newHead.x - tail.x == 2) {
            newTail.y = newHead.y;
            newTail.x = newHead.x - 1;
        } else if (newHead.x - tail.x == -2) {
            newTail.y = newHead.y;
            newTail.x = newHead.x + 1;
        } else if (newHead.y - tail.y == 2) {
            newTail.x = newHead.x;
            newTail.y = newHead.y - 1;
        } else if (newHead.y - tail.y == -2) {
            newTail.x = newHead.x;
            newTail.y = newHead.y + 1;
        }
        else {
            throw new RuntimeException();
        }
        bridge.rope.remove(tailOrder);
        bridge.rope.add(tailOrder, newTail);
        if (tailOrder == bridge.rope.size() - 1) {
            int lastX = bridge.rope.getLast().x;
            int lastY = bridge.rope.getLast().y;
            bridge.visited[lastX][lastY] = true;
            return false;
        }
        return true;
    }
    
    private void print() {
        System.out.println("-----------------------------------------------------------------------");
        for (int i = 0; i < bridge.visited.length; i++) {
            for (int j = 0; j < bridge.visited[0].length; j++) {
                int order = getOrder(i, j, bridge.rope);
                if (i == bridge.rope.getFirst().x && j == bridge.rope.getFirst().y) {
                    System.out.print("H");
                } else if (order > 0) {
                    System.out.print(order);
                } else if (bridge.visited[i][j]) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println("");
        }
        System.out.println("-----------------------------------------------------------------------");
    }
    
    int getOrder(int x, int y, LinkedList<Point> rope) {
        Iterator<Point> iterator = rope.iterator();
        iterator.next();
        int order = 1;
        while (iterator.hasNext()) {
            Point next = iterator.next();
            if (next.x == x && next.y == y) {
                return order;
            }
            order++;
        }
        return -1;
    }

    @Override
    public int getDay() {
        return 9;
    }
    
    static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public Point clone() {
            return new Point(x, y);
        }
    }
    
    static class Bridge {
        LinkedList<Point> rope;
        boolean[][] visited;

        public Bridge(int mapCapacity, int ropeLength) {
            visited = new boolean[2 * mapCapacity][2 * mapCapacity];
            visited[mapCapacity - 1][mapCapacity - 1] = true;
            rope = new LinkedList<>();
            for (int i = 0; i < ropeLength; i++) {
                rope.add(new Point(mapCapacity - 1, mapCapacity - 1));
            }
        }
    }
    
    enum Direction { R, L, U, D}
    
    static class Step {
        Direction direction;
        Integer distance;

        public Step(Direction direction, Integer distance) {
            this.direction = direction;
            this.distance = distance;
        }
    }
}
