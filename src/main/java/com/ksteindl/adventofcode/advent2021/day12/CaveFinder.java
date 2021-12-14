package com.ksteindl.adventofcode.advent2021.day12;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CaveFinder extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(CaveFinder.class);

    private static final int DAY = 12;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";
    String START = "start";
    String END = "end";
    Set<Path> paths;

    private final String fileName;

    public CaveFinder(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
        List<String> lines = fileManager.parseLines(fileName);
        paths = lines.stream()
                .map(line -> new Path(line.split("-")[0],line.split("-")[1]))
                .collect(Collectors.toSet());
    }

    public static void main(String[] args) {
        CaveFinder day = new CaveFinder(false);
        logger.info(day.getFirstSolution());
        logger.info(day.getSecondSolution());
    }

    @Override
    public Number getSecondSolution() {
        return getDay();
    }

    @Override
    public Number getFirstSolution() {
        List<Route> routes = getStartingRoutes(paths);
        List<Route> prevRoutes = new ArrayList<>(routes);
        do {
            List<Route>  sumExtensions = new ArrayList<>();
            for (int i = 0; i < prevRoutes.size(); i++) {
                Route prevRoute = prevRoutes.get(i);
                if (!prevRoute.get(prevRoute.route.size() - 1).equals(END)) {
                    List<Route> extensions = extend(prevRoute);
                    routes.addAll(extensions);
                    sumExtensions.addAll(extensions); 
                }
            }
            prevRoutes = sumExtensions;
        } while (prevRoutes.size() > 0);
        List<Route> rightOnes = routes.stream()
                .filter(route -> route.get(route.size() - 1).equals(END))
                .collect(Collectors.toList());
        return rightOnes.size();
    }
    
    private List<Route> extend(Route route) {
        String terminal = route.get(route.route.size() - 1);
        return paths.stream()
                .map(path -> getExtended(terminal, path, route))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    
    private Optional<Route> getExtended(String terminal, Path path, Route route) {
        String next = terminal.equals(path.a) ? path.b : (terminal.equals(path.b) ? path.a : null);
        if (next == null) {
            return Optional.empty();
        }
        if (deadEnd(route, next)) {
            return Optional.empty();
        }
        Route extended = new Route(route);
        extended.twice = getTwice(route, next);
        if (route.twice) {
            extended.twice = route.twice;
        }
        extended.add(next);
        return Optional.of(extended);
    }
    
    private boolean getTwice(Route route, String nextCave) {
        for (int i = 0; i < route.size(); i++) {
            String cave = route.get(i);
            if (cave.equals(nextCave) && cave.equals(cave.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean deadEnd(Route route, String nextCave) {
        if (nextCave.equals(START)) {
            return true;
        }
        Optional<String> visitedOpt = route.route.stream()
                .filter(cave -> cave.equals(nextCave))
                .findAny();
        if (!visitedOpt.isPresent()) {
            return false;
        }
        String visited = visitedOpt.get();
        if (visited.equals(visited.toUpperCase())) {
            return false;
        }
        if (!route.twice) {
            return false;
        }
        return true;
    }
    


    private List<Route> getStartingRoutes(Set<Path> paths) {
        List<Route> routes = new ArrayList<>();
        for (Path path :paths) {
            if (path.a.equals(START)) {
                Route route = new Route();
                route.add(path.a);
                route.add(path.b);
                routes.add(route);
            } else if (path.b.equals(START)) {
                Route route = new Route();
                route.add(path.b);
                route.add(path.a);
                routes.add(route);
            }
        }
        return routes;
    }
    
    static class Route {
        List<String> route = new ArrayList<>();
        boolean twice = false;

        public Route() {
        }

        public Route(Route route) {
            this.route = new ArrayList<>(route.route);
        }

        void add(String cave) {
            route.add(cave);
        }

        String get(int i) {
            return route.get(i);
        }

        int size() {
            return route.size();
        }
    }
    
    static class Path {
        final String a;
        final String b;

        public Path(String a, String b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Path path = (Path) o;

            if (!a.equals(path.a)) return false;
            return b.equals(path.b);
        }

        @Override
        public int hashCode() {
            return a.hashCode() * b.hashCode();
        }
    }
    

    @Override
    public int getDay() {
        return DAY;
    }

   


}
