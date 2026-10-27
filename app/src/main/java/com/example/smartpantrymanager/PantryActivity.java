
package com.example.smartpantrymanager;

import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PantryActivity extends AppCompatActivity {

    private EditText ingredientName;
    private EditText ingredientQuantity;
    private EditText ingredientUnit;
    private LinearLayout ingredientList;
    private PantryDatabaseHelper databaseHelper;

    private static final int PINK_PRIMARY = Color.rgb(217, 87, 131);
    private static final int PINK_DARK = Color.rgb(142, 54, 88);
    private static final int PINK_LIGHT = Color.rgb(246, 214, 225);
    private static final int PINK_BACKGROUND = Color.rgb(255, 245, 247);
    private static final int PINK_BORDER = Color.rgb(235, 192, 208);
    private static final int PINK_MUTED = Color.rgb(149, 109, 124);
    private static final int WHITE = Color.WHITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = new PantryDatabaseHelper(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(20), dp(24), dp(20), dp(28));
        layout.setBackgroundColor(PINK_BACKGROUND);

        TextView eyebrow = new TextView(this);
        eyebrow.setText("YOUR KITCHEN, ORGANIZED");
        eyebrow.setTextSize(11);
        eyebrow.setLetterSpacing(0.12f);
        eyebrow.setTypeface(null, Typeface.BOLD);
        eyebrow.setTextColor(PINK_PRIMARY);
        layout.addView(eyebrow);

        TextView title = new TextView(this);
        title.setText("My Pantry \u2661");
        title.setTextSize(30);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(PINK_DARK);
        title.setPadding(0, dp(5), 0, dp(6));
        layout.addView(title);

        TextView description = new TextView(this);
        description.setText(
                "Keep track of your ingredients and everything you have on hand.");
        description.setTextSize(15);
        description.setTextColor(PINK_MUTED);
        description.setLineSpacing(dp(2), 1f);

        LinearLayout.LayoutParams descriptionParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        descriptionParams.bottomMargin = dp(22);
        layout.addView(description, descriptionParams);

        // Ingredient entry card
        LinearLayout formCard = new LinearLayout(this);
        formCard.setOrientation(LinearLayout.VERTICAL);
        formCard.setPadding(dp(16), dp(18), dp(16), dp(16));
        formCard.setBackground(createShape(WHITE, PINK_BORDER, 22, 1));
        formCard.setElevation(dp(2));

        TextView formTitle = new TextView(this);
        formTitle.setText("Add an ingredient");
        formTitle.setTextSize(19);
        formTitle.setTypeface(null, Typeface.BOLD);
        formTitle.setTextColor(PINK_DARK);

        LinearLayout.LayoutParams formTitleParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        formTitleParams.bottomMargin = dp(12);
        formCard.addView(formTitle, formTitleParams);

        ingredientName = createInput("Ingredient name (e.g. Rice)");
        formCard.addView(ingredientName);

        ingredientQuantity = createInput("Quantity (e.g. 2)");
        ingredientQuantity.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL
                        | InputType.TYPE_NUMBER_FLAG_SIGNED);
        formCard.addView(ingredientQuantity);

        ingredientUnit = createInput("Unit (e.g. kg, g, pieces)");
        formCard.addView(ingredientUnit);

        Button addButton = createButton("＋  Add Ingredient", true);

        LinearLayout.LayoutParams addParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        addParams.topMargin = dp(10);
        formCard.addView(addButton, addParams);

        LinearLayout.LayoutParams formCardParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        formCardParams.bottomMargin = dp(26);
        layout.addView(formCard, formCardParams);

        // Pantry list heading
        TextView listTitle = new TextView(this);
        listTitle.setText("Your ingredients");
        listTitle.setTextSize(23);
        listTitle.setTypeface(null, Typeface.BOLD);
        listTitle.setTextColor(PINK_DARK);
        layout.addView(listTitle);

        TextView listDescription = new TextView(this);
        listDescription.setText("Everything you have, all in one place.");
        listDescription.setTextSize(14);
        listDescription.setTextColor(PINK_MUTED);

        LinearLayout.LayoutParams listDescriptionParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        listDescriptionParams.topMargin = dp(4);
        listDescriptionParams.bottomMargin = dp(16);
        layout.addView(listDescription, listDescriptionParams);

        ingredientList = new LinearLayout(this);
        ingredientList.setOrientation(LinearLayout.VERTICAL);
        layout.addView(
                ingredientList,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.addView(
                layout,
                new ScrollView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

        addButton.setOnClickListener(v -> addIngredient());

        setContentView(scrollView);
        loadIngredients();
    }

    private EditText createInput(String hint) {
        EditText input = new EditText(this);
        input.setHint(hint);
        input.setSingleLine(true);
        input.setTextColor(PINK_DARK);
        input.setHintTextColor(PINK_MUTED);
        input.setTextSize(15);
        input.setPadding(dp(13), dp(11), dp(13), dp(11));
        input.setBackground(createShape(WHITE, PINK_BORDER, 14, 1));
        input.setBackgroundTintList(null);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = dp(10);
        input.setLayoutParams(params);

        return input;
    }

    private Button createButton(String text, boolean primary) {
        Button button = new Button(this);
        button.setText(text);
        button.setAllCaps(false);
        button.setTextSize(15);
        button.setTypeface(null, Typeface.BOLD);
        button.setMinHeight(dp(48));
        button.setPadding(dp(16), dp(10), dp(16), dp(10));
        button.setTextColor(primary ? WHITE : PINK_DARK);
        button.setBackground(
                createShape(
                        primary ? PINK_PRIMARY : PINK_LIGHT,
                        primary ? PINK_PRIMARY : PINK_BORDER,
                        15,
                        1));
        button.setBackgroundTintList(null);
        button.setElevation(dp(1));
        return button;
    }

    private GradientDrawable createShape(
            int fillColor, int strokeColor, int radius, int strokeWidth) {
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(fillColor);
        shape.setCornerRadius(dp(radius));
        if (strokeWidth > 0) {
            shape.setStroke(dp(strokeWidth), strokeColor);
        }
        return shape;
    }

    private void addIngredient() {
        String name = ingredientName.getText().toString().trim();
        String quantityText =
                ingredientQuantity.getText().toString().trim();
        String unit = ingredientUnit.getText().toString().trim();

        if (name.isEmpty() || quantityText.isEmpty() || unit.isEmpty()) {
            showMessage("Please fill in all fields.");
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            showMessage("Enter a valid quantity.");
            return;
        }

        if (Double.isNaN(quantity) || Double.isInfinite(quantity)
                || quantity <= 0) {
            showMessage("Quantity must be greater than zero.");
            return;
        }

        long result = databaseHelper.addIngredient(name, quantity, unit);

        if (result == -1) {
            showMessage("Could not save ingredient.");
            return;
        }

        clearForm();
        loadIngredients();
        showMessage("Ingredient saved!");
    }

    private void loadIngredients() {
        ingredientList.removeAllViews();

        Cursor cursor = databaseHelper.getAllIngredients();

        try {
            if (cursor.getCount() == 0) {
                TextView emptyMessage = new TextView(this);
                emptyMessage.setText(
                        "\u2661  Your pantry is waiting for its first "
                                + "ingredient. Add one above!");
                emptyMessage.setTextSize(15);
                emptyMessage.setTextColor(PINK_MUTED);
                emptyMessage.setPadding(
                        dp(18), dp(22), dp(18), dp(22));
                emptyMessage.setBackground(
                        createShape(WHITE, PINK_BORDER, 18, 1));
                ingredientList.addView(emptyMessage);
            }

            while (cursor.moveToNext()) {
                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                PantryDatabaseHelper.COLUMN_ID));

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                PantryDatabaseHelper.COLUMN_NAME));

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(
                                PantryDatabaseHelper.COLUMN_QUANTITY));

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                PantryDatabaseHelper.COLUMN_UNIT));

                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.VERTICAL);
                row.setPadding(dp(16), dp(15), dp(16), dp(12));
                row.setBackground(
                        createShape(WHITE, PINK_BORDER, 18, 1));
                row.setElevation(dp(2));

                LinearLayout.LayoutParams rowParams =
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                rowParams.bottomMargin = dp(12);

                TextView ingredient = new TextView(this);
                ingredient.setText(name);
                ingredient.setTextSize(17);
                ingredient.setTypeface(null, Typeface.BOLD);
                ingredient.setTextColor(PINK_DARK);
                row.addView(ingredient);

                TextView quantityLabel = new TextView(this);
                quantityLabel.setText(
                        formatQuantity(quantity) + " " + unit);
                quantityLabel.setTextSize(14);
                quantityLabel.setTextColor(PINK_MUTED);

                LinearLayout.LayoutParams quantityParams =
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                quantityParams.topMargin = dp(3);
                quantityParams.bottomMargin = dp(10);
                row.addView(quantityLabel, quantityParams);

                LinearLayout buttons = new LinearLayout(this);
                buttons.setOrientation(LinearLayout.HORIZONTAL);

                Button editButton = createButton("Edit", false);
                Button deleteButton = createButton("Delete", true);

                LinearLayout.LayoutParams editParams =
                        new LinearLayout.LayoutParams(0, dp(44), 1f);
                editParams.rightMargin = dp(8);

                LinearLayout.LayoutParams deleteParams =
                        new LinearLayout.LayoutParams(0, dp(44), 1f);

                buttons.addView(editButton, editParams);
                buttons.addView(deleteButton, deleteParams);
                row.addView(buttons);

                ingredientList.addView(row, rowParams);

                editButton.setOnClickListener(
                        v -> showEditDialog(id, name, quantity, unit));

                deleteButton.setOnClickListener(
                        v -> confirmDelete(id, name));
            }
        } finally {
            cursor.close();
        }
    }

    private String formatQuantity(double quantity) {
        if (quantity == Math.floor(quantity)
                && !Double.isInfinite(quantity)) {
            return String.valueOf((long) quantity);
        }
        return String.valueOf(quantity);
    }

    private void showEditDialog(
            int id, String currentName,
            double currentQuantity, String currentUnit) {

        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);
        form.setPadding(dp(20), dp(8), dp(20), dp(8));

        EditText nameInput = createInput("Ingredient name");
        nameInput.setText(currentName);
        form.addView(nameInput);

        EditText quantityInput = createInput("Quantity");
        quantityInput.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL
                        | InputType.TYPE_NUMBER_FLAG_SIGNED);
        quantityInput.setText(formatQuantity(currentQuantity));
        form.addView(quantityInput);

        EditText unitInput = createInput("Unit");
        unitInput.setText(currentUnit);
        form.addView(unitInput);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit Ingredient")
                .setView(form)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(PINK_PRIMARY);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(PINK_DARK);

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener(v -> {
                        String name =
                                nameInput.getText().toString().trim();
                        String quantityText =
                                quantityInput.getText().toString().trim();
                        String unit =
                                unitInput.getText().toString().trim();

                        if (name.isEmpty() || quantityText.isEmpty()
                                || unit.isEmpty()) {
                            showMessage("Please fill in all fields.");
                            return;
                        }

                        double quantity;

                        try {
                            quantity = Double.parseDouble(quantityText);
                        } catch (NumberFormatException e) {
                            showMessage("Enter a valid quantity.");
                            return;
                        }

                        if (Double.isNaN(quantity)
                                || Double.isInfinite(quantity)
                                || quantity <= 0) {
                            showMessage(
                                    "Quantity must be greater than zero.");
                            return;
                        }

                        int result = databaseHelper.updateIngredient(
                                id, name, quantity, unit);

                        if (result > 0) {
                            loadIngredients();
                            showMessage("Ingredient updated!");
                            dialog.dismiss();
                        } else {
                            showMessage("Could not update ingredient.");
                        }
                    });
        });

        dialog.show();
    }

    private void confirmDelete(int id, String name) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete Ingredient")
                .setMessage("Remove " + name + " from your pantry?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (d, which) -> {
                    int result = databaseHelper.deleteIngredient(id);

                    if (result > 0) {
                        loadIngredients();
                        showMessage("Ingredient deleted.");
                    } else {
                        showMessage("Could not delete ingredient.");
                    }
                })
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(PINK_PRIMARY);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(PINK_DARK);
        });

        dialog.show();
    }

    private void clearForm() {
        ingredientName.setText("");
        ingredientQuantity.setText("");
        ingredientUnit.setText("");
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private int dp(int value) {
        return Math.round(
                value * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
