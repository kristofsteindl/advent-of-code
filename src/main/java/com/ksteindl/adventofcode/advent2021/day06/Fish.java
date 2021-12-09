package com.ksteindl.adventofcode.advent2021.day06;

public class Fish {
    
    int timer;
    boolean justBear = false;

    public Fish(int timer) {
        this.timer = timer;
    }

    public Fish() {
        this.timer = 8;
    }
    
    public void plusDay() {
        justBear = false;
        if (timer < 1) {
            timer = 7;
            justBear = true;
        }
        timer--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fish fish = (Fish) o;

        return timer == fish.timer;
    }

    @Override
    public int hashCode() {
        return timer;
    }
}
