package com.evergreen.apps.tourguideapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;

import com.android.example.tourguideapp.R;
import com.evergreen.apps.tourguideapp.models.Location;
import com.evergreen.apps.tourguideapp.providers.LocationProvider;
import com.evergreen.apps.tourguideapp.utils.Constants;


/**
 * Implementation of App Widget functionality.
 */
public class RecommendationWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Cursor cursor) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recommendation_widget);

        views.removeAllViews(R.id.widget_recommendations_container);

        if (cursor.getCount() != 0){

            while (cursor.moveToNext()) {

                String name = cursor.getString(cursor.getColumnIndexOrThrow(Location.COLUMN_NAME));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(Location.COLUMN_CITY));

                RemoteViews placeView = new RemoteViews(context.getPackageName(), R.layout.widget_list);

                placeView.setTextViewText(R.id.place_name_text_view,
                        name + " " + address);

                views.addView(R.id.widget_recommendations_container, placeView);
            }
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        new Thread(new Runnable() {
            @Override
            public void run() {

                Uri locationQueryUri = LocationProvider.URI_LOCATION;

                String selection = null;
                Cursor cursor = context.getContentResolver()
                        .query(locationQueryUri,
                                Constants.MAIN_LOCATION_PROJECTION,
                                selection,
                                Constants.MAIN_LOCATION_PROJECTION,
                                null);

                if (cursor != null) {

                    for (int appWidgetId : appWidgetIds) {
                        updateAppWidget(context, appWidgetManager, appWidgetId, cursor);
                    }

                    cursor.close();
                }
            }
        }).start();

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

