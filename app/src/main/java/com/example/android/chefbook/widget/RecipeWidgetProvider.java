package com.example.android.chefbook.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.example.android.chefbook.R;
import com.example.android.chefbook.activities.MainActivity;
import com.example.android.chefbook.objects.Recipe;
import com.example.android.chefbook.utilities.FetchRandomRecipe;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ben on 1/19/2017.
 */

public class RecipeWidgetProvider extends AppWidgetProvider implements FetchRandomRecipe.AsyncRandomResponse {

    public static final String LAUNCH_ACTION = "com.example.android.recipewidget.LAUNCH_ACTION";
    public static final String UPDATE = "com.example.android.recipewidget.UPDATE_ACTION";
    String recipeTitle;
    String imagePath;
    Recipe recipe;
    Context mContext;
    AppWidgetManager mAppWidgetManager;
    int[] mAppWidgetIds;

    @Override
    public void processRandomFinish(Recipe output) {
        this.recipe = output;
        recipeTitle = output.getTitle();
        imagePath = output.getRecipeImageURL();

        for (int i = 0; i < mAppWidgetIds.length; i++) {
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_layout);
            remoteViews.setTextViewText(R.id.widget_textview, recipeTitle);
            Intent launchIntent = new Intent(mContext, MainActivity.class);
            launchIntent.putExtra("full_recipe",recipe);
            remoteViews.setOnClickFillInIntent(R.id.widget_button, launchIntent);
            mAppWidgetManager.updateAppWidget(mAppWidgetIds[i], remoteViews);
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        FetchRandomRecipe fetchRandomRecipe = new FetchRandomRecipe(this);
        fetchRandomRecipe.execute();

        mContext = context;
        mAppWidgetManager = appWidgetManager;
        mAppWidgetIds = appWidgetIds;

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}
