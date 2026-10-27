# Smart Pantry Manager

## Overview
Smart Pantry Manager is an Android application developed in Java to help users manage pantry ingredients and discover recipes they can prepare using the ingredients they have available.

The application provides a simple, user-friendly interface with a pink theme.

## Features
- **Pantry Management:** Add, view, edit, and delete pantry ingredients.
- **Recipe Suggestions:** View recipes that match the ingredients and quantities available in the pantry.
- **Recipe Details:** View recipe descriptions, ingredient requirements, and cooking instructions.
- **Settings:** Save preferences for expiry alerts and unit preferences.
- **Data Persistence:** Uses SQLite to store pantry data locally.
- **Navigation:** Move between the Home, Pantry, Recipes, Recipe Details, and Settings screens.

## Technologies Used
- Java
- Android Studio
- Android SDK
- SQLite
- XML for Android layouts
- Git and GitHub for version control

## Project Structure
- `MainActivity.java` — Main screen and navigation.
- `PantryActivity.java` — Pantry ingredient management.
- `RecipesActivity.java` — Displays matching recipe suggestions.
- `RecipeDetailActivity.java` — Displays recipe ingredients and cooking instructions.
- `SettingsActivity.java` — Manages user preferences.
- `Recipe.java` — Represents recipe data.
- `RecipeRepository.java` — Stores recipes and handles recipe matching.
- `RecipeInstructions.java` — Provides cooking instructions for recipes.

## Requirements
- Android Studio
- A compatible Android SDK
- An Android emulator or physical Android device

## How to Run
1. Clone or download this repository from GitHub.
2. Open Android Studio.
3. Select **Open** and choose the `SmartPantryManager` project folder.
4. Allow Gradle to sync and finish downloading any required dependencies.
5. Connect an Android device or start an emulator.
6. Click **Run** in Android Studio to build and launch the application.

## Data and Preferences
Pantry information is stored locally using SQLite. Application preferences, including the expiry-alert setting and unit preference, are saved using Android SharedPreferences.

## Purpose
This project demonstrates Android application development, user interface design, database operations, recipe matching, activity navigation, and version control.

## Author
Developed as an academic Android application project.

## License
This project was developed for educational purposes.
