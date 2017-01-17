package com.example.android.chefbook.activities;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.android.chefbook.R;
import com.example.android.chefbook.database.MyRecipesContract;
import com.example.android.chefbook.database.MyRecipesDatabaseHelper;
import com.example.android.chefbook.objects.Ingredient;
import com.example.android.chefbook.utilities.ListIngredientAdapter;
import com.example.android.chefbook.utilities.ListRecipeAdapter;

/**
 * Created by Ben on 1/2/2017.
 */

public class ShoppingListActivity extends AppCompatActivity{
    ContentResolver contentResolver;
    ListRecipeAdapter listRecipeAdapter;
    ListIngredientAdapter listIngredientAdapter;
    View upButton;
    FrameLayout emptyLayout;

    public ShoppingListActivity() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        contentResolver = getContentResolver();
        setContentView(R.layout.shopping_list_activity);
        emptyLayout = (FrameLayout)findViewById(R.id.empty_list_layout);
        ListView recipeListView = (ListView)findViewById(R.id.list_recipe_listview);
        Cursor rCursor = contentResolver.query(MyRecipesContract.TableMyRecipes.LIST_RECIPE_CONTENT_URI, null, null, null, null);
        if (rCursor.getCount() > 0) {
            populateIngredientList();
            listRecipeAdapter = new ListRecipeAdapter(this, rCursor, 0);
            recipeListView.setAdapter(listRecipeAdapter);
        }
        else {
             emptyLayout.setVisibility(View.VISIBLE);
        }
        setSupportActionBar((Toolbar)findViewById(R.id.list_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map:
                String lat = "";
                String lon = "";
                Uri gmmIntentUri= Uri.parse("geo:" + lat + "," + lon + "?q=grocery stores");
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void populateIngredientList() {
        MyRecipesDatabaseHelper myRecipesDatabaseHelper = new MyRecipesDatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase = myRecipesDatabaseHelper.getReadableDatabase();
        ListView ingredientListView = (ListView)findViewById(R.id.list_ingredient_listview);

        String sql = "SELECT " + BaseColumns._ID + ", "
                + MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_ID + ", "
                + MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_NAME + ", "
                + "sum(" + MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_AMOUNT + ") AS " + MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_AMOUNT + ", "
                + MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_UNIT + ", "
                + MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_UNIT_LONG + " FROM "
                + MyRecipesContract.TableMyRecipes.LIST_INGREDIENTS_TABLE_NAME + " GROUP BY "
                + MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_UNIT + ", "
                + MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_NAME + " ORDER BY "
                + MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_NAME + " ASC";

        Cursor iCursor = sqLiteDatabase.rawQuery(sql, null);
        iCursor.moveToFirst();
        listIngredientAdapter = new ListIngredientAdapter(this, iCursor, 0);
        ingredientListView.setAdapter(listIngredientAdapter);
        sqLiteDatabase.close();
    }

    public void removeListRecipe(View view) {
        RelativeLayout relativeLayout = (RelativeLayout)((ViewGroup)view.getParent()).findViewById(R.id.shopping_list_recipe);
        int recipeID = (int)relativeLayout.getTag();
        contentResolver.delete(MyRecipesContract.TableMyRecipes.LIST_RECIPE_CONTENT_URI, MyRecipesContract.TableMyRecipes.LIST_RECIPE_ID + " = " + String.valueOf(recipeID),null);
        contentResolver.delete(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_CONTENT_URI, MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_JOIN_ID + " = " + String.valueOf(recipeID),null);
        Cursor rCursor = contentResolver.query(MyRecipesContract.TableMyRecipes.LIST_RECIPE_CONTENT_URI, null, null, null, null);
        if (rCursor.getCount() > 0) {
            ListView recipeListView = (ListView) findViewById(R.id.list_recipe_listview);
            listRecipeAdapter = new ListRecipeAdapter(this, rCursor, 0);
            recipeListView.setAdapter(listRecipeAdapter);
            populateIngredientList();
        }
        else {
            emptyLayout.setVisibility(View.VISIBLE);
            populateIngredientList();
        }
    }

    public void removeListIngredient(View view) {
        RelativeLayout relativeLayout = (RelativeLayout)((ViewGroup)view.getParent()).findViewById(R.id.shopping_list_ingredient);
        Ingredient ingredient = (Ingredient)relativeLayout.getTag();
        String where = MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_ID + "=? AND " + MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_UNIT + "=?";
        String[] args = new String[] { String.valueOf(ingredient.getIngredientID()), ingredient.getUnit()};
        contentResolver.delete(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_CONTENT_URI, where, args);

        populateIngredientList();
    }

    public void finishShopping(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.clear_shopping)
                .setMessage(getResources().getString(R.string.clear_message))
                .setPositiveButton("I'm sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contentResolver.delete(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_CONTENT_URI, null, null);
                        contentResolver.delete(MyRecipesContract.TableMyRecipes.LIST_RECIPE_CONTENT_URI, null, null);
                        recreate();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
