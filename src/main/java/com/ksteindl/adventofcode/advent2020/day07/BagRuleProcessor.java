package com.ksteindl.adventofcode.advent2020.day07;

import com.ksteindl.adventofcode.Puzzle2020;
import com.ksteindl.adventofcode.advent2020.day07.pojo.Bag;
import com.ksteindl.adventofcode.advent2020.day07.pojo.Bags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class BagRuleProcessor extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(BagRuleProcessor.class);

    private static final int DAY = 7;
    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";
    private static String INPUT_TEST_FILE_2 = FOLDER_NAME + "inputDec" + DAY_STRING + "_test2.txt";

    private static final Bag SHINY_GOLD_BAG = new Bag("shiny", "gold");

    private final String fileName;

    public BagRuleProcessor(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    @Override
    public Number getFirstSolution() {
        return getBagCountContaningShinyGoldBag();
    }

    @Override
    public Number getSecondSolution() {
        return getBagCountInsideMyShinyGoldBag();
    }

    @Override
    public int getDay() {
        return DAY;
    }

    private int getBagCountContaningShinyGoldBag() {
        List<String> rawRules = fileManager.parseLines(fileName);
        Map<Bag, Set<Bag>> rules = rawRules.stream().collect(Collectors.toMap((rawRule -> getSubjectBag(rawRule)), rawRule -> getRelatedBagSet(rawRule)));
        logRules(rules);
        Set<Bag> bagsForShinyGoldBags = getBagCountContaningShinyGoldBag(rules);
        return bagsForShinyGoldBags.size();
    }

    private int getBagCountInsideMyShinyGoldBag() {
        List<String> rawRules = fileManager.parseLines(fileName);
        Map<Bag, Set<Bags>> rules = rawRules.stream().collect(Collectors.toMap((rawRule -> getSubjectBag(rawRule)), rawRule -> getRelatedBagsSet(rawRule)));
        printBagsRules(rules);
        return countBagsRecursivly(rules, SHINY_GOLD_BAG) -1;
    }

    private int countBagsRecursivly(Map<Bag, Set<Bags>> rules, Bag parentBag) {
        int bagCount = 1;
        Set<Bags> childrenBags = rules.get(parentBag);
        for (Bags bags: childrenBags) {
            bagCount = bagCount + bags.getQuantity() * countBagsRecursivly(rules, bags.getBag());
        }
        return bagCount;
    }


    private Set<Bags> getRelatedBagsSet(String rawRule) {
        String[] rawChunks = rawRule.split(",");
        Set<Bags> relatedBags = new HashSet<>();
        String[] firstRawBags = rawChunks[0].split(" ");
        if (firstRawBags[4].equals("no")) {
            return relatedBags;
        }
        Bags firstBags = new Bags(Integer.parseInt(firstRawBags[4]), firstRawBags[5], firstRawBags[6]);
        logger.debug("first related bag: " + firstBags);
        relatedBags.add(firstBags);
        for (int i = 1; i < rawChunks.length; i++) {
            logger.debug("rawChunk: " + rawChunks[i]);
            String[] rawBag = rawChunks[i].split(" ");
            Bags bags = new Bags(Integer.parseInt(rawBag[1]), rawBag[2], rawBag[3]);
            logger.debug("related bag: " + bags.toString());
            relatedBags.add(bags);
        }
        return relatedBags;
    }

    private Set<Bag> getRelatedBagSet(String rawRule) {
        return getRelatedBagsSet(rawRule).stream().map(bags -> bags.getBag()).collect(Collectors.toSet());
    }

    private Set<Bag> getBagCountContaningShinyGoldBag(Map<Bag, Set<Bag>> rules) {
        Set<Bag> bagsForShinyGoldBags = new HashSet<>();
        Set<Bag> potentiallyBags = new HashSet<>(rules.keySet());
        Set<Bag> newBagsToExamine = null;
        do {
            newBagsToExamine = new HashSet<>();
            for (Bag potentiallyBag : potentiallyBags) {
                for (Bag indirectBag : bagsForShinyGoldBags) {
                    if (rules.get(potentiallyBag).contains(indirectBag)) {
                        newBagsToExamine.add(potentiallyBag);
                        logger.debug("in bag " + potentiallyBag + "found indirect bag bag" );
                    }
                }
                if (rules.get(potentiallyBag).contains(SHINY_GOLD_BAG)) {
                    newBagsToExamine.add(potentiallyBag);
                    logger.debug("in bag " + potentiallyBag + "found shiny gold bag" );
                }

            }
            bagsForShinyGoldBags.addAll(newBagsToExamine);
            potentiallyBags.removeAll(newBagsToExamine);
        } while (newBagsToExamine.size() > 0);
        return bagsForShinyGoldBags;
    }


    private Bag getSubjectBag(String rawRule) {
        String rawSubject = rawRule.split("bag")[0];
        String[] rawBag = rawSubject.split(" ");
        Bag subjectBag = new Bag(rawBag[0], rawBag[1]);
        logger.debug("subject bag: " + subjectBag.toString());
        return subjectBag;
    }

    private void printBagsRules(Map<Bag, Set<Bags>> rules) {
        rules.entrySet().forEach(entry -> {
            logger.debug(entry.getKey().toString());
            logger.debug(entry.getValue().toString());
            logger.debug("-----------");
        });
    }

    private void logRules(Map<Bag, Set<Bag>> rules) {
        rules.entrySet().forEach(entry -> {
            logger.debug(entry.getKey().toString());
            logger.debug(entry.getValue().toString());
            logger.debug("-----------");
        });
    }





    /*
    *
    * References
    * https://www.baeldung.com/java-collectors-tomap
    *
    * */
}
