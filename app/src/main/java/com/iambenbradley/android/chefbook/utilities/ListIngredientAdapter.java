package com.iambenbradley.android.chefbook.utilities;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iambenbradley.android.chefbook.R;
import com.iambenbradley.android.chefbook.database.MyRecipesContract;
import com.iambenbradley.android.chefbook.objects.Ingredient;

/**
 * Created by Ben on 1/5/2017.
 */

public class ListIngredientAdapter extends CursorAdapter {

    public ListIngredientAdapter(Context context, Cursor cursor, int flags){
        super(context,cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.shopping_list_ingredient_fragment,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.shopping_list_ingredient);
        TextView textView = (TextView)view.findViewById(R.id.shopping_list_ingredient_item);
        int ingredientID = cursor.getInt(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_ID));
        double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_AMOUNT));
        String unit = cursor.getString(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_UNIT));
        String unitLong = cursor.getString(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_UNIT_LONG));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_NAME));
        String finalUnit;
        if(amount>1){
            finalUnit = unitLong;
        } else {
            finalUnit = unit;
        }
        String finalAmount;
        if (amount == (long)amount) {
            finalAmount = String.format("%d",(long)amount);
        } else {
            finalAmount = String.format("%s",amount);
        }

        textView.setText(finalAmount + " " + finalUnit + " " + name);
        relativeLayout.setTag(new Ingredient(ingredientID, name, unit));
    }
}
