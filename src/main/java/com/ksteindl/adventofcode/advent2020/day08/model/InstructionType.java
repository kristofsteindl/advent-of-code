package com.ksteindl.adventofcode.advent2020.day08.model;

public enum InstructionType {

    ACC("acc"),
    JMP("jmp"),
    NOP("nop");

    public final String alias;

    InstructionType(String alias) {
        this.alias = alias;
    }

}
