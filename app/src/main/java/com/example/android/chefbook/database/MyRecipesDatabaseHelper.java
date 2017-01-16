package com.example.android.chefbook.database;

import android.app.ActionBar;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ben on 12/8/2016.
 */

public class MyRecipesDatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "myrecipes.db";

    public MyRecipesDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(MyRecipesContract.TableMyRecipes.CREATE_TABLE_RECIPES);
        sqLiteDatabase.execSQL(MyRecipesContract.TableMyRecipes.CREATE_TABLE_INGREDIENTS);
        sqLiteDatabase.execSQL(MyRecipesContract.TableMyRecipes.CREATE_TABLE_LIST_RECIPES);
        sqLiteDatabase.execSQL(MyRecipesContract.TableMyRecipes.CREATE_TABLE_LIST_INGREDIENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(MyRecipesContract.TableMyRecipes.DELETE_TABLE_RECIPES);
        sqLiteDatabase.execSQL(MyRecipesContract.TableMyRecipes.DELETE_TABLE_INGREDIENTS);
        sqLiteDatabase.execSQL(MyRecipesContract.TableMyRecipes.DELETE_TABLE_LIST_RECIPES);
        sqLiteDatabase.execSQL(MyRecipesContract.TableMyRecipes.DELETE_TABLE_LIST_INGREDIENTS);
        onCreate(sqLiteDatabase);
    }
}
