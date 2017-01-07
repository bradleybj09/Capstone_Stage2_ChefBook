package com.example.android.chefbook.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.chefbook.R;
import com.example.android.chefbook.database.MyRecipesContract;
import com.example.android.chefbook.objects.Ingredient;
import com.example.android.chefbook.objects.Recipe;
import com.example.android.chefbook.utilities.FetchRandomRecipe;
import com.example.android.chefbook.utilities.FetchRecipeDetail;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ben on 12/11/2016.
 */

public class RecipeDetailFragment extends Fragment implements FetchRecipeDetail.AsyncDetailResponse, FetchRandomRecipe.AsyncRandomResponse {
    int recipeID = 0;
    String instructions;
    String title;
    int readyTime;
    String imageURL;
    int servings;
    Ingredient[] ingredients;
    ContentResolver contentResolver;
    TextView ingredientListLayout;
    ViewGroup mContainer;

    @Override
    public void processRandomFinish(Recipe output) {
        recipeID = output.getRecipeID();
        title = output.getTitle();
        imageURL = output.getRecipeImageURL();
        instructions = output.getInstructions();
        readyTime = output.getReadyinMinutes();
        servings = output.getServings();
        ingredients = output.getIngredients();

        TextView rTitleTextView = (TextView)getView().findViewById(R.id.recipe_detail_title);
        TextView rInstructionsTextView = (TextView)getView().findViewById(R.id.recipe_detail_instructions_body);
        ImageView rImageView = (ImageView)getView().findViewById(R.id.recipe_detail_image);

        rTitleTextView.setText(title);
        rInstructionsTextView.setText(instructions);
        Picasso.with(getActivity()).load(imageURL).into(rImageView);

        Button addRecipeButton = (Button)getView().findViewById(R.id.button_add_recipe);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecipe(v);
            }
        });

        Button addListButton = (Button)getView().findViewById(R.id.button_add_list);
        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToList(v);
            }
        });

        populateIngredients();
    }

    public void populateMyRecipe(Recipe recipe, View view) {
        recipeID = recipe.getRecipeID();
        title = recipe.getTitle();
        imageURL = recipe.getRecipeImageURL();
        instructions = recipe.getInstructions();
        readyTime = recipe.getReadyinMinutes();
        servings = recipe.getServings();
        ingredients = recipe.getIngredients();

        TextView rTitleTextView = (TextView)view.findViewById(R.id.recipe_detail_title);
        TextView rInstructionsTextView = (TextView)view.findViewById(R.id.recipe_detail_instructions_body);
        ImageView rImageView = (ImageView)view.findViewById(R.id.recipe_detail_image);

        Button addRecipeButton = (Button)view.findViewById(R.id.button_add_recipe);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecipe(v);
            }
        });

        Button addListButton = (Button)view.findViewById(R.id.button_add_list);
        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToList(v);
            }
        });

        rTitleTextView.setText(title);
        rInstructionsTextView.setText(instructions);
        Picasso.with(getActivity()).load(imageURL).into(rImageView);

        populateIngredients(view);
    }

    @Override
    public void processFinish(Recipe output) {
        if(Build.VERSION.SDK_INT >= 19) {
            Integer.compare(recipeID, output.getRecipeID());
        }
        title = output.getTitle();
        imageURL = output.getRecipeImageURL();
        instructions = output.getInstructions();
        readyTime = output.getReadyinMinutes();
        servings = output.getServings();
        ingredients = output.getIngredients();

        TextView rTitleTextView = (TextView)getView().findViewById(R.id.recipe_detail_title);
        TextView rInstructionsTextView = (TextView)getView().findViewById(R.id.recipe_detail_instructions_body);
        ImageView rImageView = (ImageView)getView().findViewById(R.id.recipe_detail_image);

        Button addRecipeButton = (Button)getView().findViewById(R.id.button_add_recipe);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecipe(v);
            }
        });

        Button addListButton = (Button)getView().findViewById(R.id.button_add_list);
        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToList(v);
            }
        });

        rTitleTextView.setText(title);
        rInstructionsTextView.setText(instructions);
        Picasso.with(getActivity()).load(imageURL).into(rImageView);

        populateIngredients();
    }

    public void populateIngredients(){
        ingredientListLayout = (TextView)getView().findViewById(R.id.recipe_detail_ingredients_textview);
        for (int i = 0; i < ingredients.length; i++) {
            String originalString = ingredients[i].getOriginalString();
            if(i==0){
                ingredientListLayout.append(originalString);
            } else {
                ingredientListLayout.append("\n" + originalString);
            }
        }
    }
    public void populateIngredients(View view){
        ingredientListLayout = (TextView)view.findViewById(R.id.recipe_detail_ingredients_textview);
        for (int i = 0; i < ingredients.length; i++) {
            String originalString = ingredients[i].getOriginalString();
            if(i==0){
                ingredientListLayout.append(originalString);
            } else {
                ingredientListLayout.append("\n" + originalString);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentResolver = getContext().getContentResolver();
    }

    public RecipeDetailFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContainer = container;
        View rootview = inflater.inflate(R.layout.recipe_detail_fragment,container,false);
        Intent intent = getActivity().getIntent();
        if (intent.hasExtra("full_recipe")){
            Recipe recipe = intent.getExtras().getParcelable("full_recipe");
            populateMyRecipe(recipe,rootview);
            return rootview;
        } else if (intent.getData() == null && getArguments() != null) {
            Bundle b = getArguments();
            recipeID = b.getInt("recipeID");
            if (recipeID == 0) {
                Recipe recipe = b.getParcelable("full_recipe");
                populateMyRecipe(recipe,rootview);
                return rootview;
            }
        }
        else if (intent.hasExtra("recipeID")) {
            recipeID = intent.getExtras().getInt("recipeID");
        }
        else if (intent.hasExtra("random") || getArguments().getInt("random") == 1) {
            FetchRandomRecipe fetchRandomRecipe = new FetchRandomRecipe(this);
            fetchRandomRecipe.execute();
            return rootview;
        }
        if (recipeID == 0) {
            rootview = inflater.inflate(R.layout.start_split_detail,container,false);
            return rootview;
        }
        FetchRecipeDetail fetchRecipeDetail = new FetchRecipeDetail(this);
        fetchRecipeDetail.execute(String.valueOf(recipeID));
        RelativeLayout collapseLayout = (RelativeLayout)rootview.findViewById(R.id.collapse_expand_ingredients);
        collapseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleIngredients();
            }
        });
        return rootview;
    }

    public void addRecipe(View v) {
        ContentValues recipeContentValues = new ContentValues();
        recipeContentValues.put(MyRecipesContract.TableMyRecipes.COLUMN_RECIPE_ID, recipeID);
        recipeContentValues.put(MyRecipesContract.TableMyRecipes.COLUMN_RECIPE_TITLE,title);
        recipeContentValues.put(MyRecipesContract.TableMyRecipes.COLUMN_RECIPE_IMAGE,imageURL);
        recipeContentValues.put(MyRecipesContract.TableMyRecipes.COLUMN_INSTRUCTIONS, instructions);
        recipeContentValues.put(MyRecipesContract.TableMyRecipes.COLUMN_READY_TIME, readyTime);
        recipeContentValues.put(MyRecipesContract.TableMyRecipes.COLUMN_SERVINGS, servings);

        if (contentResolver != null) {
            contentResolver.insert(MyRecipesContract.TableMyRecipes.RECIPE_CONTENT_URI, recipeContentValues);
        }
        else {
            Log.d("add recipe","null content resolver");
        }
        for (int x = 0; x < ingredients.length; x++) {
            ContentValues ingredientContentValues = new ContentValues();
            ingredientContentValues.put(MyRecipesContract.TableMyRecipes.COLUMN_JOIN_RECIPE_ID, recipeID);
            ingredientContentValues.put(MyRecipesContract.TableMyRecipes.COLUMN_INGREDIENT_ID, ingredients[x].getIngredientID());
            ingredientContentValues.put(MyRecipesContract.TableMyRecipes.COLUMN_INGREDIENT_NAME, ingredients[x].getName());
            ingredientContentValues.put(MyRecipesContract.TableMyRecipes.COLUMN_AMOUNT, ingredients[x].getAmount());
            ingredientContentValues.put(MyRecipesContract.TableMyRecipes.COLUMN_UNIT, ingredients[x].getUnit());
            ingredientContentValues.put(MyRecipesContract.TableMyRecipes.COLUMN_UNIT_LONG, ingredients[x].getUnitLong());
            ingredientContentValues.put(MyRecipesContract.TableMyRecipes.COLUMN_UNIT_SHORT, ingredients[x].getUnitShort());
            ingredientContentValues.put(MyRecipesContract.TableMyRecipes.COLUMN_ORIGINAL_STRING, ingredients[x].getOriginalString());

            if (contentResolver != null) {
                contentResolver.insert(MyRecipesContract.TableMyRecipes.INGREDIENT_CONTENT_URI, ingredientContentValues);
            }
            else {
                Log.d("add ingredients","null content resolver");
            }
        }
    }
    public void addToList(View v) {
        ContentValues recipeContentValues = new ContentValues();
        recipeContentValues.put(MyRecipesContract.TableMyRecipes.LIST_RECIPE_ID, recipeID);
        recipeContentValues.put(MyRecipesContract.TableMyRecipes.LIST_RECIPE_NAME, title);
        if (contentResolver != null) {
            contentResolver.insert(MyRecipesContract.TableMyRecipes.LIST_RECIPE_CONTENT_URI, recipeContentValues);
        }
        else {
            Log.d("add recipe","null content resolver");
        }

        for (int x = 0; x < ingredients.length; x++) {
            ContentValues ingredientContentValues = new ContentValues();
            ingredientContentValues.put(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_ID, ingredients[x].getIngredientID());
            ingredientContentValues.put(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_NAME, ingredients[x].getName());
            ingredientContentValues.put(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_AMOUNT, ingredients[x].getAmount());
            ingredientContentValues.put(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_UNIT, ingredients[x].getUnit());
            ingredientContentValues.put(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_UNIT_LONG, ingredients[x].getUnitLong());
            ingredientContentValues.put(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_UNIT_SHORT, ingredients[x].getUnitShort());

            if (contentResolver != null) {
                contentResolver.insert(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_CONTENT_URI, ingredientContentValues);
            }
            else {
                Log.d("add ingredients","null content resolver");
            }
        }
    }
    public void toggleIngredients() {
        TextView plusMinus = (TextView)getView().findViewById(R.id.textview_plus_minus);
        if (ingredientListLayout.getVisibility() == View.VISIBLE){
            plusMinus.setText(getResources().getString(R.string.expand));
            ingredientListLayout.setVisibility(View.GONE);
        } else {
            plusMinus.setText(getResources().getString(R.string.collapse));
            ingredientListLayout.setVisibility(View.VISIBLE);
        }
    }
}