package com.iambenbradley.android.chefbook.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.iambenbradley.android.chefbook.R;

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
