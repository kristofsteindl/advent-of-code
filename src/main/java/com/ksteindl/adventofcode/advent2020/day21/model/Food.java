package com.ksteindl.adventofcode.advent2020.day21.model;

import java.util.Set;

public class Food {

    private Set<String> ingredients;
    private Set<String> allergens;

    public Food(Set<String> ingredients, Set<String> allergens) {
        this.ingredients = ingredients;
        this.allergens = allergens;
    }

    public Set<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<String> ingredients) {
        this.ingredients = ingredients;
    }

    public Set<String> getAllergens() {
        return allergens;
    }

    public void setAllergens(Set<String> allergens) {
        this.allergens = allergens;
    }
}
