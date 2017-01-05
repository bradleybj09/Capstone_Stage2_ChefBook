package com.example.android.chefbook.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.chefbook.objects.Recipe;
import com.example.android.chefbook.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ben on 12/17/2016.
 */

public class FetchedRecipeAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Recipe> recipes;

    public FetchedRecipeAdapter(Context context, ArrayList<Recipe> recipes){
        mContext = context;
        this.recipes = recipes;
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = ((Activity) mContext).getLayoutInflater();
            view = layoutInflater.inflate(R.layout.gridview_child, viewGroup, false);
        }
        SquareImageView imageView = (SquareImageView)view.findViewById(R.id.gridview_recipe_image);
        TextView textView = (TextView)view.findViewById(R.id.gridview_recipe_title);
        Picasso.with(mContext).load(recipes.get(i).getRecipeImageURL()).into(imageView);
        textView.setText(recipes.get(i).getTitle());
        return view;
    }
}
