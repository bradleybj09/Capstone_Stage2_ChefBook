package com.example.android.chefbook.activities;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.chefbook.R;
import com.example.android.chefbook.database.MyRecipesContract;
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

    public GridviewFragment() {

    }

    @Override
    public void processFinish(ArrayList<Recipe> output) {
        recipes = output;
        GridView gridView = (GridView)getView().findViewById(gridview);
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
        View rootView = inflater.inflate(R.layout.gridview_parent, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // TODO: 12/17/2016 move the below to method call for searching random recipes, and replace here with MyRecipes call
        fetchTargetedRecipes("macaroni");
    }

    public void fetchRandomRecipes() {
        FetchRecipeGrid fetchRecipeGrid = new FetchRecipeGrid(this);
        fetchRecipeGrid.execute();
    }

    public void fetchMyRecipes() {
        Cursor cursor = contentResolver.query(MyRecipesContract.TableMyRecipes.RECIPE_CONTENT_URI, null, null, null, null);
        GridView gridView = (GridView)getView().findViewById(R.id.gridview);
        myRecipeAdapter = new MyRecipeAdapter(getActivity(),cursor,0);
        gridView.setAdapter(myRecipeAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Recipe recipe = (Recipe)view.getTag();
                ((MainActivity)getActivity()).launchRecipeDetail(recipe.getRecipeID());
            }
        });
    }

    public void fetchTargetedRecipes(String query) {
        FetchRecipeGrid fetchRecipeGrid = new FetchRecipeGrid(this);
        fetchRecipeGrid.execute(query);
    }
}
