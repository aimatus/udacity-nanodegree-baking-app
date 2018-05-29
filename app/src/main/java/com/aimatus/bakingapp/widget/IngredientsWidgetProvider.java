package com.aimatus.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.aimatus.bakingapp.R;
import com.aimatus.bakingapp.model.Ingredient;
import com.aimatus.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Baking App Project
 * Udacity Associate Android Developer Fast Track Nanodegree Program
 * October 2017
 *
 * @author Abraham Matus
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

    static final String RECIPE = "recipe";
    static List<Ingredient> ingredients = new ArrayList<>();
    static String text;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);
        views.setTextViewText(R.id.tv_recipe_title_widget, text);

        Intent intent = new Intent(context, IngredientsWidgetService.class);
        views.setRemoteAdapter(R.id.lv_ingredients_widget, intent);

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

    /**
     * Based on https://stackoverflow.com/questions/28750082/how-can-i-update-a-widget-only-when-i-press-a-button-in-an-activity-from-within
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.hasExtra(RECIPE)) {
            Recipe recipe = (Recipe) intent.getSerializableExtra(RECIPE);
            text = recipe.getName();
            ingredients = recipe.getIngredients();
        } else {
            text = context.getString(R.string.no_recipe_selected);
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), IngredientsWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_ingredients_widget);
        if (appWidgetIds != null && appWidgetIds.length > 0) {
            onUpdate(context, appWidgetManager, appWidgetIds);
        }

        super.onReceive(context, intent);
    }
}

