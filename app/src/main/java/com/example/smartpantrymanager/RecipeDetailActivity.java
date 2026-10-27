
package com.example.smartpantrymanager;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class RecipeDetailActivity extends AppCompatActivity {

    private final int pinkDark = Color.rgb(142, 54, 88);
    private final int pinkPrimary = Color.rgb(217, 87, 131);
    private final int pinkBackground = Color.rgb(255, 245, 247);
    private final int pinkLight = Color.rgb(246, 214, 225);
    private final int pinkMuted = Color.rgb(149, 109, 124);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String name = getIntent().getStringExtra("recipe_name");
        String description = getIntent().getStringExtra("recipe_description");
        String ingredients = getIntent().getStringExtra("recipe_ingredients");

        if (name == null) name = "Recipe Details";
        if (description == null) description = "";
        if (ingredients == null) ingredients = "No ingredients available.";

        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setBackgroundColor(pinkBackground);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(22), dp(28), dp(22), dp(28));
        root.setBackgroundColor(pinkBackground);

        TextView heading = new TextView(this);
        heading.setText(name);
        heading.setTextSize(29);
        heading.setTypeface(null, Typeface.BOLD);
        heading.setTextColor(pinkDark);
        root.addView(heading);

        TextView intro = new TextView(this);
        intro.setText(description);
        intro.setTextSize(16);
        intro.setTextColor(pinkMuted);
        intro.setPadding(0, dp(10), 0, dp(22));
        root.addView(intro);

        root.addView(createSectionTitle("Required Ingredients"));
        root.addView(createCard(ingredients));

        TextView methodHeading = createSectionTitle("Cooking Method");
        LinearLayout.LayoutParams methodParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        methodParams.topMargin = dp(24);
        root.addView(methodHeading, methodParams);

        String instructions =
                RecipeInstructions.getInstructions(name);

        root.addView(createCard(instructions));

        TextView safetyNote = new TextView(this);
        safetyNote.setText(
                "Food safety: Cook chicken thoroughly to 74°C "
                        + "internally. Follow package instructions "
                        + "where applicable.");
        safetyNote.setTextSize(13);
        safetyNote.setTextColor(pinkMuted);
        safetyNote.setPadding(dp(4), dp(18), dp(4), dp(4));
        root.addView(safetyNote);

        Button backButton = new Button(this);
        backButton.setText("Back to Recipes");
        backButton.setAllCaps(false);
        backButton.setTextColor(Color.WHITE);
        backButton.setTextSize(16);
        backButton.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(pinkPrimary));

        LinearLayout.LayoutParams backParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(52));
        backParams.topMargin = dp(24);
        root.addView(backButton, backParams);

        backButton.setOnClickListener(v -> finish());

        scrollView.addView(root);
        setContentView(scrollView);
    }

    private TextView createSectionTitle(String text) {
        TextView title = new TextView(this);
        title.setText(text);
        title.setTextSize(20);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(pinkDark);
        return title;
    }

    private TextView createCard(String text) {
        TextView card = new TextView(this);
        card.setText(text);
        card.setTextSize(16);
        card.setTextColor(pinkDark);
        card.setLineSpacing(dp(4), 1f);
        card.setPadding(dp(18), dp(18), dp(18), dp(18));

        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.WHITE);
        background.setCornerRadius(dp(16));
        background.setStroke(dp(1), pinkLight);
        card.setBackground(background);

        return card;
    }

    private int dp(int value) {
        return Math.round(
                value * getResources().getDisplayMetrics().density);
    }
}
