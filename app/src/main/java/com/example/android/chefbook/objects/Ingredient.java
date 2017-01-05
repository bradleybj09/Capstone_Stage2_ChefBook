package com.example.android.chefbook.objects;

/**
 * Created by Ben on 12/7/2016.
 */

public class Ingredient {
    private int ingredientID;
    private String name;
    private double amount;
    private String unit;
    private String unitShort;
    private String unitLong;
    private String originalString;
    private boolean isPurchased;

    public Ingredient(int ingredientID, String name, double amount, String unit, String unitShort, String unitLong, String originalString)
    {
        this.ingredientID = ingredientID;
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.unitShort = unitShort;
        this.unitLong = unitLong;
        this.originalString = originalString;
        isPurchased = false;
    }

    public int getIngredientID() {
        return ingredientID;
    }

    public double getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public String getOriginalString() {
        return originalString;
    }

    public String getUnit() {
        return unit;
    }

    public String getUnitLong() {
        return unitLong;
    }

    public String getUnitShort() {
        return unitShort;
    }

    public void toggleIngredient(){
        if (!isPurchased) {
            isPurchased = true;
        }
        else {
            isPurchased = false;
        }
    }
}
