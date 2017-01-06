package com.example.android.chefbook.activities;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.chefbook.R;
import com.example.android.chefbook.database.MyRecipesContract;

/**
 * Created by Ben on 1/2/2017.
 */

public class ShoppingListActivity extends AppCompatActivity{
    ContentResolver contentResolver;
    public ShoppingListActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        contentResolver = getContentResolver();
        setContentView(R.layout.shopping_list_activity);
        Cursor rCursor = contentResolver.query(MyRecipesContract.TableMyRecipes.LIST_RECIPE_CONTENT_URI,null,null,null,null);
        Cursor iCursor = contentResolver.query(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_CONTENT_URI,null,null,null,null);
        super.onCreate(savedInstanceState, persistentState);
    }
}
