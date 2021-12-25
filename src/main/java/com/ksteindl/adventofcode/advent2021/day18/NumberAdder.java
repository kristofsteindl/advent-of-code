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
        NumberAdder day = new NumberAdder(true);
        logger.info(day.getFirstSolution());
        logger.info(day.getSecondSolution());
    }

    private long getMagnitude(List<Element> elements) {
        while (elements.size() > 1) {
            System.out.println("Before magnitude reduction: ");
            print(elements);
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

    @Override
    public Number getFirstSolution() {
        List<Element> sum = elementRows.get(0);
        for (int row = 1; row < elementRows.size(); row++) {
            System.out.println("Before addition, sum: ");
            print(sum);
            List<Element> added = new ArrayList<>();
            added.add(new Element(Type.OPEN));
            added.addAll(sum);
            added.add(new Element(Type.COLON));
            added.addAll(elementRows.get(row));
            added.add(new Element(Type.CLOSE));
            System.out.println("After addition, before reduction: ");
            print(added);
            boolean shouldReduce = true;
            while (shouldReduce) {
                shouldReduce = false;
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
                        System.out.println("After exposion: ");
                        print(added);
                    }
                    index++;
                }
                index = 0;
                while (!shouldReduce && index < added.size()) {
                    Element element = added.get(index);
                    if (element.type == Type.NUMBER && element.value > 9) {
                        shouldReduce = true;
                        Integer tenSmth = element.value;
                        added.remove(index);
                        int first = tenSmth / 2;
                        int second = first * 2 == tenSmth ? first : first + 1;
                        added.add(index, new Element(Type.CLOSE));
                        added.add(index, new Element(Type.NUMBER, second));
                        added.add(index, new Element(Type.COLON));
                        added.add(index, new Element(Type.NUMBER, first));
                        added.add(index, new Element(Type.OPEN));
                        System.out.println("After split: ");
                        print(added);
                    }
                    index++;
                }
            }
            sum = added;
        }
        return getMagnitude(sum);
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
    public Number getSecondSolution() {
        return getDay();
    }
    

    @Override
    public int getDay() {
        return DAY;
    }

   


}
