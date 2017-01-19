package com.example.android.chefbook.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.View;

import com.example.android.chefbook.R;

public class RecipeDetailActivity extends AppCompatActivity {

    RecipeDetailFragment recipeDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_activity);
        if (savedInstanceState == null) {
            recipeDetailFragment = new RecipeDetailFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.recipe_detail_container, recipeDetailFragment)
                    .commit();
        }
    }

    public void onIngredientToggleClick(View view) {
        recipeDetailFragment.toggleIngredients();
    }
}
