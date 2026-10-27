
package com.example.smartpantrymanager;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecipeRepository {

    private final List<Recipe> recipes = new ArrayList<>();

    public RecipeRepository() {
        loadRecipes();
    }

    public List<Recipe> getAllRecipes() {
        return new ArrayList<>(recipes);
    }

    public List<Recipe> getMatchingRecipes(Context context) {
        Map<String, Double> pantry = new LinkedHashMap<>();

        PantryDatabaseHelper helper =
                new PantryDatabaseHelper(context.getApplicationContext());

        try (Cursor cursor = helper.getAllIngredients()) {
            int nameIndex = cursor.getColumnIndexOrThrow(
                    PantryDatabaseHelper.COLUMN_NAME);
            int quantityIndex = cursor.getColumnIndexOrThrow(
                    PantryDatabaseHelper.COLUMN_QUANTITY);
            int unitIndex = cursor.getColumnIndexOrThrow(
                    PantryDatabaseHelper.COLUMN_UNIT);

            while (cursor.moveToNext()) {
                String name = normalizeIngredient(
                        cursor.getString(nameIndex));

                double quantity = cursor.getDouble(quantityIndex);

                String unit = normalizeUnit(cursor.getString(unitIndex));

                if (name.isEmpty() || quantity <= 0) {
                    continue;
                }

                String key = name + "|" + unit;

                pantry.put(
                        key,
                        pantry.getOrDefault(key, 0.0) + quantity);
            }
        } finally {
            helper.close();
        }

        List<Recipe> matching = new ArrayList<>();

        for (Recipe recipe : recipes) {
            boolean canMake = true;

            for (Map.Entry<String, Double> required
                    : recipe.getIngredients().entrySet()) {

                String name = normalizeIngredient(required.getKey());
                String requiredUnit = normalizeUnit(
                        recipe.getIngredientUnit(required.getKey()));

                double needed = required.getValue();

                double available = getAvailableQuantity(
                        pantry, name, requiredUnit);

                if (available + 0.000001 < needed) {
                    canMake = false;
                    break;
                }
            }

            if (canMake) {
                matching.add(recipe);
            }
        }

        return matching;
    }

    private double getAvailableQuantity(
            Map<String, Double> pantry,
            String ingredient,
            String requiredUnit) {

        double total = 0;

        for (Map.Entry<String, Double> entry : pantry.entrySet()) {
            String[] parts = entry.getKey().split("\\|", 2);

            if (parts.length != 2 || !parts[0].equals(ingredient)) {
                continue;
            }

            String pantryUnit = parts[1];

            if (pantryUnit.equals(requiredUnit)) {
                total += entry.getValue();
                continue;
            }

            double converted = convertQuantity(
                    ingredient,
                    entry.getValue(),
                    pantryUnit,
                    requiredUnit);

            if (converted >= 0) {
                total += converted;
            }
        }

        return total;
    }

    /*
     * Returns -1 when the units cannot safely be converted.
     */
    private double convertQuantity(
            String ingredient,
            double quantity,
            String fromUnit,
            String toUnit) {

        if (fromUnit.equals(toUnit)) {
            return quantity;
        }

        // Weight conversions.
        double grams = toGrams(quantity, fromUnit);

        if (grams >= 0 && isWeightUnit(toUnit)) {
            return fromGrams(grams, toUnit);
        }

        // Volume conversions.
        double millilitres = toMillilitres(quantity, fromUnit);

        if (millilitres >= 0 && isVolumeUnit(toUnit)) {
            return fromMillilitres(millilitres, toUnit);
        }

        /*
         * Approximate ingredient-specific conversions.
         * These are used only for ingredients with a common
         * practical kitchen measurement.
         */
        if (isVolumeUnit(fromUnit) && isWeightUnit(toUnit)) {
            double ml = toMillilitres(quantity, fromUnit);
            double gramsPerCup = gramsPerCup(ingredient);

            if (ml >= 0 && gramsPerCup > 0) {
                double gramsValue = ml / 240.0 * gramsPerCup;
                return fromGrams(gramsValue, toUnit);
            }
        }

        if (isWeightUnit(fromUnit) && isVolumeUnit(toUnit)) {
            double g = toGrams(quantity, fromUnit);
            double gramsPerCup = gramsPerCup(ingredient);

            if (g >= 0 && gramsPerCup > 0) {
                double ml = g / gramsPerCup * 240.0;
                return fromMillilitres(ml, toUnit);
            }
        }

        return -1;
    }

    private double gramsPerCup(String ingredient) {
        switch (ingredient) {
            case "rice":
                return 185;
            case "oats":
                return 90;
            case "pasta":
                return 100;
            case "flour":
                return 125;
            case "sugar":
                return 200;
            case "butter":
                return 227;
            case "peanut butter":
                return 270;
            case "milk":
                return 240;
            default:
                return -1;
        }
    }

    private boolean isWeightUnit(String unit) {
        return unit.equals("g")
                || unit.equals("kg")
                || unit.equals("mg");
    }

    private boolean isVolumeUnit(String unit) {
        return unit.equals("ml")
                || unit.equals("l")
                || unit.equals("cups")
                || unit.equals("tablespoons")
                || unit.equals("teaspoons");
    }

    private double toGrams(double quantity, String unit) {
        switch (unit) {
            case "g":
                return quantity;
            case "kg":
                return quantity * 1000;
            case "mg":
                return quantity / 1000;
            default:
                return -1;
        }
    }

    private double fromGrams(double grams, String unit) {
        switch (unit) {
            case "g":
                return grams;
            case "kg":
                return grams / 1000;
            case "mg":
                return grams * 1000;
            default:
                return -1;
        }
    }

    private double toMillilitres(double quantity, String unit) {
        switch (unit) {
            case "ml":
                return quantity;
            case "l":
                return quantity * 1000;
            case "cups":
                return quantity * 240;
            case "tablespoons":
                return quantity * 15;
            case "teaspoons":
                return quantity * 5;
            default:
                return -1;
        }
    }

    private double fromMillilitres(double ml, String unit) {
        switch (unit) {
            case "ml":
                return ml;
            case "l":
                return ml / 1000;
            case "cups":
                return ml / 240;
            case "tablespoons":
                return ml / 15;
            case "teaspoons":
                return ml / 5;
            default:
                return -1;
        }
    }

    private String normalizeIngredient(String value) {
        String name = value == null
                ? ""
                : value.trim().toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");

        switch (name) {
            case "egg":
                return "eggs";
            case "tomato":
                return "tomatoes";
            case "banana":
                return "bananas";
            case "potato":
                return "potatoes";
            case "oat":
                return "oats";
            case "avocados":
                return "avocado";
            case "loaf":
            case "loaves":
            case "bread slices":
                return "bread";
            case "chicken breast":
            case "chicken pieces":
                return "chicken";
            case "pasta noodles":
                return "pasta";
            case "rolled oats":
                return "oats";
            case "peanut-butter":
                return "peanut butter";
            default:
                return name;
        }
    }

    private String normalizeUnit(String value) {
        String unit = value == null
                ? ""
                : value.trim().toLowerCase(Locale.ROOT)
                .replace(".", "");

        switch (unit) {
            case "piece":
            case "pc":
            case "pcs":
            case "each":
            case "item":
            case "items":
            case "unit":
            case "units":
                return "pieces";

            case "slice":
                return "slices";

            case "clove":
                return "cloves";

            case "gram":
            case "grams":
                return "g";

            case "kilogram":
            case "kilograms":
                return "kg";

            case "milligram":
            case "milligrams":
                return "mg";

            case "millilitre":
            case "millilitres":
            case "milliliter":
            case "milliliters":
                return "ml";

            case "litre":
            case "litres":
            case "liter":
            case "liters":
                return "l";

            case "cup":
                return "cups";

            case "tbsp":
            case "tablespoon":
            case "tablespoonful":
                return "tablespoons";

            case "tsp":
            case "teaspoon":
            case "teaspoonful":
                return "teaspoons";

            default:
                return unit;
        }
    }

    private void loadRecipes() {
        recipes.clear();

        addRecipe("Tomato Omelette",
                "A simple omelette made with fresh tomatoes.",
                ingredients("eggs", 2, "tomatoes", 2),
                units("eggs", "pieces", "tomatoes", "pieces"));

        addRecipe("Scrambled Eggs",
                "Soft scrambled eggs for a quick meal.",
                ingredients("eggs", 2),
                units("eggs", "pieces"));

        addRecipe("Tomato Salad",
                "A fresh and simple tomato salad.",
                ingredients("tomatoes", 2),
                units("tomatoes", "pieces"));

        addRecipe("Cheese Omelette",
                "Eggs cooked with cheese.",
                ingredients("eggs", 2, "cheese", 1),
                units("eggs", "pieces", "cheese", "slices"));

        addRecipe("Chicken and Rice",
                "A filling chicken and rice meal.",
                ingredients("chicken", 1, "rice", 1),
                units("chicken", "pieces", "rice", "cups"));

        addRecipe("Chicken Salad",
                "Chicken served with fresh tomatoes.",
                ingredients("chicken", 1, "tomatoes", 2),
                units("chicken", "pieces", "tomatoes", "pieces"));

        addRecipe("Tomato Pasta",
                "Pasta served with a tomato sauce.",
                ingredients("pasta", 1, "tomatoes", 2),
                units("pasta", "cups", "tomatoes", "pieces"));

        addRecipe("Egg Fried Rice",
                "Rice cooked with eggs.",
                ingredients("rice", 1, "eggs", 2),
                units("rice", "cups", "eggs", "pieces"));

        addRecipe("Banana Smoothie",
                "A creamy banana and milk smoothie.",
                ingredients("bananas", 1, "milk", 1),
                units("bananas", "pieces", "milk", "cups"));

        addRecipe("Peanut Butter Sandwich",
                "Bread filled with peanut butter.",
                ingredients("bread", 2, "peanut butter", 1),
                units("bread", "slices",
                        "peanut butter", "tablespoons"));

        addRecipe("Cheese Sandwich",
                "A simple bread and cheese sandwich.",
                ingredients("bread", 2, "cheese", 1),
                units("bread", "slices", "cheese", "slices"));

        addRecipe("Potato Mash",
                "Mashed potatoes for a comforting side dish.",
                ingredients("potatoes", 3),
                units("potatoes", "pieces"));

        addRecipe("Chicken and Potatoes",
                "Chicken served with potatoes.",
                ingredients("chicken", 1, "potatoes", 2),
                units("chicken", "pieces", "potatoes", "pieces"));

        addRecipe("Banana Pancakes",
                "Pancakes made with bananas and eggs.",
                ingredients("bananas", 1, "eggs", 2),
                units("bananas", "pieces", "eggs", "pieces"));

        addRecipe("Milk Porridge",
                "Warm porridge made with oats and milk.",
                ingredients("oats", 1, "milk", 1),
                units("oats", "cups", "milk", "cups"));

        addRecipe("Avocado Toast",
                "Sliced avocado served on bread.",
                ingredients("bread", 2, "avocado", 1),
                units("bread", "slices", "avocado", "pieces"));

        addRecipe("Garlic Butter Pasta",
                "Pasta with garlic and butter.",
                ingredients("pasta", 1, "garlic", 2, "butter", 1),
                units("pasta", "cups",
                        "garlic", "cloves",
                        "butter", "tablespoons"));

        addRecipe("Banana Oatmeal",
                "Oats prepared with milk and banana.",
                ingredients("oats", 1, "milk", 1, "bananas", 1),
                units("oats", "cups",
                        "milk", "cups",
                        "bananas", "pieces"));

        addRecipe("Chicken Sandwich",
                "A sandwich filled with chicken.",
                ingredients("bread", 2, "chicken", 1),
                units("bread", "slices", "chicken", "pieces"));

        addRecipe("Tomato Rice",
                "Rice served with tomatoes.",
                ingredients("rice", 1, "tomatoes", 2),
                units("rice", "cups", "tomatoes", "pieces"));
    }

    private void addRecipe(
            String name,
            String description,
            Map<String, Double> ingredients,
            Map<String, String> units) {

        recipes.add(new Recipe(name, description, ingredients, units));
    }

    private Map<String, Double> ingredients(Object... values) {
        Map<String, Double> result = new LinkedHashMap<>();

        for (int i = 0; i < values.length; i += 2) {
            result.put(
                    (String) values[i],
                    ((Number) values[i + 1]).doubleValue());
        }

        return result;
    }

    private Map<String, String> units(String... values) {
        Map<String, String> result = new LinkedHashMap<>();

        for (int i = 0; i < values.length; i += 2) {
            result.put(values[i], values[i + 1]);
        }

        return result;
    }
}
