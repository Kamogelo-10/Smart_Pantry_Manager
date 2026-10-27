
package com.example.smartpantrymanager;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    private final List<Recipe> recipes = new ArrayList<>();
    private final OnRecipeClickListener listener;

    private final int pinkDark = Color.rgb(142, 54, 88);
    private final int pinkPrimary = Color.rgb(217, 87, 131);
    private final int pinkLight = Color.rgb(246, 214, 225);
    private final int pinkMuted = Color.rgb(149, 109, 124);

    public RecipeAdapter(
            List<Recipe> recipes,
            OnRecipeClickListener listener) {

        if (recipes != null) {
            this.recipes.addAll(recipes);
        }

        this.listener = listener;
    }

    public void updateRecipes(List<Recipe> newRecipes) {
        recipes.clear();

        if (newRecipes != null) {
            recipes.addAll(newRecipes);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        LinearLayout card = new LinearLayout(parent.getContext());
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(parent, 18), dp(parent, 16),
                dp(parent, 18), dp(parent, 16));

        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.WHITE);
        background.setCornerRadius(dp(parent, 16));
        background.setStroke(dp(parent, 1), pinkLight);
        card.setBackground(background);
        card.setElevation(dp(parent, 2));

        RecyclerView.LayoutParams params =
                new RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

        params.bottomMargin = dp(parent, 12);
        card.setLayoutParams(params);

        TextView name = new TextView(parent.getContext());
        name.setTextSize(19);
        name.setTypeface(null, Typeface.BOLD);
        name.setTextColor(pinkDark);
        card.addView(name);

        TextView description = new TextView(parent.getContext());
        description.setTextSize(14);
        description.setTextColor(pinkMuted);
        description.setPadding(0, dp(parent, 7), 0, dp(parent, 10));
        card.addView(description);

        TextView action = new TextView(parent.getContext());
        action.setText("View recipe and cooking method  \u2192");
        action.setTextSize(14);
        action.setTypeface(null, Typeface.BOLD);
        action.setTextColor(pinkPrimary);
        card.addView(action);

        return new RecipeViewHolder(card, name, description, action);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder, int position) {

        Recipe recipe = recipes.get(position);

        holder.name.setText(recipe.getName());
        holder.description.setText(recipe.getDescription());
        holder.action.setText(
                "View recipe and cooking method  \u2192");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecipeClick(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {

        final TextView name;
        final TextView description;
        final TextView action;

        RecipeViewHolder(
                @NonNull View itemView,
                TextView name,
                TextView description,
                TextView action) {

            super(itemView);
            this.name = name;
            this.description = description;
            this.action = action;
        }
    }

    private int dp(View view, int value) {
        return Math.round(
                value * view.getResources()
                        .getDisplayMetrics().density);
    }
}
