package com.ksteindl.adventofcode.advent2020.day04;

import com.ksteindl.adventofcode.advent2020.Puzzle2020;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PassportScanner extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(PassportScanner.class);

    private static final int DAY = 4;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;

    private static String ENTRY_SPLIT_REGEX = " ";
    private static String KEY_VALUE_SPLIT_REGEX = ":";
    private static final String HEX_WEBCOLOR_PATTERN = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$";
    private static final List<String> VALID_PASSWORD_KEYS = new ArrayList<>(Arrays.asList(
            //"cid"
            "byr",
            "iyr",
            "eyr",
            "hgt",
            "hcl",
            "ecl",
            "pid"
    ));
    private static final Set<String> VALID_EYE_COLORS = new HashSet<>(Arrays.stream(new String[] {
            "amb",
            "blu",
            "brn",
            "gry",
            "grn",
            "hzl",
            "oth"}
    ).collect(Collectors.toSet()));

    public PassportScanner(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    @Override
    public Number getFirstSolution() {
        return getValidPassportCount(this::isValidSolutionOne);
    }

    @Override
    public Number getSecondSolution() {
        return getValidPassportCount(this::isValidSolutionTwo);
    }

    @Override
    public int getDay() {
        return DAY;
    }

    private int getValidPassportCount(Predicate<Map<String, String>> validator) {
        List<String> lines = fileManager.parseLines(fileName);
        List<List<String>> rawPassports = new ArrayList<>();
        List<String> rawPassport = new ArrayList<>();
        for (String line : lines) {
            if (!line.equals("")) {
                rawPassport.add(line);
            } else {
                List<String> found = new ArrayList<>(rawPassport);
                rawPassports.add(found);
                rawPassport.clear();
            }
        }
        rawPassports.add(rawPassport);
        logger.debug(rawPassports.toString());
        List<Map<String, String>> passports = rawPassports.stream().map(rawPass -> createPassport(rawPass)).collect(Collectors.toList());
        int validPassportCount = (int) passports.stream().filter(validator).count();
        logger.debug(validPassportCount);
        return validPassportCount;
    }

    private boolean isValidSolutionOne(Map<String, String> passport) {
        int validKeysFound = (int) VALID_PASSWORD_KEYS.stream().filter(pwKey -> passport.containsKey(pwKey)).count();
        return validKeysFound == VALID_PASSWORD_KEYS.size();
    }

    private boolean isValidSolutionTwo(Map<String, String> passport) {
        if (!isValidSolutionOne(passport)) {
            return false;
        }
        return
                validateValue(passport.get("byr"), 1920, 2002) &&
                        validateValue(passport.get("iyr"), 2010, 2020) &&
                        validateValue(passport.get("eyr"), 2020, 2030) &&
                        validateHeight(passport.get("hgt")) &&
                        VALID_EYE_COLORS.contains(passport.get("ecl")) &&
                        isValid(passport.get("hcl")) &&
                        validatePid(passport.get("pid"));
    }

    private boolean validatePid(String value) {
        if (value.length() != 9) {
            return false;
        }
        try {
            long pid = Long.parseLong(value);
            return true;
        } catch (NumberFormatException ex) {

        }
        return false;
    }

    private boolean validateValue(String value, int atLeast, int atMost) {
        int number = Integer.parseInt(value);
        return number >= atLeast && number <= atMost;
    }

    private boolean validateHeight(String value) {
        if (value.endsWith("in")) {
            int number = Integer.parseInt(value.split("in")[0]);
            return number >= 59 && number <= 76;
        } else {
            int number = Integer.parseInt(value.split("cm")[0]);
            return number >= 150 && number <= 193;
        }
    }


    private Map<String, String> createPassport(List<String> rawPassport) {
        Map<String, String> passport = new HashMap<>();
        for (String rawPassportLine : rawPassport) {
            for (String rawEntry: rawPassportLine.split(ENTRY_SPLIT_REGEX)) {
                String[] keyValue = rawEntry.split(KEY_VALUE_SPLIT_REGEX);
                passport.put(keyValue[0], keyValue[1]);
            }
        }
        logger.debug(passport.toString());
        return passport;
    }



    private static final Pattern pattern = Pattern.compile(HEX_WEBCOLOR_PATTERN);

    public static boolean isValid(final String colorCode) {
        Matcher matcher = pattern.matcher(colorCode);
        return matcher.matches();
    }


}
