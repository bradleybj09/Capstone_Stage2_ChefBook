package com.iambenbradley.android.chefbook.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ben on 12/7/2016.
 */

public class Recipe implements Parcelable {
    private int recipeID;
    private String instructions;
    private String title;
    private int readyinMinutes;
    private String imageURL;
    private int servings;
    private Ingredient[] ingredients;

    public Recipe(int recipeID, String title, String instructions, int readyTime, int servings, Ingredient[] ingredients, String imageURL){
        this.recipeID = recipeID;
        this.title = title;
        this.imageURL = imageURL;
        this.instructions = instructions;
        this.readyinMinutes = readyTime;
        this.servings = servings;
        this.ingredients = ingredients;
    }

    public Recipe(int recipeID, String title, String imageURL) {
        this.recipeID = recipeID;
        this.title = title;
        this.imageURL = imageURL;
    }

    public ClassLoader getClassLoader(){
        return getClass().getClassLoader();
    }

    private Recipe(Parcel parcel) {
        this.recipeID = parcel.readInt();
        this.title = parcel.readString();
        this.imageURL = parcel.readString();
        this.instructions = parcel.readString();
        this.readyinMinutes = parcel.readInt();
        this.servings = parcel.readInt();
        this.ingredients = parcel.createTypedArray(Ingredient.CREATOR);
    }

    public int getRecipeID() {
        return recipeID;
    }

    public String getTitle() {
        return title;
    }

    public String getRecipeImageURL() {
        return imageURL;
    }

    public String getInstructions() {
        return instructions;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public int getReadyinMinutes(){
        return readyinMinutes;
    }

    public int getServings() {
        return servings;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(recipeID);
        parcel.writeString(title);
        parcel.writeString(imageURL);
        parcel.writeString(instructions);
        parcel.writeInt(readyinMinutes);
        parcel.writeInt(servings);
        parcel.writeTypedArray(ingredients,0);
    }
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel parcel) {
            return new Recipe(parcel);
        }

        @Override
        public Recipe[] newArray(int i) {
            return new Recipe[i];
        }

    };
}
