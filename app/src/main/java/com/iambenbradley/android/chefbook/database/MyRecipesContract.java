package com.iambenbradley.android.chefbook.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ben on 12/8/2016.
 */

public class MyRecipesContract {
    public static final String CONTENT_AUTHORITY = "com.iambenbradley.android.chefbook";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_RECIPES = "myrecipes";
    public static final String PATH_INGREDIENTS = "ingredients";
    public static final String PATH_LIST_RECIPES = "list_recipes";
    public static final String PATH_LIST_INGREDIENTS = "list_ingredients";

    private MyRecipesContract(){}

    public static final class TableMyRecipes implements BaseColumns {

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPES;

        public static final Uri RECIPE_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();
        public static final Uri INGREDIENT_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();
        public static final Uri LIST_RECIPE_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LIST_RECIPES).build();
        public static final Uri LIST_INGREDIENT_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LIST_INGREDIENTS).build();

        public static final String RECIPES_TABLE_NAME = "my_recipes";
        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_RECIPE_TITLE = "recipe_title";
        public static final String COLUMN_RECIPE_IMAGE = "recipe_image";
        public static final String COLUMN_INSTRUCTIONS = "instructions";
        public static final String COLUMN_READY_TIME = "ready_time";
        public static final String COLUMN_SERVINGS = "servings";

        public static final String CREATE_TABLE_RECIPES = "CREATE TABLE " + RECIPES_TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY, "
                + COLUMN_RECIPE_ID + " INTEGER NOT NULL, "
                + COLUMN_RECIPE_TITLE + " TEXT NOT NULL, "
                + COLUMN_RECIPE_IMAGE + " TEXT NOT NULL, "
                + COLUMN_INSTRUCTIONS + " TEXT NOT NULL, "
                + COLUMN_READY_TIME + " TEXT NOT NULL, "
                + COLUMN_SERVINGS + " TEXT NOT NULL"
                + ");";

        public static final String INGREDIENTS_TABLE_NAME = "ingredients";
        public static final String COLUMN_JOIN_RECIPE_ID = "join_recipe_id";
        public static final String COLUMN_INGREDIENT_ID = "ingredient_id";
        public static final String COLUMN_INGREDIENT_NAME = "ingredient_name";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_UNIT = "unit";
        public static final String COLUMN_UNIT_SHORT = "unit_short";
        public static final String COLUMN_UNIT_LONG = "unit_long";
        public static final String COLUMN_ORIGINAL_STRING = "original_string";

        public static final String CREATE_TABLE_INGREDIENTS = "CREATE TABLE " + INGREDIENTS_TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY, "
                + COLUMN_JOIN_RECIPE_ID + " INTEGER NOT NULL, "
                + COLUMN_INGREDIENT_ID + " INTEGER NOT NULL, "
                + COLUMN_INGREDIENT_NAME + " TEXT NOT NULL, "
                + COLUMN_AMOUNT + " REAL NOT NULL, "
                + COLUMN_UNIT + " TEXT NOT NULL, "
                + COLUMN_UNIT_SHORT + " TEXT NOT NULL, "
                + COLUMN_UNIT_LONG + " TEXT NOT NULL, "
                + COLUMN_ORIGINAL_STRING + " TEXT NOT NULL"
                + ");";

        public static final String LIST_RECIPES_TABLE_NAME = "list_recipes";
        public static final String LIST_INGREDIENTS_TABLE_NAME = "list_ingredients";
        public static final String LIST_RECIPE_ID = "list_recipe_id";
        public static final String LIST_RECIPE_NAME = "list_recipe_name";
        public static final String LIST_INGREDIENT_ID = "list_ingredient_id";
        public static final String LIST_INGREDIENT_JOIN_ID = "list_ingredient_join_id";
        public static final String LIST_INGREDIENT_NAME = "list_ingredient_name";
        public static final String LIST_INGREDIENT_AMOUNT = "list_ingredient_amount";
        public static final String LIST_INGREDIENT_UNIT = "list_ingredient_unit";
        public static final String LIST_INGREDIENT_UNIT_SHORT = "list_ingredient_unit_short";
        public static final String LIST_INGREDIENT_UNIT_LONG = "list_ingredient_unit_long";

        public static final String CREATE_TABLE_LIST_RECIPES = "CREATE TABLE " + LIST_RECIPES_TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY, "
                + LIST_RECIPE_ID + " INTEGER NOT NULL, "
                + LIST_RECIPE_NAME + " TEXT NOT NULL"
                + ");";

        public static final String CREATE_TABLE_LIST_INGREDIENTS = "CREATE TABLE " + LIST_INGREDIENTS_TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY, "
                + LIST_INGREDIENT_ID + " INTEGER NOT NULL, "
                + LIST_INGREDIENT_JOIN_ID + " INTEGER NOT NULL, "
                + LIST_INGREDIENT_NAME + " TEXT NOT NULL, "
                + LIST_INGREDIENT_AMOUNT + " REAL NOT NULL, "
                + LIST_INGREDIENT_UNIT + " TEXT NOT NULL, "
                + LIST_INGREDIENT_UNIT_SHORT + " TEXT NOT NULL, "
                + LIST_INGREDIENT_UNIT_LONG + " TEXT NOT NULL"
                + ");";

        public static final String DELETE_TABLE_RECIPES = "DROP TABLE IF EXISTS " + RECIPES_TABLE_NAME;
        public static final String DELETE_TABLE_INGREDIENTS = "DROP TABLE IF EXISTS " + INGREDIENTS_TABLE_NAME;
        public static final String DELETE_TABLE_LIST_RECIPES = "DROP TABLE IF EXISTS " + LIST_RECIPES_TABLE_NAME;
        public static final String DELETE_TABLE_LIST_INGREDIENTS = "DROP TABLE IF EXISTS " + LIST_INGREDIENTS_TABLE_NAME;

        public static Uri buildRecipesUri(long id) {
            return ContentUris.withAppendedId(RECIPE_CONTENT_URI, id);
        }
    }
}
