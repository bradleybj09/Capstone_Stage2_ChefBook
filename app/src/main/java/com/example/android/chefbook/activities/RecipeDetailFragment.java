package com.example.android.chefbook.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

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
    CollapsingToolbarLayout collapsingToolbarLayout;
    Button addRecipeButton;
    Button addListButton;
    Button removeRecipeButton;

    @Override
    public void processRandomFinish(Recipe output) {
        recipeID = output.getRecipeID();
        title = output.getTitle();
        imageURL = output.getRecipeImageURL();
        instructions = output.getInstructions();
        readyTime = output.getReadyinMinutes();
        servings = output.getServings();
        ingredients = output.getIngredients();

        TextView rInstructionsTextView = (TextView)getView().findViewById(R.id.recipe_detail_instructions_body);
        ImageView rImageView = (ImageView)getView().findViewById(R.id.recipe_detail_image);

        finalizeUI();
        rInstructionsTextView.setText(instructions);
        Picasso.with(getActivity()).load(imageURL).into(rImageView);

        removeRecipeButton = (Button)getView().findViewById(R.id.button_remove_recipe);
        removeRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeRecipe();
            }
        });

        addRecipeButton = (Button)getView().findViewById(R.id.button_add_recipe);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecipe(v);
            }
        });

        addListButton = (Button)getView().findViewById(R.id.button_add_list);
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

        TextView rInstructionsTextView = (TextView)view.findViewById(R.id.recipe_detail_instructions_body);
        ImageView rImageView = (ImageView)view.findViewById(R.id.recipe_detail_image);

        removeRecipeButton = (Button)view.findViewById(R.id.button_remove_recipe);
        removeRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeRecipe();
            }
        });

        addRecipeButton = (Button)view.findViewById(R.id.button_add_recipe);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecipe(v);
            }
        });

        addListButton = (Button)view.findViewById(R.id.button_add_list);
        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToList(v);
            }
        });

        finalizeUI();
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

        TextView rInstructionsTextView = (TextView)getView().findViewById(R.id.recipe_detail_instructions_body);
        ImageView rImageView = (ImageView)getView().findViewById(R.id.recipe_detail_image);

        removeRecipeButton = (Button)getView().findViewById(R.id.button_remove_recipe);
        removeRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeRecipe();
            }
        });

        addRecipeButton = (Button)getView().findViewById(R.id.button_add_recipe);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecipe(v);
            }
        });

        addListButton = (Button)getView().findViewById(R.id.button_add_list);
        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToList(v);
            }
        });

        finalizeUI();
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

    public void finalizeUI() {
        if(title.length() > 25){
            collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.LongTitle);
        }
        if(title.length() > 30){
            collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.VeryLongTitle);
            collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.VeryLongTitle);
        }
        collapsingToolbarLayout.setTitle(title);

        if (isMyRecipe()) {
            addRecipeButton.setVisibility(View.GONE);
            removeRecipeButton.setVisibility(View.VISIBLE);
        }
        else {
            addRecipeButton.setVisibility(View.VISIBLE);
            removeRecipeButton.setVisibility(View.GONE);
        }
    }

    public boolean isMyRecipe(){
        Cursor c = contentResolver.query(MyRecipesContract.TableMyRecipes.RECIPE_CONTENT_URI, null, MyRecipesContract.TableMyRecipes.COLUMN_RECIPE_ID+ " = " + recipeID, null, null);
        if (c != null) {
            if (c.getCount() > 0) {
                c.close();
                return true;
            } else {
                c.close();
                return false;
            }
        } return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContainer = container;
        View rootView = inflater.inflate(R.layout.recipe_detail_fragment,container,false);
        Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.detail_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        AppBarLayout appBarLayout = (AppBarLayout)rootView.findViewById(R.id.detail_appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
            }
        });
        collapsingToolbarLayout = (CollapsingToolbarLayout)rootView.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        Intent intent = getActivity().getIntent();
        if (intent.hasExtra("full_recipe")){
            Recipe recipe = intent.getExtras().getParcelable("full_recipe");
            populateMyRecipe(recipe,rootView);
            return rootView;
        } else if (intent.getData() == null && getArguments() != null) {
            Bundle b = getArguments();
            recipeID = b.getInt("recipeID");
            if (recipeID == 0) {
                Recipe recipe = b.getParcelable("full_recipe");
                populateMyRecipe(recipe,rootView);
                return rootView;
            }
        }
        else if (intent.hasExtra("recipeID")) {
            recipeID = intent.getExtras().getInt("recipeID");
        }
        else if (intent.hasExtra("random") || getArguments().getInt("random") == 1) {
            FetchRandomRecipe fetchRandomRecipe = new FetchRandomRecipe(this);
            fetchRandomRecipe.execute();
            return rootView;
        }
        if (recipeID == 0) {
            rootView = inflater.inflate(R.layout.start_split_detail,container,false);
            return rootView;
        }
        FetchRecipeDetail fetchRecipeDetail = new FetchRecipeDetail(this);
        fetchRecipeDetail.execute(String.valueOf(recipeID));
        RelativeLayout collapseLayout = (RelativeLayout)rootView.findViewById(R.id.collapse_expand_ingredients);
        collapseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleIngredients();
            }
        });
        return rootView;
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
    public void removeRecipe() {
        contentResolver.delete(MyRecipesContract.TableMyRecipes.RECIPE_CONTENT_URI, MyRecipesContract.TableMyRecipes.COLUMN_RECIPE_ID + " = " + recipeID,null) ;
        contentResolver.delete(MyRecipesContract.TableMyRecipes.INGREDIENT_CONTENT_URI, MyRecipesContract.TableMyRecipes.COLUMN_JOIN_RECIPE_ID + " = " + recipeID, null);
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