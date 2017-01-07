package com.example.android.chefbook.utilities;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.chefbook.database.MyRecipesContract;
import com.example.android.chefbook.objects.Recipe;
import com.example.android.chefbook.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Ben on 12/17/2016.
 */

public class MyRecipeAdapter extends CursorAdapter {

    public MyRecipeAdapter(Context context, Cursor cursor, int flags){
        super(context,cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.gridview_child, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView)view.findViewById(R.id.gridview_recipe_image);
        TextView textView = (TextView)view.findViewById(R.id.gridview_recipe_title);
        String imageURL = cursor.getString(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.COLUMN_RECIPE_IMAGE));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.COLUMN_RECIPE_TITLE));
        int recipeID = cursor.getInt(cursor.getColumnIndexOrThrow(MyRecipesContract.TableMyRecipes.COLUMN_RECIPE_ID));
        Picasso.with(context).load(imageURL).into(imageView);
        textView.setText(title);
    }
}
