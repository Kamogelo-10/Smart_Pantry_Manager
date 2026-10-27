
package com.example.smartpantrymanager;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class Recipe {

    private final String name;
    private final String description;
    private final Map<String, Double> ingredients;
    private final Map<String, String> ingredientUnits;

    public Recipe(
            String name,
            String description,
            Map<String, Double> ingredients,
            Map<String, String> ingredientUnits) {

        this.name = name;
        this.description = description;
        this.ingredients = new LinkedHashMap<>(ingredients);
        this.ingredientUnits = new LinkedHashMap<>();

        for (String ingredient : ingredients.keySet()) {
            String unit = ingredientUnits.get(ingredient);

            if (unit == null || unit.trim().isEmpty()) {
                unit = defaultUnit(ingredient);
            }

            this.ingredientUnits.put(ingredient, unit);
        }
    }

    public Recipe(
            String name,
            String description,
            Map<String, Double> ingredients) {

        this(name, description, ingredients, defaultUnits(ingredients));
    }

    private static Map<String, String> defaultUnits(
            Map<String, Double> ingredients) {

        Map<String, String> units = new LinkedHashMap<>();

        for (String ingredient : ingredients.keySet()) {
            units.put(ingredient, defaultUnit(ingredient));
        }

        return units;
    }

    private static String defaultUnit(String ingredient) {
        String name = ingredient.toLowerCase(Locale.ROOT).trim();

        switch (name) {
            case "bread":
                return "slices";

            case "milk":
            case "water":
            case "oil":
            case "lemon juice":
                return "ml";

            case "rice":
            case "flour":
            case "sugar":
            case "oats":
            case "butter":
            case "cheese":
                return "g";

            default:
                return "pieces";
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Double> getIngredients() {
        return new LinkedHashMap<>(ingredients);
    }

    public Map<String, String> getIngredientUnits() {
        return new LinkedHashMap<>(ingredientUnits);
    }

    public String getIngredientUnit(String ingredient) {
        String unit = ingredientUnits.get(ingredient);
        return unit != null ? unit : defaultUnit(ingredient);
    }
}
