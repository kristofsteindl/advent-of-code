package com.ksteindl.adventofcode.advent2021.day18;

import com.ksteindl.adventofcode.advent2021.Puzzle2021;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class NumberAdder extends Puzzle2021 {

    private static final Logger logger = LogManager.getLogger(NumberAdder.class);

    private static final int DAY = 18;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;
    private List<List<Element>> elementRows;

    public NumberAdder(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
        List<String> lines = fileManager.parseLines(fileName);
        elementRows = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int index = 0;
            List<Element> elements = new ArrayList<>();
            while (index < line.length()) {
                if (line.charAt(index) == '[') {
                    elements.add(new Element(Type.OPEN));
                } else if (line.charAt(index) == ']') {
                    elements.add(new Element(Type.CLOSE));
                } else if (line.charAt(index) == ',') {
                    elements.add(new Element(Type.COLON));
                } else {
                    elements.add((new Element(Type.NUMBER, Integer.parseInt(line.substring(index, index + 1)))));
                }
                index++;
            }
            elementRows.add(elements);
        }
    }

    public static void main(String[] args) {
        NumberAdder day = new NumberAdder(false);
        logger.info(day.getFirstSolution());
        logger.info(day.getSecondSolution());
    }

    @Override
    public Number getSecondSolution() {
        long max = 0;
        for (int leftIndex = 0; leftIndex < elementRows.size(); leftIndex++) {
            for (int rightIndex = 0; rightIndex < elementRows.size(); rightIndex++) {
                List<Element> left = elementRows.get(leftIndex);
                List<Element> right = elementRows.get(rightIndex);
                List<Element> added = addTwoLines(left, right);
                long sum = getMagnitude(added);
                if (leftIndex != rightIndex && sum > max) {
                    print(left);
                    print(right);
                    max = sum;
                }
            }
        }
        return max;
    }

    @Override
    public Number getFirstSolution() {
        List<Element> first = elementRows.get(0);
        for (int row = 1; row < elementRows.size(); row++) {
            List<Element> second = elementRows.get(row);
            List<Element> added = addTwoLines(first, second);
            first = added;
        }
        return getMagnitude(first);
    }
    
    private List<Element> addTwoLines(List<Element> first, List<Element> second) {
//        System.out.println("Before addition, sum: ");
//        print(first);
        List<Element> added = new ArrayList<>();
        added.add(new Element(Type.OPEN));
        added.addAll(first);
        added.add(new Element(Type.COLON));
        added.addAll(second);
        added.add(new Element(Type.CLOSE));
//        System.out.println("After addition, before reduction: ");
//        print(added);
        while (reduce(added)) {
        }
        return added;
    }
    
    private boolean reduce(List<Element> added) {
        boolean shouldReduce = false;
        int index = 0;
        int open = 0;
        while (!shouldReduce && index < added.size()) {
            Element element = added.get(index);

            if (element.type == Type.OPEN) {
                open++;
            } else if (element.type == Type.CLOSE) {
                open--;
            }
            if (open == 5) {
                shouldReduce = true;
                explode(added, index);
//                System.out.println("After explosion: ");
//                print(added);
            }
            index++;
        }
        index = 0;
        while (!shouldReduce && index < added.size()) {
            Element element = added.get(index);
            if (element.type == Type.NUMBER && element.value > 9) {
                shouldReduce = true;
                added.remove(index);
                split(added, index, element);
//                System.out.println("After split: ");
//                print(added);
            }
            index++;
        }
        return shouldReduce;
    }
    
    private void split(List<Element> added, Integer index, Element element) {
        Integer tenSmth = element.value;
        int firstHalf = tenSmth / 2;
        int secondHalf = firstHalf * 2 == tenSmth ? firstHalf : firstHalf + 1;
        added.add(index, new Element(Type.CLOSE));
        added.add(index, new Element(Type.NUMBER, secondHalf));
        added.add(index, new Element(Type.COLON));
        added.add(index, new Element(Type.NUMBER, firstHalf));
        added.add(index, new Element(Type.OPEN));
    }
    
    private void explode(List<Element> added, int index) {
        Integer left = added.get(index + 1).value;
        Integer right = added.get(index + 3).value;
        for (int leftIndex = index; leftIndex >= 0; leftIndex--) {
            if (added.get(leftIndex).type == Type.NUMBER) {
                added.set(leftIndex, new Element(Type.NUMBER, added.get(leftIndex).value + left));
                break;
            }
        }
        for (int rightIndex = index + 4; rightIndex < added.size(); rightIndex++) {
            if (added.get(rightIndex).type == Type.NUMBER) {
                added.set(rightIndex, new Element(Type.NUMBER, added.get(rightIndex).value + right));
                break;
            }
        }
        for (int i = 0; i < 5; i++) {
            added.remove(index);
        }
        added.add(index, new Element(Type.NUMBER, 0));
    }

    private long getMagnitude(List<Element> elements) {
        while (elements.size() > 1) {
//            System.out.println("Before magnitude reduction: ");
//            print(elements);
            int index = elements.size() - 1;
            while (index >= 0) {
                if (
                        elements.get(index).type == Type.CLOSE &&
                                elements.get(index - 1).type == Type.NUMBER &&
                                elements.get(index - 2).type == Type.COLON &&
                                elements.get(index - 3).type == Type.NUMBER &&
                                elements.get(index - 4).type == Type.OPEN
                ) {
                    Integer right = elements.get(index - 1).value;
                    Integer left = elements.get(index - 3).value;
                    for (int i = 0; i < 5; i++) {
                        elements.remove(index - 4);
                    }
                    elements.add(index - 4, new Element(Type.NUMBER, (left * 3) + (right * 2)));
                    index -= 5;
                } else {
                    index--;
                }
            }
        }
        return elements.get(0).value;
    }
    
    
    enum Type {OPEN, CLOSE, COLON, NUMBER;}
    
    class Element {
        Type type;
        Integer value;

        public Element(Type type) {
            this.type = type;
        }

        public Element(Type type, Integer value) {
            this.type = type;
            this.value = value;
        }
    }
    
    private void print(List<Element> elements) {
        StringBuilder builder = new StringBuilder();
        for (Element element :elements) {
            String str;
            switch (element.type) {
                case OPEN: 
                    str = "[";
                    break;
                case CLOSE:
                    str = "]";
                    break;
                case COLON:
                    str = ",";
                    break;
                case NUMBER:
                    str = element.value.toString();
                    break;
                default: throw new RuntimeException("Error in logic :'(");
            }
            builder.append(str);
        }
        System.out.println(builder);
    }
    
    @Override
    public int getDay() {
        return DAY;
    }

   


}
