package com.ksteindl.adventofcode.advent2020.day08.model;

import java.util.Objects;

public class Instruction {

    private InstructionType type;
    private final int argument;
    private boolean stepped = false;

    public Instruction(InstructionType type, int argument) {
        this.type = type;
        this.argument = argument;
    }

    public void setType(InstructionType type) {
        this.type = type;
    }

    public InstructionType getType() {
        return type;
    }

    public int getArgument() {
        return argument;
    }

    public boolean isStepped() {
        return stepped;
    }

    public void setStepped(boolean stepped) {
        this.stepped = stepped;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "type=" + type +
                ", argument=" + argument +
                ", stepped=" + stepped +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instruction that = (Instruction) o;
        return argument == that.argument &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, argument);
    }
}
