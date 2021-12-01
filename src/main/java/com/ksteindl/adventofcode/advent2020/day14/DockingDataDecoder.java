package com.ksteindl.adventofcode.advent2020.day14;

import com.ksteindl.adventofcode.advent2020.Puzzle2020;
import com.ksteindl.adventofcode.advent2020.day14.model.LineToDecodeBuilder;
import com.ksteindl.adventofcode.advent2020.day14.model.LineToDecode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DockingDataDecoder extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(DockingDataDecoder.class);

    private static final int DAY = 14;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";
    private static String INPUT_TEST_FILE2 = FOLDER_NAME + "inputDec" + DAY_STRING + "_test2.txt";

    private final static String MASK_LINE_PREFIX = "mask = ";
    private static int BIT_SIZE = 36;

    private final String fileName;

    public DockingDataDecoder(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    @Override
    public Number getFirstSolution() {
        List<String> lines = fileManager.parseLines(fileName);
        return getSumOfValues(this::decodeV1, lines);
    }

    @Override
    public Number getSecondSolution() {
        List<String> lines;
        if (isTest) {
            lines = fileManager.parseLines(INPUT_TEST_FILE2);
        } else {
            lines = fileManager.parseLines(fileName);
        }
        return getSumOfValues(this::decodeV2, lines);
    }

    private long getSumOfValues(Consumer<LineToDecode> decoder, List<String> lines) {
        Map<Long, Long> memory = new HashMap<>();
        String mask = null;
        for (String line : lines) {
            if (line.startsWith(MASK_LINE_PREFIX)) {
                mask = line.substring(MASK_LINE_PREFIX.length());
            } else {
                int beginIndex = line.indexOf("[") + 1;
                int endIndex = line.indexOf("]");
                Long address = Long.parseLong(line.substring(beginIndex, endIndex));
                Long value = Long.parseLong(line.substring(line.indexOf("=") + 2));
                LineToDecode lineToDecode = new LineToDecodeBuilder()
                        .setAddress(address)
                        .setMask(mask)
                        .setMemory(memory)
                        .setOriginalValue(value)
                        .createMemoryModifierWrapper();
                decoder.accept(lineToDecode);
            }
        }
        return memory.values().stream().mapToLong(i -> i).sum();
    }

    private void decodeV2(LineToDecode lineToDecode) {
        String mask = lineToDecode.getMask();
        List<Boolean[]> addresses = new ArrayList<>();
        Boolean[] startingBinaryAddress = convertBinary(BIT_SIZE, lineToDecode.getAddress());
        addresses.add(startingBinaryAddress);
        for (int i = 0; i < startingBinaryAddress.length; i++) {
            if (mask.charAt(i) == '1') {
                maskEach(addresses, true, i);
            } else if (mask.charAt(i) == 'X') {
                addresses = getAllVariationsOfAddressesAtPosition(i, addresses);
            }
        }
        addresses.forEach(binaryAddress -> lineToDecode.getMemory().put(getLongValue(binaryAddress), lineToDecode.getOriginalValue()));
    }

    private List<Boolean[]> getAllVariationsOfAddressesAtPosition(int i, List<Boolean[]> addresses) {
        List<Boolean[]> allVariationsOfAddressesAtPosition = new ArrayList<>(addresses);
        for (Boolean[] binaryAddress : addresses) {
            Boolean[] negated = binaryAddress.clone();
            negated[i] = !binaryAddress[i];
            allVariationsOfAddressesAtPosition.add(negated);
        }
        return allVariationsOfAddressesAtPosition;
    }

    private void maskEach(List<Boolean[]> addresses, boolean bool, int i) {
        for (Boolean[] binaryAddress: addresses) {
            binaryAddress[i] = bool;
        }
    }

    private void decodeV1(LineToDecode lineToDecode) {
        String mask = lineToDecode.getMask();
        Boolean[] boolValues = convertBinary(BIT_SIZE, lineToDecode.getOriginalValue());
        for (int i = 0; i < boolValues.length; i++) {
            if (mask.charAt(i) == '1') {
                boolValues[i] = true;
            } else if (mask.charAt(i) == '0') {
                boolValues[i] = false;
            }
        }
        lineToDecode.getMemory().put(lineToDecode.getAddress(), getLongValue(boolValues));
    }

    private Boolean[] convertBinary(int bitSize, Long longValue) {
        String stringBinaryValue = Long.toBinaryString(longValue);
        Boolean[] boolValues = new Boolean[bitSize];
        for (int i = 0; i < boolValues.length; i++) {
            boolValues[boolValues.length - 1 - i] = stringBinaryValue.length() > i && stringBinaryValue.charAt(stringBinaryValue.length() -1 -i) == '1';
        }
        return boolValues;
    }

    private long getLongValue(Boolean[] boolValues) {
        long maskedValue = 0l;
        for (int i = 0; i < boolValues.length; i++) {
            maskedValue += boolValues[i] ? Math.pow(2, boolValues.length - 1 - i) : 0;
        }
        return maskedValue;
    }


    @Override
    public int getDay() {
        return DAY;
    }

    /*
    *
    * References
    *
    * https://mkyong.com/java/java-convert-an-integer-to-a-binary-string/
    * https://mkyong.com/java/java-convert-an-integer-to-a-binary-string/
    * https://www.jetbrains.com/help/idea/replace-constructor-with-builder.html#replace_constructorBuilder_example
    *
    * */
}
