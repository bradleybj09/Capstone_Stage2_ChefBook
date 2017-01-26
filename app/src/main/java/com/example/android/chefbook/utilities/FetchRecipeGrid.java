package com.example.android.chefbook.utilities;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.chefbook.objects.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ben on 12/11/2016.
 */

public class FetchRecipeGrid extends AsyncTask<String, Void, ArrayList<Recipe>> {

    public interface AsyncResponse {
        void processFinish(ArrayList<Recipe> output);
    }

    public AsyncResponse delegate = null;

    public FetchRecipeGrid(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected ArrayList<Recipe> doInBackground(String... strings) {
        Log.d("doInBackground","Initiated");
        String apiKey = "yMn7M1DywmmshjLnVrGx90sD2ESIp1XfB2ijsnfU7kDaPhYGLb";
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        String recipeJsonStr;
        String currentURL;
        String query = "";
        try {
            query = java.net.URLEncoder.encode(strings[0], "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("Query Encoding","Failed");
        }
        if (strings.length == 0) {
            currentURL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/random?limitLicense=false&number=24";
        }
        else {
            currentURL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/search?limitLicense=false&number=24&offset=0&query=" + query;
        }

        try {
            URL url = new URL(currentURL);
            Log.d("URL", currentURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("X-Mashape-Key",apiKey);
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(false);
            Log.d("Response Code",String.valueOf(urlConnection.getResponseCode()));
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
            Log.d("JSON",recipeJsonStr);
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
                if (strings.length == 0) {
                    ArrayList<Recipe> finalRecipes = getRecipeFromJson(recipeJsonStr, false);
                    for (int i = 0; i < finalRecipes.size(); i++) {
                        Log.d("Recipe " + String.valueOf(i), String.valueOf(finalRecipes.get(i).getRecipeID()));
                    }
                    return finalRecipes;
                } else {
                    ArrayList<Recipe> finalRecipes = getRecipeFromJson(recipeJsonStr, true);
                    for (int i = 0; i < finalRecipes.size(); i++) {
                        Log.d("Recipe " + String.valueOf(i),String.valueOf(finalRecipes.get(i).getRecipeID()));
                    }
                    return finalRecipes;
                }
            } catch (JSONException e) {
                Log.e("Async1",e.getMessage());
                e.printStackTrace();
            }
        } return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Recipe> recipes) {
        super.onPostExecute(recipes);
        delegate.processFinish(recipes);
    }

    private ArrayList<Recipe> getRecipeFromJson(String RecipeJson, boolean Targeted) throws JSONException {

        final String RESULTS = "results";
        final String RECIPES = "recipes";
        final String ID = "id";
        final String TITLE = "title";
        final String IMAGEURL = "image";
        ArrayList<Recipe> returnRecipes;
        JSONArray recipeJsonArray = null;

        JSONObject fullRecipeJson = new JSONObject(RecipeJson);
        if (Targeted) {
            recipeJsonArray = fullRecipeJson.getJSONArray(RESULTS);
        }
        else {
            recipeJsonArray = fullRecipeJson.getJSONArray(RECIPES);
        }
        returnRecipes = new ArrayList<Recipe>();

        if (recipeJsonArray != null) {
            for (int i = 0; i < recipeJsonArray.length(); i++) {
                Log.d("rounds",String.valueOf(i));
                if (recipeJsonArray.getJSONObject(i).optString(TITLE) != "") {
                    int recipeID = recipeJsonArray.getJSONObject(i).getInt(ID);
                    String recipeTitle = recipeJsonArray.getJSONObject(i).getString(TITLE);
                    String recipeImageURL = recipeJsonArray.getJSONObject(i).getString(IMAGEURL);
                    if (Targeted) {
                        recipeImageURL = "https://spoonacular.com/recipeImages/" + recipeImageURL;
                    }
                    returnRecipes.add(new Recipe(recipeID, recipeTitle, recipeImageURL));
                }
            }
        }
        return returnRecipes;
    }
}
