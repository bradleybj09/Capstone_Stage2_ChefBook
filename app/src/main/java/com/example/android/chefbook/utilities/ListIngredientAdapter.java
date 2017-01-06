package com.example.android.chefbook.utilities;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.chefbook.R;
import com.example.android.chefbook.database.MyRecipesContract;

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
        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.shopping_list_ingredient);
        TextView amountView = (TextView)view.findViewById(R.id.shopping_list_ingredient_amount);
        TextView unitView = (TextView)view.findViewById(R.id.shopping_list_ingredient_measurement);
        TextView nameView = (TextView)view.findViewById(R.id.shopping_list_ingredient_name);
        int ingredientID = cursor.getInt(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_ID));
        Double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_AMOUNT));
        String unit = cursor.getString(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_UNIT));
        String unitLong = cursor.getString(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_UNIT_LONG));
        String unitShort = cursor.getString(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_UNIT_SHORT));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_NAME));
        if(amount>1){
            unitView.setText(unitLong);
        } else {
            unitView.setText(unitShort);
        }
        amountView.setText(String.valueOf(amount));
        nameView.setText(name);
        linearLayout.setTag(ingredientID);
    }
}
