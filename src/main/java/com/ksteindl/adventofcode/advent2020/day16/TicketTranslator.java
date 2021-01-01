package com.ksteindl.adventofcode.advent2020.day16;

import com.ksteindl.adventofcode.Puzzle2020;
import com.ksteindl.adventofcode.advent2020.day16.model.Pair;
import com.ksteindl.adventofcode.advent2020.day16.model.Rule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class TicketTranslator extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(TicketTranslator.class);

    private static final int DAY = 16;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";
    private static String INPUT_TEST_FILE_2 = FOLDER_NAME + "inputDec" + DAY_STRING + "_test2.txt";

    private final String fileName;
    private List<Rule> rules;
    private List<List<Integer>> nearbyTickets;
    private List<Integer> myTicket;


    public TicketTranslator(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
        initTask(fileName);
    }


    @Override
    public Number getFirstSolution() {
        return getTicketScanningErrorRate();
    }

    @Override
    public Number getSecondSolution() {
        return getDepartureValuesProduct();
    }

    @Override
    public int getDay() {
        return DAY;
    }

    private long getDepartureValuesProduct () {
        if (isTest) {
            // With the following test-input, only the algorithm can be tested via debugging. No meaningful result is returned.
            initTask(INPUT_TEST_FILE_2);
        }
        List<List<Integer>> validTickets = nearbyTickets.stream().filter(ticket -> !isInvalid(ticket)).collect(Collectors.toList());
        List<Set<Integer>> ticketColumns = getTicketColumns(validTickets);
        final Map<String, List<Integer>> keyToPotentiallyColumns = getKeyToPotentiallyColumns(ticketColumns);
        final Map<String, Integer> keyToColumns = getKeyToColumns(keyToPotentiallyColumns);
        long AllProduct = keyToColumns.entrySet().stream().filter(entry -> entry.getKey().startsWith("departure")).mapToLong(entry -> myTicket.get(entry.getValue())).reduce(1, (product, element) -> product * element);
        return AllProduct;
    }

    private  Map<String, Integer> getKeyToColumns(final Map<String, List<Integer>> finalKeyToPotentiallyColumns) {
        Integer size = finalKeyToPotentiallyColumns.size();
        Map<String, Integer> keyToColumns = new HashMap<>();
        List<Map.Entry<String, List<Integer>>> keyToPotentiallyColumns  = new ArrayList<>(finalKeyToPotentiallyColumns.entrySet());
        // TODO modifying the elements keyToPotentiallyColumns means finalKeyToPotentiallyColumns is modiyied too. Very not nice, but for getting the right result it doesn't bother us
        while (keyToColumns.size() < size) {
            int index = 0;
            while (keyToPotentiallyColumns.get(index).getValue().size() > 1) {
                index++;
            }
            Map.Entry<String, List<Integer>> entry = keyToPotentiallyColumns.get(index);
            if (entry.getValue().size() == 1) {
                Integer value = entry.getValue().get(0);
                keyToColumns.put(entry.getKey(), value);
                removeValue(keyToPotentiallyColumns, value);
                keyToPotentiallyColumns.remove(entry);
            }
        }
        return keyToColumns;
    }

    private void removeValue(List<Map.Entry<String, List<Integer>>> keyToPotentiallyColumns, Integer value) {
        for (Map.Entry<String, List<Integer>> entry : keyToPotentiallyColumns) {
            entry.getValue().remove(value);
        }
    }


    private Map<String, List<Integer>> getKeyToPotentiallyColumns(List<Set<Integer>> ticketColumns) {
        Map<String, List<Integer>> keyToPotentiallyColumns = new HashMap<>();
        for (int i = 0; i < ticketColumns.size(); i++) {
            Set<Integer> ticketColumn = ticketColumns.get(i);
            for (Rule rule : rules) {
                if (isPotentialValidRuleForColumn(rule, ticketColumn)) {
                    if (keyToPotentiallyColumns.get(rule.getKey()) == null) {
                        keyToPotentiallyColumns.put(rule.getKey(), new ArrayList<>());
                    }
                    keyToPotentiallyColumns.get(rule.getKey()).add(Integer.valueOf(i));
                }
            }
        }
        return keyToPotentiallyColumns;
    }

    private boolean isPotentialValidRuleForColumn(Rule rule, Set<Integer> values) {
        return !values.stream().anyMatch(value -> !applyToRule(value, rule));
    }

    private List<Set<Integer>> getTicketColumns(List<List<Integer>> validTickets) {
        List<Set<Integer>> ticketFieldSets = new ArrayList<>();
        for (int i = 0; i < validTickets.get(0).size(); i++) {
            Set<Integer> ticketFieldSet = new TreeSet<>();
            for (int j = 0; j < validTickets.size(); j++) {
                ticketFieldSet.add(validTickets.get(j).get(i));
            }
            ticketFieldSets.add(ticketFieldSet);
        }
        return ticketFieldSets;
    }

    private boolean isInvalid(List<Integer> ticket) {
        return ticket.stream().anyMatch(number -> !foundInAnySection(number));
    }

    private long getTicketScanningErrorRate() {
        List<Integer> invalidNumbers = nearbyTickets.stream().flatMap(List::stream).filter(number -> !foundInAnySection(number)).collect(Collectors.toList());
        return invalidNumbers.stream().mapToInt(Integer::intValue).sum();
    }

    private boolean foundInAnySection(Integer number) {
        return rules.stream().anyMatch(rule -> applyToRule(number, rule));
    }

    private boolean applyToRule(Integer number, Rule rule) {
        Pair firstPair = rule.getFirstPair();
        Pair secondPair = rule.getSecondPair();
        if (firstPair.getLower() <= number && firstPair.getUpper() >= number) {
            return true;
        }
        if (secondPair.getLower() <= number && secondPair.getUpper() >= number) {
            return true;
        }
        return false;
    }


    private void initTask(String fileName) {
        List<String> lines = fileManager.parseLines(fileName);
        rules = getRules(lines);
        myTicket = getTicket(lines.get(rules.size() + 2));
        nearbyTickets = getNearbyTickets(rules.size() + 5, lines);


    }

    private List<List<Integer>> getNearbyTickets(int startIndex, List<String> lines) {
        return lines.subList(startIndex, lines.size()).stream().map(line -> getTicket(line)).collect(Collectors.toList());
    }

    private List<Integer> getTicket(String line) {
        return Arrays.stream(line.split(",")).map(string -> Integer.parseInt(string)).collect(Collectors.toList());
    }

    private List<Rule> getRules(List<String> lines) {
        List<Rule> rules = new ArrayList<>();
        int i = 0;
        do {
            rules.add(createRule(lines.get(i)));
        } while (!lines.get(++i).equals(""));
        return rules;
    }

    private Rule createRule(String line) {
        String[] keyValue = line.split(":");
        String key = keyValue[0];
        String vaules[] = keyValue[1].split(" ");
        String[] rawPari1 = vaules[1].split("-");
        String[] rawPari2 = vaules[3].split("-");
        Pair pair1 = new Pair(Integer.parseInt(rawPari1[0]), Integer.parseInt(rawPari1[1]));
        Pair pair2 = new Pair(Integer.parseInt(rawPari2[0]), Integer.parseInt(rawPari2[1]));
        return new Rule(key, pair1, pair2);
    }


    /*
    *
    * References
    * https://www.baeldung.com/java-stream-reduce
    * https://stackoverflow.com/questions/25147094/how-can-i-turn-a-list-of-lists-into-a-list-in-java-8
    *
    * */
}
