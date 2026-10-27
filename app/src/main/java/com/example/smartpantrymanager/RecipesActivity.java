
package com.example.smartpantrymanager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

public class RecipesActivity extends AppCompatActivity {

    private final int pinkPrimary = Color.rgb(217, 87, 131);
    private final int pinkDark = Color.rgb(142, 54, 88);
    private final int pinkBackground = Color.rgb(255, 245, 247);
    private final int pinkLight = Color.rgb(246, 214, 225);
    private final int pinkMuted = Color.rgb(149, 109, 124);

    private RecipeRepository repository;
    private RecipeAdapter adapter;

    private TextView status;
    private TextView emptyMessage;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        repository = new RecipeRepository();

        buildScreen();
        showMatchingRecipes();
    }

    private void buildScreen() {
        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setBackgroundColor(pinkBackground);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(28), dp(20), dp(24));
        root.setBackgroundColor(pinkBackground);

        TextView title = new TextView(this);
        title.setText("Suggested Recipes");
        title.setTextSize(28);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(pinkDark);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText(
                "Discover meals you can make with the ingredients "
                        + "currently in your pantry.");
        subtitle.setTextSize(15);
        subtitle.setTextColor(pinkMuted);
        subtitle.setPadding(0, dp(8), 0, dp(20));
        root.addView(subtitle);

        status = new TextView(this);
        status.setTextSize(16);
        status.setTypeface(null, Typeface.BOLD);
        status.setTextColor(pinkPrimary);
        status.setPadding(0, 0, 0, dp(12));
        root.addView(status);

        emptyMessage = new TextView(this);
        emptyMessage.setTextSize(15);
        emptyMessage.setTextColor(pinkMuted);
        emptyMessage.setGravity(Gravity.CENTER);
        emptyMessage.setPadding(dp(16), dp(24), dp(16), dp(24));
        emptyMessage.setVisibility(View.GONE);
        root.addView(emptyMessage);

        recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setClipToPadding(false);

        adapter = new RecipeAdapter(
                Collections.emptyList(),
                this::showRecipeDetails);

        recyclerView.setAdapter(adapter);

        root.addView(
                recyclerView,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

        Button refreshButton = createButton("Refresh Suggestions");
        LinearLayout.LayoutParams refreshParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(50));
        refreshParams.topMargin = dp(18);
        root.addView(refreshButton, refreshParams);

        refreshButton.setOnClickListener(v -> showMatchingRecipes());

        Button backButton = createButton("Back to Home");
        LinearLayout.LayoutParams backParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(50));
        backParams.topMargin = dp(10);
        root.addView(backButton, backParams);

        backButton.setOnClickListener(v -> finish());

        scrollView.addView(root);
        setContentView(scrollView);
    }

    private Button createButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setAllCaps(false);
        button.setTextSize(15);
        button.setTypeface(null, Typeface.BOLD);
        button.setTextColor(Color.WHITE);
        button.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                        pinkPrimary));
        return button;
    }

    private void showMatchingRecipes() {
        if (repository == null) {
            repository = new RecipeRepository();
        }

        java.util.List<Recipe> matchingRecipes =
                repository.getMatchingRecipes(this);

        status.setText(
                matchingRecipes.size()
                        + (matchingRecipes.size() == 1
                        ? " recipe you can make"
                        : " recipes you can make"));

        adapter.updateRecipes(matchingRecipes);

        if (matchingRecipes.isEmpty()) {
            emptyMessage.setText(
                    "No complete recipe matches your pantry yet.\n\n"
                            + "Add or update your ingredients and quantities "
                            + "in the Pantry screen, then refresh your "
                            + "suggestions.");
            emptyMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyMessage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showRecipeDetails(Recipe recipe) {
        StringBuilder ingredients = new StringBuilder();

        for (Map.Entry<String, Double> ingredient
                : recipe.getIngredients().entrySet()) {

            double quantity = ingredient.getValue();

            String quantityText = quantity == (long) quantity
                    ? String.valueOf((long) quantity)
                    : String.format(
                    Locale.getDefault(), "%s", quantity);

            ingredients.append("\u2022 ")
                    .append(ingredient.getKey())
                    .append(" \u2014 ")
                    .append(quantityText)
                    .append(" ")
                    .append(recipe.getIngredientUnit(
                            ingredient.getKey()))
                    .append("\n\n");
        }

        Intent intent = new Intent(
                RecipesActivity.this,
                RecipeDetailActivity.class);

        intent.putExtra("recipe_name", recipe.getName());
        intent.putExtra(
                "recipe_description", recipe.getDescription());
        intent.putExtra(
                "recipe_ingredients", ingredients.toString());

        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (repository != null && adapter != null) {
            showMatchingRecipes();
        }
    }

    private int dp(int value) {
        return Math.round(
                value * getResources()
                        .getDisplayMetrics().density);
    }
}
