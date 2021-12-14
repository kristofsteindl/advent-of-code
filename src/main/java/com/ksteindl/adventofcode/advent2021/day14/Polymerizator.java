package com.ksteindl.adventofcode.advent2021.day14;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import com.ksteindl.adventofcode.advent2021.day13.ThermometerManual;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class Polymerizator extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(Polymerizator.class);

    private static final int DAY = 14;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;

    public static void main(String[] args) {
        Polymerizator day = new Polymerizator(false);
        logger.info(day.getFirstSolution());
        logger.info(day.getSecondSolution());
    }

    public Polymerizator(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    @Override
    public Number getSecondSolution() {
        List<String> lines = fileManager.parseLines(fileName);
        Map<PositionedPair, AtomicLong> template = getTemplate();
        
        Map<Reaction, Character> reactions = lines.subList(2, lines.size()).stream()
                .map(line -> new Reaction(line.charAt(0), line.charAt(1), line.charAt(6)))
                .collect(Collectors.toMap(r->r, r->r.product));

        Map<PositionedPair, AtomicLong> prevTemplate = new HashMap<>(template);
        for (int i = 0; i < 40; i++) {
            Map<PositionedPair, AtomicLong> nextProducts = new HashMap<>();
            for (PositionedPair pair : prevTemplate.keySet()) {
                Reaction reaction = new Reaction(pair.r1, pair.r2);
                Character newProduct = reactions.get(reaction);

                PositionedPair p1 = new PositionedPair(pair.prev, pair.r1, newProduct, pair.r2);
                if (nextProducts.get(p1) == null) {
                    nextProducts.put(p1, new AtomicLong());
                }
                nextProducts.get(p1).addAndGet(prevTemplate
                        .get(pair)
                        .get());

                PositionedPair p2 = new PositionedPair(pair.r1, newProduct, pair.r2, pair.next);
                if (nextProducts.get(p2) == null) {
                    nextProducts.put(p2, new AtomicLong());
                }
                nextProducts.get(p2).addAndGet(prevTemplate.get(pair).get());
                
                
                
            }
            prevTemplate = nextProducts;
            

        }
        Map<Character, AtomicLong> result = new HashMap<>();
        for (Map.Entry<PositionedPair, AtomicLong> entry : prevTemplate.entrySet()) {
            if(!result.containsKey(entry.getKey().r1)) {
                result.put(entry.getKey().r1, new AtomicLong());
            }
            if(!result.containsKey(entry.getKey().r2)) {
                result.put(entry.getKey().r2, new AtomicLong());
            }
            if (entry.getKey().prev == null) {
             result.get(entry.getKey().r1).addAndGet(entry.getValue().get() * 2);
            } else {
             result.get(entry.getKey().r1).addAndGet(entry.getValue().get());
            }
             
            if (entry.getKey().next == null) {
                result.get(entry.getKey().r2).addAndGet(entry.getValue().get() * 2);
            } else {
                result.get(entry.getKey().r2).addAndGet(entry.getValue().get());
            }
        }
        List<Long> sorted = result.entrySet().stream()
                .sorted((entry1, entry2) -> entry1.getValue().get() > entry2.getValue().get() ? -1 : 1)
                .map(entry -> entry.getValue().get() / 2)
                .collect(Collectors.toList());
        return sorted.get(0) - sorted.get(sorted.size() - 1);

    }
    
    private Map<PositionedPair, AtomicLong> getTemplate() {
        List<String> lines = fileManager.parseLines(fileName);
        char[] charTemplate = lines.get(0).toCharArray();
        Map<PositionedPair, AtomicLong> template = new HashMap<>();
        template.put(new PositionedPair(null, charTemplate[0], charTemplate[1], charTemplate[2]), new AtomicLong(1));
        for (int i = 1; i < charTemplate.length -2; i++) {
            PositionedPair lastPair = new PositionedPair(
                    charTemplate[i - 1],
                    charTemplate[i],
                    charTemplate[i + 1],
                    charTemplate[i + 2]);
            if (template.get(lastPair) == null) {
                template.put(lastPair, new AtomicLong());
            }
            template.get(lastPair).incrementAndGet();
        }
        PositionedPair lastPair = new PositionedPair(charTemplate[charTemplate.length - 3], charTemplate[charTemplate.length - 2], charTemplate[charTemplate.length - 1], null);
        if (template.get(lastPair) == null) {
            template.put(lastPair, new AtomicLong());
        }
        template.get(lastPair).incrementAndGet();
        return template;
    }


    private static class PositionedPair {
        final Character prev;
        final Character r1;
        final Character r2;
        final Character next;

        public PositionedPair(Character prev, Character r1, Character r2, Character next) {
            this.prev = prev;
            this.r1 = r1;
            this.r2 = r2;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PositionedPair that = (PositionedPair) o;

            if (!r1.equals(that.r1)) return false;
            if (!r2.equals(that.r2)) return false;
            if (prev != null ? !prev.equals(that.prev) : that.prev != null) return false;
            return next != null ? next.equals(that.next) : that.next == null;
        }

        @Override
        public int hashCode() {
            int result = r1.hashCode();
            result = 31 * result + r2.hashCode();
            result = 31 * result + (prev != null ? prev.hashCode() : 0);
            result = 31 * result + (next != null ? next.hashCode() : 0);
            return result;
        }
    }



    @Override
    public Number getFirstSolution() {
        List<String> lines = fileManager.parseLines(fileName);
        char[] charTemplate = lines.get(0).toCharArray();
        List<Character> template = new ArrayList<>();
        for (Character character : charTemplate) {
            template.add(character);
        }
        Map<Reaction, Character> reactions = lines.subList(2, lines.size()).stream()
                .map(line -> new Reaction(line.charAt(0), line.charAt(1), line.charAt(6)))
                .collect(Collectors.toMap(r->r, r->r.product));
        List<Character> prevTemplate = new ArrayList<>(template);
        for (int i = 0; i < 10; i++) {
            List<Character> newProducts = new ArrayList<>();
            for (int j = 1; j < prevTemplate.size(); j++) {
                Character reagant1 = prevTemplate.get(j-1);
                Character reagant2 = prevTemplate.get(j);
                Reaction reaction = new Reaction(reagant1, reagant2);
                Character product = reactions.get(reaction);
                newProducts.add(product);
            }
            List<Character> newTemplate = new ArrayList<>();
            newTemplate.add(prevTemplate.get(0));
            for (int j = 1; j < prevTemplate.size(); j++) {
                newTemplate.add(newProducts.get(j-1));
                newTemplate.add(prevTemplate.get(j));
            }
            prevTemplate = newTemplate;

        }
        Map<Character, List<Character>> grouped = prevTemplate.stream().collect(groupingBy(c -> c));
        List<List<Character>> sorted = grouped.values().stream().sorted((list1, list2) -> list1.size() > list2.size() ? -1 : 1).collect(Collectors.toList());
        return sorted.get(0).size() - sorted.get(sorted.size() - 1).size();
    }
    
    private static class Reaction {
        final Character r1;
        final Character r2;
        Character product;

        public Reaction(Character r1, Character r2) {
            this.r1 = r1;
            this.r2 = r2;
        }

        public Reaction(Character r1, Character r2, Character product) {
            this.r1 = r1;
            this.r2 = r2;
            this.product = product;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Reaction reaction = (Reaction) o;

            if (r1 != null ? !r1.equals(reaction.r1) : reaction.r1 != null) return false;
            return r2 != null ? r2.equals(reaction.r2) : reaction.r2 == null;
        }

        @Override
        public int hashCode() {
            int result = r1 != null ? r1.hashCode() : 0;
            result = 31 * result + (r2 != null ? r2.hashCode() : 0);
            return result;
        }
    }
    

    @Override
    public int getDay() {
        return DAY;
    }

   


}
