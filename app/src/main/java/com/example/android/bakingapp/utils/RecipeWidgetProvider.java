package com.example.android.bakingapp.utils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.bakingapp.DetailRecipeActivity;
import com.example.android.bakingapp.R;

public class RecipeWidgetProvider extends AppWidgetProvider {

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, Recipe recipe, int appWidgetId){
        RemoteViews rv;
        rv = getIngredientListRemoteView(context, recipe);
        appWidgetManager.updateAppWidget(appWidgetId,rv);
    }

    public static void updateRecipeWidget(Context context, AppWidgetManager appWidgetManager, Recipe recipe, int [] appWidgetId) {
        for (int id : appWidgetId){
            updateAppWidget(context,appWidgetManager,recipe,id);
        }
    }

    private static RemoteViews getIngredientListRemoteView(Context context, Recipe recipe) {
        Intent intent = new Intent(context, DetailRecipeActivity.class);
        intent.putExtra("recipe", recipe);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews recipeView = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        StringBuilder ingredients = new StringBuilder();
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredients.append(" ").append(ingredient.getQuantity()).append(" ").append(ingredient.getMeasure()).append(" ").append(ingredient.getIngredient()).append("\n");

        }

        recipeView.setTextViewText(R.id.recipe_list_widget, ingredients.toString());
        recipeView.setTextViewText(R.id.tv_title, recipe.getRecipe_name());
        recipeView.setOnClickPendingIntent(R.id.recipe_list_widget, pendingIntent);

        return recipeView;
    }
}


