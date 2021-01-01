package com.ksteindl.adventofcode.advent2020.day10;

import com.ksteindl.adventofcode.Puzzle2020;
import com.ksteindl.adventofcode.advent2020.day10.model.Adapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdapterCombiner extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(AdapterCombiner.class);

    private static final int DAY = 10;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;

    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE_ = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test2.txt";

    private final String fileName;
    private final List<Integer> adapterValues;

    public AdapterCombiner(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
        List<Integer> adapterValues = fileManager.parseLines(fileName).stream().mapToInt(line -> Integer.parseInt(line)).boxed().sorted().collect(Collectors.toList());
        adapterValues.add(0,0);
        adapterValues.add(adapterValues.get(adapterValues.size()-1) + 3) ;
        this.adapterValues = adapterValues;
    }


    @Override
    public Number getFirstSolution() {
        return getOneMultipliedThreeDifference();
    }

    @Override
    public Number getSecondSolution() {
        return getTotalNumberOfArrangements2();
    }

    @Override
    public int getDay() {
        return DAY;
    }

    private long getTotalNumberOfArrangements2() {
        List<Adapter> adapters = new ArrayList<>();
        for (int i = 0; i < adapterValues.size(); i++) {
            adapters.add(new Adapter(i, adapterValues.get(i), validWhenFirstElementRemoved(adapterValues, i)));
        }
        List<List<Adapter>> blocks = new ArrayList<>();
        List<Adapter> block = null;
        for (int i = 0; i < adapters.size(); i++) {
            Adapter adapter = adapters.get(i);
            if(adapter.isValidToRemove()) {
                if (block == null) {
                    block = new ArrayList<>();
                }
                block.add(adapter);
            } else {
                if (block != null && !block.isEmpty()) {
                    blocks.add(new ArrayList<>(block));
                    block.clear();
                }
            }
        }
        List<Long> multiplierBlocks = blocks.stream().filter(blocky -> blocky.size() > 0).map(blocky -> getMultiplier(blocky)).collect(Collectors.toList());
        return multiplierBlocks.stream().mapToLong(i -> i).reduce(1, (i, k) -> i * k);
    }

    private Long getMultiplier(List<Adapter> block) {
        if (block.size() == 1) {
            return 2l;
        }
        return getTotalNumberOfArrangements(adapterValues.subList(block.get(0).getIndex() -1 , block.get(block.size()-1).getIndex() + 2), 0);
    }

    private long getTotalNumberOfArrangements(final List<Integer> adapterSubList, final int index) {
        long totalNumberOfArrangments = 0;
        if (adapterSubList.size() == index) {
            return 1;
        }
        if (validInBlock(adapterSubList, index)) {
            List<Integer> newList = new ArrayList<>(adapterSubList);
            newList.remove(index);
            totalNumberOfArrangments = totalNumberOfArrangments + getTotalNumberOfArrangements(newList, index);
        }
        totalNumberOfArrangments = totalNumberOfArrangments + getTotalNumberOfArrangements(adapterSubList,index + 1);
        return totalNumberOfArrangments;
    }

    private boolean validInBlock(final List<Integer> adapterSubList, final int index) {
        logger.debug(adapterSubList.get(index));
        boolean validWhenFirstElementRemoved;
        if (index == 0) {
            validWhenFirstElementRemoved = false;
        } else if (index == adapterSubList.size() -1) {
            validWhenFirstElementRemoved = false;
        } else {
            Integer leftNeighbour = adapterSubList.get(index - 1);
            Integer rightNeighbour = adapterSubList.get(index + 1);
            validWhenFirstElementRemoved = rightNeighbour - leftNeighbour < 4;
            logger.debug(adapterSubList.toString());
            logger.debug("lfetNeighbour: " + leftNeighbour + ", rightNeighbour: " + rightNeighbour + "valid: " + validWhenFirstElementRemoved);
        }
        return validWhenFirstElementRemoved;
    }

    private boolean validWhenFirstElementRemoved(final List<Integer> adapterSubList, final int index) {
        logger.debug(adapterSubList.get(index));
        boolean validWhenFirstElementRemoved;
        if (index == 0) {
            validWhenFirstElementRemoved = false;
        } else if (index == adapterSubList.size() -1) {
            validWhenFirstElementRemoved = false;
        } else {
            Integer leftNeighbour = adapterSubList.get(index - 1);
            Integer rightNeighbour = adapterSubList.get(index + 1);
            validWhenFirstElementRemoved = rightNeighbour - leftNeighbour < 4;
            logger.debug(adapterSubList.toString());
            logger.debug("lfetNeighbour: " + leftNeighbour + ", rightNeighbour: " + rightNeighbour + "valid: " + validWhenFirstElementRemoved);
        }
        return validWhenFirstElementRemoved;
    }

    private int getOneMultipliedThreeDifference() {
        adapterValues.forEach(number -> logger.debug(number));
        int diff1 = 0;
        int diff3 = 0;
        if(adapterValues.get(0) == 1) {
            diff1++;
        } else if (adapterValues.get(0) == 3) {
            diff3++;
        }
        for (int i = 0; i < adapterValues.size() - 1; i++) {
            int diff = adapterValues.get(i + 1) - adapterValues.get(i);
            if (diff % 3 == 0) {
                diff3++;
            } else if (diff % 2 != 0) {
                logger.debug("diff1: i: " + adapterValues.get(i) + ", i+1: " + adapterValues.get(i + 1));
                diff1++;
            }
        }
        logger.debug("diff1: " + diff1 + ", diff3: " + diff3);
        return diff1 * diff3;
    }

    /*
    *
    * References
    *
    * */
}
