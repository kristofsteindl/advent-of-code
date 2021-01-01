package com.ksteindl.adventofcode.advent2020.day19;

import com.ksteindl.adventofcode.Puzzle2020;
import com.ksteindl.adventofcode.advent2020.day19.model.BranchRule;
import com.ksteindl.adventofcode.advent2020.day19.model.LeafRule;
import com.ksteindl.adventofcode.advent2020.day19.model.MatchResult;
import com.ksteindl.adventofcode.advent2020.day19.model.Rule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MessageValidator extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(MessageValidator.class);

    private static final int DAY = 19;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";
    private static String INPUT_TEST_FILE2 = FOLDER_NAME + "inputDec" + DAY_STRING + "_test2.txt";

    private final String fileName;
    private Map<Integer, Rule> rootRules;
    private List<String> messages;

    public MessageValidator(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    @Override
    public Number getFirstSolution() {
        init(fileName);
        return getValidMessages().size();
    }

    @Override
    public Number getSecondSolution() {
        init(isTest ? INPUT_TEST_FILE2 : fileName);
        changeSomeRulesAccordingToPartTwo();
        return getValidMessages().size();
    }

    @Override
    public int getDay() {
        return DAY;
    }

    private void init(String fileName) {
        final List<String> lines = fileManager.parseLines(fileName);
        int blankLineIndex = getBlankLineIndex(lines);
        this.rootRules = createRootRules(lines, blankLineIndex);
        this.messages = lines.subList(blankLineIndex + 1, lines.size());

    }

    private void changeSomeRulesAccordingToPartTwo() {

        BranchRule branchRule8 = (BranchRule) rootRules.get(8);
        List<Rule> singleRules8 = new ArrayList<>();
        singleRules8.add(rootRules.get(42));
        singleRules8.add(rootRules.get(8));
        branchRule8.addRules(singleRules8);

        BranchRule branchRule11 = (BranchRule) rootRules.get(11);
        List<Rule> singleRules11 = new ArrayList<>();
        singleRules11.add(rootRules.get(42));
        singleRules11.add(rootRules.get(11));
        singleRules11.add(rootRules.get(31));
        branchRule11.addRules(singleRules11);
    }

    private Set<String> getValidMessages() {
        logger.debug("Valid messages:");
        return messages.stream()
               .filter(message -> isValid(message))
               .collect(Collectors.toSet());
    }

    private boolean isValid(String message) {
        MatchResult matchResult = rootRules.get(0).consume(message);
        logger.debug("message: " + message + ", matchResult: " + matchResult);
        return matchResult.isValid() && matchResult.getLeftOver().isEmpty();
    }

    private Map<Integer, Rule> createRootRules(List<String> lines, int blankLineIndex) {
        Map<Integer, Rule> rootRules = createRootRulesSkeleton(lines, blankLineIndex);
        for (int i = 0; i < blankLineIndex; i++) {
            String line = lines.get(i);
            String[] keyValue = line.split(":");
            Integer id = Integer.parseInt(keyValue[0]);
            String value = keyValue[1];
            if (value.charAt(1) != '"') {
                BranchRule rootRule = (BranchRule) rootRules.get(id);
                String[] options = value.split(Pattern.quote("|"));
                for (String option: options) {
                    List<Rule> rules = Arrays.stream(option.split(" ")).filter(string -> !string.isEmpty()).map(string -> rootRules.get(Integer.parseInt(string))).collect(Collectors.toList());
                    rootRule.addRules(rules);
                }
            }
        }
        logRootRules(rootRules);
        return rootRules;
    }

    private Map<Integer, Rule> createRootRulesSkeleton(List<String> lines, int blankLineIndex) {
        Map<Integer, Rule> rootRules = new HashMap<>();
        for (int i = 0; i < blankLineIndex; i++) {
            String line = lines.get(i);
            String[] keyValue = line.split(":");
            Integer id = Integer.parseInt(keyValue[0]);
            String value = keyValue[1];
            if (value.charAt(1) == '"') {
                rootRules.put(id, new LeafRule(id, value.charAt(2)));
            } else {
                rootRules.put(id, new BranchRule(id));
            }
        }
        return rootRules;
    }

    private static int getBlankLineIndex(List<String> lines) {
        int blankLineINdex = 0;
        while (!lines.get(blankLineINdex).isEmpty()) {
            blankLineINdex++;
        }
        return blankLineINdex;
    }

    private void logRootRules(Map<Integer, Rule> rootRules) {
        rootRules.entrySet().forEach(keyValue -> logger.debug(keyValue.getKey() + ", rules: " + keyValue.getValue().toString()));
    }


    /*
    *
    * References
    *
    * */
}
