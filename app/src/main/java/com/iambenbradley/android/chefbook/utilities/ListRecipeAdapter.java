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

/**
 * Created by Ben on 1/5/2017.
 */

public class ListRecipeAdapter extends CursorAdapter {

    public ListRecipeAdapter(Context context, Cursor cursor,int flags){
        super(context,cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.shopping_list_recipe_fragment,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        RelativeLayout relativeLayout = (RelativeLayout)view.findViewById(R.id.shopping_list_recipe);
        TextView textView = (TextView)view.findViewById(R.id.shopping_list_recipe_title);
        int recipeID = cursor.getInt(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.LIST_RECIPE_ID));
        String recipeName = cursor.getString(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.LIST_RECIPE_NAME));
        textView.setText(recipeName);
        relativeLayout.setTag(recipeID);
    }
}
