package com.example.android.chefbook.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.example.android.chefbook.R;
import com.example.android.chefbook.database.MyRecipesContract;
import com.example.android.chefbook.objects.Ingredient;
import com.example.android.chefbook.objects.Recipe;
import com.example.android.chefbook.utilities.FetchRecipeGrid;
import com.example.android.chefbook.utilities.FetchedRecipeAdapter;
import com.example.android.chefbook.utilities.MyRecipeAdapter;

import java.util.ArrayList;

import static com.example.android.chefbook.R.id.gridview;

/**
 * Created by Ben on 12/17/2016.
 */

public class GridviewFragment extends Fragment implements FetchRecipeGrid.AsyncResponse {

    FetchedRecipeAdapter fetchedRecipeAdapter;
    MyRecipeAdapter myRecipeAdapter;
    ArrayList<Recipe> recipes;
    ContentResolver contentResolver;
    GridView gridView;

    public GridviewFragment() {

    }

    @Override
    public void processFinish(ArrayList<Recipe> output) {
        recipes = output;
        gridView = (GridView)getView().findViewById(gridview);
        ViewCompat.setNestedScrollingEnabled(gridView,true);
        fetchedRecipeAdapter = new FetchedRecipeAdapter(getActivity(), output);
        gridView.setAdapter(fetchedRecipeAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainActivity)getActivity()).launchRecipeDetail(recipes.get(i).getRecipeID());
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentResolver = getContext().getContentResolver();
        Log.d("GridviewFrag onCreate","Finished");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.gridview_parent, container, false);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            rootView.findViewById(R.id.gridview).setVisibility(View.GONE);
            rootView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    rootView.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        getActivity().startPostponedEnterTransition();
                    }
                    rootView.findViewById(R.id.gridview).setVisibility(View.VISIBLE);
                    return true;
                }
            });
        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //fetchTargetedRecipes("macaroni");
        fetchMyRecipes();
    }

    public void fetchMyRecipes() {
        Cursor rCursor = contentResolver.query(MyRecipesContract.TableMyRecipes.RECIPE_CONTENT_URI, null, null, null, null);
        gridView = (GridView)getView().findViewById(R.id.gridview);
        ViewCompat.setNestedScrollingEnabled(gridView,true);
        myRecipeAdapter = new MyRecipeAdapter(getActivity(),rCursor,0);
        gridView.setAdapter(myRecipeAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Recipe recipe;
                Cursor cursor = (Cursor)adapterView.getItemAtPosition(i);
                int recipeID = cursor.getInt(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.COLUMN_RECIPE_ID));
                String recipeTitle = cursor.getString(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.COLUMN_RECIPE_TITLE));
                String instructions = cursor.getString(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.COLUMN_INSTRUCTIONS));
                String imageURL = cursor.getString(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.COLUMN_RECIPE_IMAGE));
                int readyInMinutes = cursor.getInt(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.COLUMN_READY_TIME));
                int servings = cursor.getInt(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.COLUMN_SERVINGS));
                String[] args = {String.valueOf(recipeID)};
                Cursor iCursor = contentResolver.query(MyRecipesContract.TableMyRecipes.INGREDIENT_CONTENT_URI, null, MyRecipesContract.TableMyRecipes.COLUMN_JOIN_RECIPE_ID+"=?", args, null);
                iCursor.moveToFirst();
                Log.e("iCursor", DatabaseUtils.dumpCurrentRowToString(iCursor));
                Ingredient[] ingredients = new Ingredient[iCursor.getCount()];
                for (int c = 0; c<ingredients.length; c++){
                    int ingredientID = iCursor.getInt(iCursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.COLUMN_INGREDIENT_ID));
                    String name = iCursor.getString(iCursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.COLUMN_INGREDIENT_NAME));
                    Double amount = iCursor.getDouble(iCursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.COLUMN_AMOUNT));
                    String unit = iCursor.getString(iCursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.COLUMN_UNIT));
                    String unitShort = iCursor.getString(iCursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.COLUMN_UNIT_SHORT));
                    String unitLong = iCursor.getString(iCursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.COLUMN_UNIT_LONG));
                    String originalString = iCursor.getString(iCursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.COLUMN_ORIGINAL_STRING));
                    ingredients[c] = new Ingredient(ingredientID,name,amount,unit,unitShort,unitLong,originalString);
                    iCursor.moveToNext();
                }
                recipe = new Recipe(recipeID,recipeTitle,instructions,readyInMinutes,servings,ingredients,imageURL);
                ((MainActivity)getActivity()).launchRecipeDetail(recipe);
            }
        });
    }

    public void fetchTargetedRecipes(String query) {
        FetchRecipeGrid fetchRecipeGrid = new FetchRecipeGrid(this);
        fetchRecipeGrid.execute(query);
    }
}
