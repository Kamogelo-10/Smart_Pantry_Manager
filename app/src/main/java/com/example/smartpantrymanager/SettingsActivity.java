
package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "SmartPantrySettings";
    private static final String KEY_EXPIRY_ALERT = "expiryAlert";
    private static final String KEY_UNIT = "unitPreference";

    private SharedPreferences preferences;

    private final int pinkDark = Color.rgb(142, 54, 88);
    private final int pinkPrimary = Color.rgb(217, 87, 131);
    private final int pinkLight = Color.rgb(246, 214, 225);
    private final int pinkBackground = Color.rgb(255, 245, 247);
    private final int pinkMuted = Color.rgb(149, 109, 124);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(24), dp(28), dp(24), dp(24));
        root.setBackgroundColor(pinkBackground);

        TextView title = new TextView(this);
        title.setText("Settings & Preferences");
        title.setTextColor(pinkDark);
        title.setTextSize(26);
        title.setTypeface(null, Typeface.BOLD);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("Personalise your Smart Pantry experience.");
        subtitle.setTextColor(pinkMuted);
        subtitle.setTextSize(15);
        LinearLayout.LayoutParams subtitleParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        subtitleParams.topMargin = dp(8);
        subtitleParams.bottomMargin = dp(24);
        root.addView(subtitle, subtitleParams);

        LinearLayout expiryCard = createCard();

        TextView expiryTitle = createLabel("Expiring-soon alerts");
        expiryCard.addView(expiryTitle);

        TextView expiryDescription = createDescription(
                "Enable this preference to indicate that you want "
                        + "reminders about ingredients nearing expiry.");
        LinearLayout.LayoutParams descriptionParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        descriptionParams.topMargin = dp(8);
        expiryCard.addView(expiryDescription, descriptionParams);

        Switch expirySwitch = new Switch(this);
        expirySwitch.setText("Enable alerts");
        expirySwitch.setTextColor(pinkDark);
        expirySwitch.setTextSize(15);
        expirySwitch.setButtonTintList(null);
        expirySwitch.setChecked(
                preferences.getBoolean(KEY_EXPIRY_ALERT, true));
        expirySwitch.setOnCheckedChangeListener((button, checked) ->
                preferences.edit()
                        .putBoolean(KEY_EXPIRY_ALERT, checked)
                        .apply());

        LinearLayout.LayoutParams switchParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        switchParams.topMargin = dp(12);
        expiryCard.addView(expirySwitch, switchParams);

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        cardParams.bottomMargin = dp(16);
        root.addView(expiryCard, cardParams);

        LinearLayout unitCard = createCard();

        TextView unitTitle = createLabel("Unit preference");
        unitCard.addView(unitTitle);

        TextView unitDescription = createDescription(
                "Choose the default unit you prefer to work with.");
        LinearLayout.LayoutParams unitDescriptionParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        unitDescriptionParams.topMargin = dp(8);
        unitCard.addView(unitDescription, unitDescriptionParams);

        String savedUnit = preferences.getString(KEY_UNIT, "g");

        LinearLayout unitOptions = new LinearLayout(this);
        unitOptions.setOrientation(LinearLayout.HORIZONTAL);
        unitOptions.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams optionsParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        optionsParams.topMargin = dp(16);
        unitCard.addView(unitOptions, optionsParams);

        String[] units = {"g", "kg", "ml", "L"};
        for (String unit : units) {
            Button unitButton = new Button(this);
            unitButton.setText(unit);
            unitButton.setAllCaps(false);
            unitButton.setTextSize(14);

            boolean selected = unit.equals(savedUnit);
            unitButton.setTextColor(selected ? Color.WHITE : pinkDark);
            unitButton.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(
                            selected ? pinkPrimary : pinkLight));

            LinearLayout.LayoutParams buttonParams =
                    new LinearLayout.LayoutParams(
                            0, dp(48), 1f);
            buttonParams.setMargins(dp(3), 0, dp(3), 0);
            unitOptions.addView(unitButton, buttonParams);

            unitButton.setOnClickListener(v -> {
                preferences.edit().putString(KEY_UNIT, unit).apply();

                for (int i = 0; i < unitOptions.getChildCount(); i++) {
                    Button button =
                            (Button) unitOptions.getChildAt(i);
                    boolean isSelected =
                            button.getText().toString().equals(unit);

                    button.setTextColor(
                            isSelected ? Color.WHITE : pinkDark);
                    button.setBackgroundTintList(
                            android.content.res.ColorStateList.valueOf(
                                    isSelected ? pinkPrimary : pinkLight));
                }
            });
        }

        LinearLayout.LayoutParams unitCardParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        unitCardParams.bottomMargin = dp(24);
        root.addView(unitCard, unitCardParams);

        TextView note = createDescription(
                "Your preferences are saved automatically on this device.");
        note.setGravity(Gravity.CENTER);
        root.addView(note);

        Button backButton = new Button(this);
        backButton.setText("Back to Home");
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

        setContentView(root);
    }

    private LinearLayout createCard() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(18), dp(18), dp(18), dp(18));

        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.WHITE);
        background.setCornerRadius(dp(18));
        background.setStroke(dp(1), pinkLight);
        card.setBackground(background);

        return card;
    }

    private TextView createLabel(String text) {
        TextView label = new TextView(this);
        label.setText(text);
        label.setTextColor(pinkDark);
        label.setTextSize(18);
        label.setTypeface(null, Typeface.BOLD);
        return label;
    }

    private TextView createDescription(String text) {
        TextView description = new TextView(this);
        description.setText(text);
        description.setTextColor(pinkMuted);
        description.setTextSize(14);
        return description;
    }

    private int dp(float value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density + 0.5f);
    }
}
