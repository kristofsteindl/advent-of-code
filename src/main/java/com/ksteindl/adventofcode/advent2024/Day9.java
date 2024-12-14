package com.ksteindl.adventofcode.advent2024;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.google.common.base.Stopwatch;

public class Day9 extends Puzzle2024{

    public static void main(String[] args) {
        var day9 = new Day9();
        day9.printSolutions();
    }
    
    static class Block {
        public Block(int index, int length, boolean isEmpty) {
            this.value = index;
            this.length = length;
            this.isEmpty = isEmpty;
        }

        int value; 
        int length;
        boolean isEmpty;


        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;

            Block block = (Block) object;
            return value == block.value;
        }

        @Override
        public int hashCode() {
            return value;
        }
    }
    
    private List<Block> getOriginalBlocks(String line) {
        List<Block> originalBlocks = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            boolean isEmpty = i % 2 != 0;
            originalBlocks.add(new Block(isEmpty ? -1 : i / 2,  Integer.parseInt("" + line.charAt(i)), isEmpty));
        }
        return Collections.unmodifiableList(originalBlocks);
    }
    
    @Override
    protected Number getFirstSolution(List<String> lines) {
        String line = lines.get(0);
        List<Block> originalBlocks = getOriginalBlocks(line);
        boolean complete = false;
        int originalBlockArrayIndex = 0;
        int lastNonEmptyBoxArrayIndex = originalBlocks.size() - 1;
        List<Block> sortedBlocks = new ArrayList<>();
        while (!complete) {
            Block nextBlock = originalBlocks.get(originalBlockArrayIndex);
            if (!nextBlock.isEmpty) {
                sortedBlocks.add(nextBlock);
            } else {
                Block lastNonEmptyBlock = originalBlocks.get(lastNonEmptyBoxArrayIndex);
                Block remainingNextBlock = new Block(nextBlock.value, nextBlock.length, nextBlock.isEmpty);
                while (remainingNextBlock != null && !complete) {

                    if (remainingNextBlock.length <= lastNonEmptyBlock.length) {
                        sortedBlocks.add(new Block(lastNonEmptyBlock.value, remainingNextBlock.length, false));
                        lastNonEmptyBlock.length -= remainingNextBlock.length;
                        remainingNextBlock = null;
                        if (originalBlockArrayIndex + 1 == lastNonEmptyBoxArrayIndex) {
                            complete = true;
                            sortedBlocks.add(new Block(lastNonEmptyBlock.value, lastNonEmptyBlock.length, false));
                        }
                    } else {
                        sortedBlocks.add(new Block(lastNonEmptyBlock.value, lastNonEmptyBlock.length, false));
                        remainingNextBlock.length -= lastNonEmptyBlock.length;
                        lastNonEmptyBoxArrayIndex -= 2;
                        lastNonEmptyBlock = originalBlocks.get(lastNonEmptyBoxArrayIndex);
                        if (originalBlockArrayIndex > lastNonEmptyBoxArrayIndex) {
                            complete = true;
                        }
                    }
                }
            }
            originalBlockArrayIndex++;
        }
        return calcChecksum(sortedBlocks);
        
    }
    
    private long calcChecksum(List<Block> sortedBlocks) {
        long sum = 0;
        int index = 0;
        for (Block block : sortedBlocks) {
            for (int i = 0; i < block.length; i++) {
                sum += block.isEmpty ? 0 : (long) block.value * index;
                index++;
            }
        }
        return sum;
    }
    

    @Override
    protected Number getSecondSolution(List<String> lines) {
        List<Block> blocks = new ArrayList<>(getOriginalBlocks(lines.get(0)));
        Block lastFileBlock = blocks.get(blocks.size() - 1);
        var watch = Stopwatch.createStarted();
        while (lastFileBlock != null) {
            int blockIndex = 0;
            int lastFileBlockIndex = blocks.indexOf(lastFileBlock);
            int value = lastFileBlock.value;
            while (true) {
                Block block = blocks.get(blockIndex);
                if (block.isEmpty && block.length >= lastFileBlock.length) {
                    block.length -= lastFileBlock.length;
                    blocks.add(blockIndex, new Block(value, lastFileBlock.length, false));
                    lastFileBlock.value = -1;
                    lastFileBlock.isEmpty = true;
                    break;
                }
                else if (blockIndex >= lastFileBlockIndex) {
                    break;
                }
                blockIndex++;
            }
            System.out.println(lastFileBlockIndex);
            lastFileBlock = getNextLastFileBlock(lastFileBlockIndex, value, blocks);
        }
        System.out.println(watch.elapsed().getSeconds());
        return calcChecksum(blocks);
    }
    
    private Block getNextLastFileBlock(int fromIndex, Integer nextValue, List<Block> blocks) {
        for (int i = fromIndex; i > 0; i--) {
            Block block = blocks.get(i);
            if (block.value == (nextValue - 1)) {
                return block;
            }
        }
        return null;
    }

    @Override
    public int getDay() {
        return 9;
    }
}
