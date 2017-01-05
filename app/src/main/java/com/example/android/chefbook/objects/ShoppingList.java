package com.example.android.chefbook.objects;

import java.util.ArrayList;

/**
 * Created by Ben on 12/7/2016.
 */

public class ShoppingList {
    private ArrayList<Recipe> recipes;
    private ArrayList<Ingredient> ingredients;

    public ShoppingList() {
        recipes = new ArrayList<>();
        ingredients = new ArrayList<>();
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    public void removeRecipe(int position){
        recipes.remove(position);
    }

    public void toggleIngredient(int position){
        ingredients.get(position).toggleIngredient();
    }

    public void clearList() {
        recipes = new ArrayList<>();
        ingredients = new ArrayList<>();
    }
}
