package com.example.android.chefbook.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ben on 12/7/2016.
 */

public class Ingredient implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    private Ingredient(Parcel parcel){
        this.ingredientID = parcel.readInt();
        this.name = parcel.readString();
        this.amount = parcel.readDouble();
        this.unit = parcel.readString();
        this.unitShort = parcel.readString();
        this.unitLong = parcel.readString();
        this.originalString = parcel.readString();
    }

    public Ingredient(int ingredientID, String name, String unit) {
        this.ingredientID = ingredientID;
        this.name = name;
        this.unit = unit;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ingredientID);
        dest.writeString(name);
        dest.writeDouble(amount);
        dest.writeString(unit);
        dest.writeString(unitShort);
        dest.writeString(unitLong);
        dest.writeString(originalString);
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

}
