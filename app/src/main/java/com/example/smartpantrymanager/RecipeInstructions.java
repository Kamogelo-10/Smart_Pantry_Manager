
package com.example.smartpantrymanager;

public class RecipeInstructions {

    public static String getInstructions(String name) {
        switch (name) {
            case "Tomato Omelette":
                return "1. Wash and chop the tomatoes.\n\n"
                        + "2. Crack the eggs into a bowl and beat them.\n\n"
                        + "3. Mix in the chopped tomatoes.\n\n"
                        + "4. Cook in a lightly greased pan over medium heat "
                        + "until the eggs are set.";

            case "Scrambled Eggs":
                return "1. Crack the eggs into a bowl and beat them.\n\n"
                        + "2. Heat a lightly greased pan over low to medium heat.\n\n"
                        + "3. Add the eggs and stir gently as they cook.\n\n"
                        + "4. Remove from the heat when fully cooked.";

            case "Tomato Salad":
                return "1. Wash the tomatoes thoroughly.\n\n"
                        + "2. Cut the tomatoes into bite-sized pieces.\n\n"
                        + "3. Place them in a serving bowl.\n\n"
                        + "4. Mix gently and serve fresh.";

            case "Cheese Omelette":
                return "1. Beat the eggs in a bowl.\n\n"
                        + "2. Heat a lightly greased pan over medium heat.\n\n"
                        + "3. Pour in the eggs and cook until they begin to set.\n\n"
                        + "4. Add the cheese, fold the omelette, and cook "
                        + "until the eggs are fully cooked.";

            case "Chicken and Rice":
                return "1. Rinse the rice if appropriate for the type used.\n\n"
                        + "2. Cook the rice in water according to its package "
                        + "instructions.\n\n"
                        + "3. Cook the chicken thoroughly in a separate pan "
                        + "or pot.\n\n"
                        + "4. Ensure the chicken reaches 74°C internally.\n\n"
                        + "5. Serve the cooked chicken with the rice.";

            case "Chicken Salad":
                return "1. Wash the tomatoes and cut them into pieces.\n\n"
                        + "2. Cook the chicken thoroughly and allow it to cool "
                        + "slightly.\n\n"
                        + "3. Cut the cooked chicken into bite-sized pieces.\n\n"
                        + "4. Combine the chicken and tomatoes and serve.";

            case "Tomato Pasta":
                return "1. Cook the pasta according to its package "
                        + "instructions.\n\n"
                        + "2. Wash and chop the tomatoes.\n\n"
                        + "3. Cook the tomatoes in a pan until softened.\n\n"
                        + "4. Mix the tomato sauce with the drained pasta "
                        + "and serve.";

            case "Egg Fried Rice":
                return "1. Cook the rice and allow it to cool slightly. "
                        + "Leftover rice should have been refrigerated safely.\n\n"
                        + "2. Beat the eggs in a bowl.\n\n"
                        + "3. Scramble the eggs in a hot, lightly greased pan.\n\n"
                        + "4. Add the cooked rice and stir-fry until hot "
                        + "throughout.";

            case "Banana Smoothie":
                return "1. Peel the banana and cut it into pieces.\n\n"
                        + "2. Add the banana and milk to a blender.\n\n"
                        + "3. Blend until smooth.\n\n"
                        + "4. Pour into a glass and serve immediately.";

            case "Peanut Butter Sandwich":
                return "1. Place the bread slices on a clean surface.\n\n"
                        + "2. Spread peanut butter over one side of each "
                        + "slice, adjusting the amount to taste.\n\n"
                        + "3. Place the slices together with the spread "
                        + "inside.\n\n"
                        + "4. Cut in half and serve.";

            case "Cheese Sandwich":
                return "1. Place the bread slices on a clean surface.\n\n"
                        + "2. Arrange the cheese on one slice of bread.\n\n"
                        + "3. Cover with the second slice.\n\n"
                        + "4. Cut in half and serve, or toast if desired.";

            case "Potato Mash":
                return "1. Wash, peel if desired, and cut the potatoes "
                        + "into evenly sized pieces.\n\n"
                        + "2. Boil in water until fork-tender.\n\n"
                        + "3. Drain thoroughly.\n\n"
                        + "4. Mash until smooth. Add seasoning or other "
                        + "ingredients only if available separately.";

            case "Chicken and Potatoes":
                return "1. Wash the potatoes and cut them into pieces.\n\n"
                        + "2. Cook the potatoes by boiling or roasting "
                        + "until tender.\n\n"
                        + "3. Cook the chicken thoroughly in a separate pan "
                        + "or oven.\n\n"
                        + "4. Check that the chicken reaches 74°C internally.\n\n"
                        + "5. Serve the chicken with the potatoes.";

            case "Banana Pancakes":
                return "1. Peel and mash the banana in a bowl.\n\n"
                        + "2. Beat the eggs and mix them into the banana.\n\n"
                        + "3. Heat a lightly greased pan over medium-low heat.\n\n"
                        + "4. Pour small portions of batter into the pan.\n\n"
                        + "5. Cook until the underside is set, flip carefully, "
                        + "and cook the other side until fully done.";

            case "Milk Porridge":
                return "1. Add the oats and milk to a saucepan.\n\n"
                        + "2. Heat gently, stirring regularly.\n\n"
                        + "3. Simmer until the oats soften and the mixture "
                        + "reaches your preferred thickness.\n\n"
                        + "4. Serve warm.";

            case "Avocado Toast":
                return "1. Toast the bread to your preferred crispness.\n\n"
                        + "2. Cut the avocado in half and remove the stone.\n\n"
                        + "3. Scoop out the flesh and mash it with a fork.\n\n"
                        + "4. Spread the avocado over the toast and serve.";

            case "Garlic Butter Pasta":
                return "1. Cook the pasta according to its package "
                        + "instructions.\n\n"
                        + "2. Peel and finely chop the garlic.\n\n"
                        + "3. Melt the butter in a pan over low heat.\n\n"
                        + "4. Add the garlic and cook briefly without "
                        + "letting it burn.\n\n"
                        + "5. Add the drained pasta and toss to combine.";

            case "Banana Oatmeal":
                return "1. Peel and slice or mash the banana.\n\n"
                        + "2. Add the oats and milk to a saucepan.\n\n"
                        + "3. Cook over low to medium heat, stirring "
                        + "until the oats soften.\n\n"
                        + "4. Stir in the banana and heat through.\n\n"
                        + "5. Serve warm.";

            case "Chicken Sandwich":
                return "1. Cook the chicken thoroughly until it reaches "
                        + "74°C internally.\n\n"
                        + "2. Allow it to rest briefly, then cut or shred it.\n\n"
                        + "3. Place the chicken between the bread slices.\n\n"
                        + "4. Cut the sandwich and serve.";

            case "Tomato Rice":
                return "1. Wash and chop the tomatoes.\n\n"
                        + "2. Cook the rice according to its package "
                        + "instructions.\n\n"
                        + "3. Cook the tomatoes in a pan until softened.\n\n"
                        + "4. Mix the cooked tomatoes with the rice "
                        + "and serve.";

            default:
                return "Cooking instructions are not available for "
                        + "this recipe yet.";
        }
    }
}
