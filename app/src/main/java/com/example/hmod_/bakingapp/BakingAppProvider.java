package com.example.hmod_.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.hmod_.bakingapp.NetWork.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppProvider extends AppWidgetProvider {
    private static RemoteViews views;
    private static Context mContext;
    private static final String TAG = "BakingAppProvider";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        views = new RemoteViews(context.getPackageName(), R.layout.baking_app_provider);
        mContext = context;

        Intent intentBack = new Intent(context, BakingAppProvider.class);
        Intent intentForId1 = new Intent(context, BakingAppProvider.class);
        Intent intentForId2 = new Intent(context, BakingAppProvider.class);
        Intent intentForId3 = new Intent(context, BakingAppProvider.class);
        Intent intentForId4 = new Intent(context, BakingAppProvider.class);

        intentBack.setAction("back");
        intentForId1.putExtra("id", 1);
        intentForId1.putExtra("name", "Nutella Pie");
        intentForId2.putExtra("id", 2);
        intentForId2.putExtra("name", "Brownies");
        intentForId3.putExtra("id", 3);
        intentForId3.putExtra("name", "Yellow Cake");
        intentForId4.putExtra("id", 4);
        intentForId4.putExtra("name", "Cheesecake");


        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 1, intentForId1, 0);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 2, intentForId2, 0);
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context, 3, intentForId3, 0);
        PendingIntent pendingIntent4 = PendingIntent.getBroadcast(context, 4, intentForId4, 0);
        PendingIntent pendingIntent5 = PendingIntent.getBroadcast(context, 5, intentBack, 0);


        views.setOnClickPendingIntent(R.id.nutella, pendingIntent1);
        views.setOnClickPendingIntent(R.id.brownies, pendingIntent2);
        views.setOnClickPendingIntent(R.id.yellow, pendingIntent3);
        views.setOnClickPendingIntent(R.id.cheesecake, pendingIntent4);
        views.setOnClickPendingIntent(R.id.back, pendingIntent5);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);


        if (intent.hasExtra("id")) {
            new DetailsAsyncTask().execute(intent.getIntExtra("id", 1));
        } else if (intent.getAction().equals("back")) {

            views.setViewVisibility(R.id.widget, View.VISIBLE);
            views.setTextViewText(R.id.details, " ");
            views.setViewVisibility(R.id.linear_details, View.INVISIBLE);

            AppWidgetManager.getInstance(mContext).updateAppWidget(
                    new ComponentName(mContext, BakingAppProvider.class), views
            );

        }

    }

    class DetailsAsyncTask extends AsyncTask<Integer, Void, JSONArray> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONArray doInBackground(Integer... integers) {
            if (integers != null)
                return NetworkUtils.getIngredients(integers[0]);
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);

            if (jsonArray != null && jsonArray.length() != 0) {

                try {

                    String details = "";
                    JSONObject jsonObject;
                    String ingredient, quantity, measure;

                    for (int i = 0; i < jsonArray.length(); i++) {

                        jsonObject = jsonArray.getJSONObject(i);
                        ingredient = jsonObject.getString("ingredient");
                        quantity = jsonObject.getString("quantity");
                        measure = jsonObject.getString("measure");
                        details += " * " + ingredient + " * " + quantity + " * " + measure + "\n";
                    }

                    Log.d(TAG, "onPostExecute: " + details);
                    views.setTextViewText(R.id.details, details);

                } catch (JSONException ex) {
                    views.setTextViewText(R.id.details, "Empty data please check your internet connection");
                }
            } else
                views.setTextViewText(R.id.details, "Empty data please check your internet connection");

            views.setViewVisibility(R.id.widget, View.INVISIBLE);
            views.setViewVisibility(R.id.linear_details, View.VISIBLE);

            AppWidgetManager.getInstance(mContext).updateAppWidget(
                    new ComponentName(mContext, BakingAppProvider.class), views
            );

        }
    }
}

