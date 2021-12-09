package com.ksteindl.adventofcode.advent2021.day08;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DigitDecoder extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(DigitDecoder.class);

    private static final int DAY = 8;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;
    List<List<String>> pattern;
    List<List<String>> digits;

    public DigitDecoder(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
        List<String> lines = fileManager.parseLines(fileName);
        pattern = new ArrayList<>();
        digits = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String[] numbers = lines.get(i).split(" ");
            pattern.add(Arrays.stream(numbers).collect(Collectors.toList()).subList(0,10));
            digits.add(Arrays.stream(numbers).collect(Collectors.toList()).subList(11,15));
        }
    }

    public static void main(String[] args) {
        DigitDecoder day = new DigitDecoder(false);
        logger.info(day.getFirstSolution());
        logger.info(day.getSecondSolution());
    }

    @Override
    public Number getFirstSolution() {
        Long result = digits.stream()
                .flatMap(List::stream)
                .filter(n -> n.length() == 2 || n.length() == 3 || n.length() == 4 || n.length() == 7)
                .mapToLong(i -> i.length())
                .count();
        return result;
    }

    @Override
    public Number getSecondSolution() {
        int sum = 0;
        for (int i = 0; i < pattern.size(); i++) {
            Map<String, Integer> decodedDigits = getDecodedDigits(pattern.get(i));
            List<String> digitLine = digits.get(i);
            StringBuilder fourDibitBuilder = new StringBuilder();
            for (int j = 0; j < digitLine.size(); j++) {
                String digit = digitLine.get(j);
                Integer numberDigit = getDigit(decodedDigits, digit);
                fourDibitBuilder.append(numberDigit);
            }
            Integer result = Integer.parseInt(fourDibitBuilder.toString());
            sum += result;
        }
        return sum;
        
    }
    
    private Integer getDigit(Map<String, Integer> decodedDigits, String digit) {
        for (Map.Entry<String, Integer> entry: decodedDigits.entrySet()) {
            if (sameChars(entry.getKey(), digit)) {
                return entry.getValue();
            }
        }
        return null;
    }
    
    private boolean sameChars(String firstStr, String secondStr) {
        char[] first = firstStr.toCharArray();
        char[] second = secondStr.toCharArray();
        Arrays.sort(first);
        Arrays.sort(second);
        return Arrays.equals(first, second);
    }
        
    
    
    private Map<String, Integer> getDecodedDigits(List<String> pattern) {
        Map<String, Integer> decoded = new HashMap<>();
        // Getting 1
        String one = pattern.stream().filter(p -> p.length() == 2).findAny().get();
        decoded.put(one, 1);
        // Getting 7
        String seven = pattern.stream().filter(p -> p.length() == 3).findAny().get();
        decoded.put(seven, 7);
        // Getting 4
        String four = pattern.stream().filter(p -> p.length() == 4).findAny().get();
        decoded.put(four, 4);
        // Getting 8
        String eight = pattern.stream().filter(p -> p.length() == 7).findAny().get();
        decoded.put(eight, 8);
        char a = getPlusChars(seven, one).get(0);
        // Getting 3
        List<String> fivers = pattern.stream().filter(s -> s.length()==5).collect(Collectors.toList());
        String three = fivers.stream().filter(s -> s.indexOf(one.charAt(0)) != -1 && s.indexOf(one.charAt(1)) != -1).findAny().get();
        decoded.put(three, 3);
        // Getting 9
        List<String> sixers = pattern.stream().filter(s -> s.length()==6).collect(Collectors.toList());
        String nine = sixers.stream().filter(s -> isNine(three, s)).findAny().get();
        decoded.put(nine, 9);
        char b = getPlusChars(nine, three).get(0);
        // Getting 5
        String five = fivers.stream().filter(s -> s.indexOf(b) != -1).findAny().get();
        decoded.put(five, 5);
        // Getting 2
        String two = fivers.stream().filter(s -> !s.equals(five) && !s.equals(three)).findAny().get();
        decoded.put(two, 2);
        // Getting 6
        String six = sixers.stream().filter(sixer -> getPlusChars(sixer, seven).size() == 4).findAny().get();
        decoded.put(six, 6);
        // Getting 0
        String zero = pattern.stream().filter(s-> !decoded.containsKey(s)).findAny().get();
        decoded.put(zero, 0);
        
        return decoded;
    }
    
    private boolean isNine(String three, String digit) {
        if (digit.length() != 6) {
            return false;
        }
        for (int i = 0; i < three.length(); i++) {
            if (digit.indexOf(three.charAt(i)) == -1) {
                return false;
            }
        }
        return true;
    }

    
    private List<Character> getPlusChars(String s1, String s2) {
        String bigger = s1.length() > s2.length() ? s1 : s2;
        String smaller = s1.length() < s2.length() ? s1 : s2;
        List<Character> difference = new ArrayList(IntStream.range(0, bigger.toCharArray().length).mapToObj(i -> bigger.toCharArray()[i]).collect(Collectors.toList()));
        for (int i = 0; i < smaller.length(); i++) {
            Character c = smaller.charAt(i);
            difference.remove(c);
        }
        return difference;
        
    }
    

    @Override
    public int getDay() {
        return DAY;
    }

   


}
