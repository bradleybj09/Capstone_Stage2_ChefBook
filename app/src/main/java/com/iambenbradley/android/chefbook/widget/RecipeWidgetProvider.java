package com.iambenbradley.android.chefbook.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.iambenbradley.android.chefbook.R;
import com.iambenbradley.android.chefbook.activities.MainActivity;

/**
 * Created by Ben on 1/19/2017.
 */

public class RecipeWidgetProvider extends AppWidgetProvider {

    public static final String LAUNCH_ACTION = "com.example.android.recipewidget.LAUNCH_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(LAUNCH_ACTION)) {
            Intent launchIntent = new Intent(context,MainActivity.class);
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchIntent.putExtra("launch", "Random");
            context.startActivity(launchIntent);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appwidgetid : appWidgetIds) {

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            Intent intent = new Intent(context, RecipeWidgetProvider.class);
            intent.setAction(LAUNCH_ACTION);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            remoteViews.setOnClickPendingIntent(R.id.widget_button, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
            super.onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

}
