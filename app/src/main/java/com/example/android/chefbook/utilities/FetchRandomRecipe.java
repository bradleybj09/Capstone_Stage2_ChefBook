package com.example.android.chefbook.utilities;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.chefbook.objects.Ingredient;
import com.example.android.chefbook.objects.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.interfaces.DSAParams;
import java.util.ArrayList;

/**
 * Created by Ben on 12/11/2016.
 */

public class FetchRandomRecipe extends AsyncTask<String, Void, Recipe> {

    public interface AsyncRandomResponse {
        void processRandomFinish(Recipe output);
    }

    public AsyncRandomResponse delegate = null;

    public FetchRandomRecipe(AsyncRandomResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Recipe doInBackground(String... voids) {
        Log.d("doInBackground", "Initiated");
        String apiKey = "yMn7M1DywmmshjLnVrGx90sD2ESIp1XfB2ijsnfU7kDaPhYGLb";
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        String recipeJsonStr;
        String currentURL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/random?limitLicense=false&number=2";
        int finalID = 0;
        while (finalID == 0) {
            try {
                URL url = new URL(currentURL);
                Log.d("URL", currentURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("X-Mashape-Key", apiKey);
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(false);
                Log.d("Response Code", String.valueOf(urlConnection.getResponseCode()));
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                if (inputStream == null) {
                    recipeJsonStr = null;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }

                if (stringBuffer.length() == 0) {
                    recipeJsonStr = null;
                }
                recipeJsonStr = stringBuffer.toString();
                Log.d("JSON", recipeJsonStr);
                try {
                    if (getRandomID(recipeJsonStr) == 0) {
                        finalID = 0;
                    } else {
                        finalID = getRandomID(recipeJsonStr);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                recipeJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            if (finalID != 0) {
                String detailURL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/" + String.valueOf(finalID) + "/information?includeNutrition=false";
                try {
                    URL url = new URL(detailURL);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("X-Mashape-Key", apiKey);
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoOutput(false);
                    Log.d("Response Code", String.valueOf(urlConnection.getResponseCode()));
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer stringBuffer = new StringBuffer();
                    if (inputStream == null) {
                        recipeJsonStr = null;
                    }
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line + "\n");
                    }

                    if (stringBuffer.length() == 0) {
                        recipeJsonStr = null;
                    }
                    recipeJsonStr = stringBuffer.toString();
                    Log.d("JSON", recipeJsonStr);
                } catch (IOException e) {
                    Log.e("PlaceholderFragment", "Error ", e);
                    recipeJsonStr = null;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (final IOException e) {
                            Log.e("PlaceholderFragment", "Error closing stream", e);
                        }
                    }
                }
                if (recipeJsonStr != null) {
                    try {
                        Recipe returnRecipe = getDetailFromJson(recipeJsonStr);
                        return returnRecipe;
                    } catch (JSONException e) {
                        Log.e("Async1", e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } return null;
    }

    @Override
    protected void onPostExecute(Recipe recipe) {
        super.onPostExecute(recipe);
        if (recipe != null){
            delegate.processRandomFinish(recipe);
        }
    }

    private int getRandomID(String RecipeJson) throws JSONException {

        final String RECIPES = "recipes";
        final String ID = "id";
        final String TITLE = "title";
        int finalID = 0;

        JSONObject fullRecipeJson = new JSONObject(RecipeJson);
        JSONArray recipeJsonArray = fullRecipeJson.getJSONArray(RECIPES);

        if (recipeJsonArray != null) {
            for (int i = 0; i < recipeJsonArray.length(); i++) {
                Log.d("rounds",String.valueOf(i));
                if (recipeJsonArray.getJSONObject(i).optString(TITLE) != "") {
                    finalID = recipeJsonArray.getJSONObject(i).getInt(ID);
                }
            }
        } return finalID;
    }

    private Recipe getDetailFromJson(String recipeJsonStr) throws JSONException {

        Recipe returnRecipe;
        int rID;
        String rTitle;
        String rInstructions;
        String rImage;
        int rReadyInMinutes;
        int rServings;
        Ingredient[] rIngredients;

        final String RECIPEID = "id";
        final String RECIPETITLE = "title";
        final String INSTRUCTIONS = "instructions";
        final String IMAGE = "image";
        final String READYINMINUTES = "readyInMinutes";
        final String SERVINGS = "servings";
        final String INGREDIENTS = "extendedIngredients";
        final String IID = "id";
        final String INAME = "name";
        final String IAMOUNT = "amount";
        final String IUNIT = "unit";
        final String ISHORTUNIT = "unitShort";
        final String ILONGUNIT = "unitLong";
        final String IORIGINALSTRING = "originalString";

        JSONObject fullRecipeJson = new JSONObject(recipeJsonStr);
        JSONArray ingredientsJsonArray = fullRecipeJson.getJSONArray(INGREDIENTS);

        rID = fullRecipeJson.getInt(RECIPEID);
        rTitle = fullRecipeJson.getString(RECIPETITLE);
        rInstructions = fullRecipeJson.getString(INSTRUCTIONS);
        rImage = fullRecipeJson.getString(IMAGE);
        rReadyInMinutes = fullRecipeJson.getInt(READYINMINUTES);
        rServings = fullRecipeJson.getInt(SERVINGS);

        rIngredients = new Ingredient[ingredientsJsonArray.length()];

        if (ingredientsJsonArray.length() != 0) {
            for (int i = 0; i < ingredientsJsonArray.length(); i++){
                int iID = ingredientsJsonArray.getJSONObject(i).getInt(IID);
                String iName = ingredientsJsonArray.getJSONObject(i).getString(INAME);
                double iAmount = ingredientsJsonArray.getJSONObject(i).getDouble(IAMOUNT);
                String iUnit = ingredientsJsonArray.getJSONObject(i).getString(IUNIT);
                String iShortUnit = ingredientsJsonArray.getJSONObject(i).getString(ISHORTUNIT);
                String iLongUnit = ingredientsJsonArray.getJSONObject(i).getString(ILONGUNIT);
                String iOriginalString = ingredientsJsonArray.getJSONObject(i).getString(IORIGINALSTRING);
                rIngredients[i] = new Ingredient(iID,iName,iAmount,iUnit,iShortUnit,iLongUnit,iOriginalString);
            }
        }
        returnRecipe = new Recipe(rID,rTitle,rInstructions,rReadyInMinutes,rServings,rIngredients,rImage);
        return returnRecipe;
    }
}
