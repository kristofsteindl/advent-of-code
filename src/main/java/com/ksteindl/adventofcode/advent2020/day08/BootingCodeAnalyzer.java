package com.ksteindl.adventofcode.advent2020.day08;

import com.ksteindl.adventofcode.Puzzle2020;
import com.ksteindl.adventofcode.advent2020.day08.model.Instruction;
import com.ksteindl.adventofcode.advent2020.day08.model.InstructionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BootingCodeAnalyzer extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(BootingCodeAnalyzer.class);

    private static final int DAY = 8;
    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;

    public BootingCodeAnalyzer(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    @Override
    public Number getFirstSolution() {
        return getAccumulatorValueBeforeSecondLoop();
    }

    @Override
    public Number getSecondSolution() {
        return getAccumulatorValueAfterFixingInfiniteLoop();
    }

    @Override
    public int getDay() {
        return DAY;
    }

    private int getAccumulatorValueAfterFixingInfiniteLoop() {
         new AtomicInteger(0);
        List<Instruction> instructions = fileManager.parseLines(fileName).stream().map(line -> createInstruction(line)).collect(Collectors.toList());
        AtomicInteger accumulator = getAccumulatorValueAfterFixingInfiniteLoop(instructions);
        return accumulator.intValue();
    }

    private AtomicInteger getAccumulatorValueAfterFixingInfiniteLoop(final List<Instruction> instructions) {
        int index = 0;
        boolean terminated = false;
        AtomicInteger accumulator = null;
        while (!terminated) {
            Instruction instruction = instructions.get(index);
            InstructionType originalType = instruction.getType();
            swapInstructions(instruction);
            accumulator = new AtomicInteger(0);
            terminated = runInstructions(instructions, accumulator);
            instruction.setType(originalType);
            index++;
        }
        return accumulator;
    }

    private void swapInstructions(Instruction instruction) {
        switch (instruction.getType()) {
            case ACC:
                break;
            case JMP:
                instruction.setType(InstructionType.NOP);
                break;
            case NOP:
                instruction.setType(InstructionType.JMP);
                break;
            default: throw new RuntimeException("no instruction is found");
        }
    }

    private boolean runInstructions(List<Instruction> instructions, AtomicInteger accumulator) {
        int index = 0;
        while (index < instructions.size() && !instructions.get(index).isStepped()) {
            Instruction instruction = instructions.get(index);
            switch (instruction.getType()) {
                case ACC:
                    accumulator.addAndGet(instruction.getArgument());
                    instruction.setStepped(true);
                    index++;
                    break;
                case JMP:
                    instruction.setStepped(true);
                    index = index + instruction.getArgument();
                    break;
                case NOP:
                    instruction.setStepped(true);
                    index++;
                    break;
                default: throw new RuntimeException("no instruction is found");
            }
        }
        instructions.forEach(instruction -> instruction.setStepped(false));
        boolean isTerminated = index == instructions.size();
        logger.debug(isTerminated);
        return isTerminated;
    }

    private int getAccumulatorValueBeforeSecondLoop() {
        AtomicInteger accumulator = new AtomicInteger(0);
        List<Instruction> instructions = fileManager.parseLines(fileName).stream().map(line -> createInstruction(line)).collect(Collectors.toList());
        runInstructions(instructions, accumulator);
        return accumulator.intValue();
    }

    private Instruction createInstruction(String line) {
        String[] splittedLine = line.split(" ");
        Instruction instruction;
        switch (InstructionType.valueOf(splittedLine[0].toUpperCase())) {
            case ACC: instruction = new Instruction(InstructionType.ACC, Integer.parseInt(splittedLine[1])); break;
            case JMP: instruction = new Instruction(InstructionType.JMP, Integer.parseInt(splittedLine[1])); break;
            case NOP: instruction = new Instruction(InstructionType.NOP, Integer.parseInt(splittedLine[1])); break;
            default: throw new RuntimeException("no instruction is found");
        }
        logger.debug(instruction);
        return instruction;
    }



    /*
    *
    * References
    * https://www.geeksforgeeks.org/string-in-switch-case-in-java/
    * https://stackoverflow.com/questions/29319454/enums-with-attributes-java
    *
    * */
}
