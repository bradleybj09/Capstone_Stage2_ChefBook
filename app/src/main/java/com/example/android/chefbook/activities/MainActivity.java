package com.example.android.chefbook.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.chefbook.R;
import com.example.android.chefbook.database.MyRecipesContract;
import com.example.android.chefbook.objects.Recipe;
import com.example.android.chefbook.utilities.MyRecipeAdapter;

public class MainActivity extends AppCompatActivity {
    private boolean mTwoPane;
    ContentResolver contentResolver;
    MyRecipeAdapter myRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        contentResolver = getContentResolver();
        if (findViewById(R.id.recipe_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.recipe_detail_container, new RecipeDetailFragment(),null)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
        Log.d("MainActivity/onCreate","Finished");
    }

    public void launchRecipeDetail(int recipeID) {
        Log.d("MainLaunchDetail","Initiated");
        if (mTwoPane) {
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            Bundle b = new Bundle();
            b.putInt("recipeID",recipeID);
            recipeDetailFragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.recipe_detail_container, recipeDetailFragment,null).commit();
        } else {
            Intent intent = new Intent(this, RecipeDetailActivity.class).putExtra("recipeID", recipeID);
            startActivity(intent);
        }
    }

    public void getRandomRecipe() {
        Log.d("MainLaunchDetail","Initiated");
        if (mTwoPane) {
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            Bundle b = new Bundle();
            b.putInt("random",1);
            recipeDetailFragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.recipe_detail_container, recipeDetailFragment,null).commit();
        } else {
            Intent intent = new Intent(this, RecipeDetailActivity.class).putExtra("random", 1);
            startActivity(intent);
        }
    }

    public void mainFetchMyRecipes() {
        Cursor cursor = contentResolver.query(MyRecipesContract.TableMyRecipes.RECIPE_CONTENT_URI, null, null, null, null);
        GridView gridView = (GridView)findViewById(R.id.gridview);
        myRecipeAdapter = new MyRecipeAdapter(this,cursor,0);
        gridView.setAdapter(myRecipeAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Recipe recipe = (Recipe)view.getTag();
                launchRecipeDetail(recipe.getRecipeID());
            }
        });
    }
}
