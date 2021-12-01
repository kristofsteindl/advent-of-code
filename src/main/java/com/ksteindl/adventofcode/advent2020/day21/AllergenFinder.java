package com.ksteindl.adventofcode.advent2020.day21;

import com.ksteindl.adventofcode.advent2020.Puzzle2020;
import com.ksteindl.adventofcode.advent2020.day21.model.Food;
import com.ksteindl.adventofcode.advent2020.day21.model.ProcessedIngredients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class AllergenFinder extends Puzzle2020 {

    private static final Logger logger = LogManager.getLogger(AllergenFinder.class);

    private static final int DAY = 21;

    private static final String DAY_STRING = (DAY < 10 ? "0" : "") + DAY ;
    private static String INPUT_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + ".txt";
    private static String INPUT_TEST_FILE = FOLDER_NAME + "inputDec" + DAY_STRING + "_test.txt";

    private final String fileName;
    ProcessedIngredients cachedProcessedIngredients;

    public AllergenFinder(boolean isTest) {
        super(isTest);
        this.fileName = isTest ? INPUT_TEST_FILE : INPUT_FILE;
    }

    @Override
    public Number getFirstSolution() {
        return getAllergenFreeIngredientsOccurrenceCount();
    }

    @Override
    public String getSecondSolution() {
        return getCanonicalDangerousIngredientList();
    }

    @Override
    public int getDay() {
        return DAY;
    }


    private int getAllergenFreeIngredientsOccurrenceCount() {
        if (cachedProcessedIngredients == null) {
            cachedProcessedIngredients = getProcessedIngredients();
        }
        List<String> safeIngredients = getSafeIngredients(cachedProcessedIngredients.getSafeFoods());
        logger.debug(safeIngredients.toString());
        return safeIngredients.size();
    }

    private ProcessedIngredients getProcessedIngredients() {
        List<String> lines = fileManager.parseLines(fileName);
        Set<Food> foodList = lines.stream().map(line -> convertToFood(line)).collect(Collectors.toSet());
        final Map<String, Integer> allergenHeatMap = getAllergenHeatMap(foodList);
        final Map<Integer, List<Map.Entry<String, Integer>>> allergensInDescOrderOverHead = allergenHeatMap
                .entrySet()
                .stream()
                .collect(groupingBy(entry -> entry.getValue()));
        Map<Integer, List<String>> allergensInAscOrder = allergensInDescOrderOverHead.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue().stream().map(subEntry -> subEntry.getKey()).collect(Collectors.toList()),
                        (o1, o2) -> o1, TreeMap::new));
        List<String> allergenListInDescOrder = getAllergenListInDescOrder(allergensInAscOrder);
        return getProcessedIngredients(allergenListInDescOrder, foodList);
    }

    private String getCanonicalDangerousIngredientList() {
        if (cachedProcessedIngredients == null) {
            cachedProcessedIngredients = getProcessedIngredients();
        }
        Map<String, String> ingredientsToAllergen = cachedProcessedIngredients.getIngredientsToAllergen();
        List<Map.Entry<String, String>> ingredientsToAllergenList = new ArrayList<>(ingredientsToAllergen.entrySet());
        ingredientsToAllergenList.sort(Comparator.comparing(Map.Entry::getValue));
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry :ingredientsToAllergenList) {
            builder.append(entry.getKey());
            builder.append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        String canonicalDangerousIngredientList = builder.toString();
        return canonicalDangerousIngredientList;
    }

    private List<String> getSafeIngredients(Set<Food> foodList) {
        return foodList.stream().flatMap(food -> food.getIngredients().stream()).collect(Collectors.toList());
    }

    private  List<String> getAllergenListInDescOrder(Map<Integer, List<String>> allergensInAscOrder) {
        List<String> allergenListInDescOrder = new ArrayList<>();
        for (Map.Entry<Integer, List<String>> entry : allergensInAscOrder.entrySet()) {
            allergenListInDescOrder.addAll(0, entry.getValue());
        }
        return allergenListInDescOrder;
    }

    private ProcessedIngredients getProcessedIngredients(List<String> allergenListInDescOrder, final Set<Food> foodList) {
        Map<String, String> ingredientsToAllergen = new HashMap<>();
        Set<Food> safeFoods = new HashSet<>(foodList);
        for (int i = 0; i < allergenListInDescOrder.size(); i++) {
            String allergen = allergenListInDescOrder.get(i);
            Set<String> potentialIngredients = null;
            for (Food food : safeFoods) {
                if (food.getAllergens().contains(allergen)) {
                    if (potentialIngredients == null) {
                        potentialIngredients = new HashSet<>(food.getIngredients());
                    }
                    potentialIngredients.retainAll(food.getIngredients());
                }
            }
            if (potentialIngredients == null || potentialIngredients.size() == 0) {
                throw new RuntimeException("Algorithm is wrong");
            } else if (potentialIngredients.size() == 1) {
                String ingredient = potentialIngredients.stream().findAny().get();
                ingredientsToAllergen.put(ingredient, allergen);
                allergenListInDescOrder.remove(allergen);
                removeIngredientsFromEveryFood(ingredient, safeFoods);
                i = -1;
            }
        }
        return new ProcessedIngredients(ingredientsToAllergen, safeFoods);
    }


    private void removeIngredientsFromEveryFood(String ingredient, Set<Food> foodList) {
        for (Food food : foodList) {
            food.getIngredients().remove(ingredient);
        }
    }

    private Map<String, Integer> getAllergenHeatMap(Set<Food> foodList) {
        Map<String, Integer> allergenHeatMap = new HashMap<>();
        for (Food food: foodList) {
            for (String allergen : food.getAllergens()) {
                allergenHeatMap.merge(allergen, 1, (w, prev) -> prev + 1);
            }
        }
        return allergenHeatMap;
    }

    private Food convertToFood(String line) {
        String[] blocks = line.split("[(]");
        Set<String> ingredients = Arrays.stream(blocks[0].split(" ")).filter(ing -> !ing.isEmpty()).collect(Collectors.toSet());
        Set<String> allergens = Arrays.stream(blocks[1].split(" "))
                .filter(string -> !string.equals("contains"))
                .map(string -> string.substring(0, string.length() - 1))
                .collect(Collectors.toSet());
        return new Food(ingredients, allergens);
    }


    /*
    *
    * References
    *
    * */
}
