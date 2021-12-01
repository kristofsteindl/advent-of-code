package com.ksteindl.adventofcode.advent2020.day21.model;

import java.util.Map;
import java.util.Set;

public class ProcessedIngredients {

    private final Map<String, String> ingredientsToAllergen;
    private final Set<Food> safeFoods;

    public ProcessedIngredients(Map<String, String> ingredientsToAllergen, Set<Food> safeFoods) {
        this.ingredientsToAllergen = ingredientsToAllergen;
        this.safeFoods = safeFoods;
    }

    public Map<String, String> getIngredientsToAllergen() {
        return ingredientsToAllergen;
    }

    public Set<Food> getSafeFoods() {
        return safeFoods;
    }
}
