package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingapp.utils.Ingredient;
import com.example.android.bakingapp.utils.Recipe;
import com.example.android.bakingapp.utils.RecipeWidgetProvider;
import com.example.android.bakingapp.utils.Step;

import java.util.ArrayList;

public class DetailRecipeActivity extends AppCompatActivity {

    private static final String TAG = DetailRecipeActivity.class.getSimpleName();
    static Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);
        Intent intentDetailItem = getIntent();

        if(intentDetailItem != null && intentDetailItem.hasExtra("recipe")) {
            recipe = intentDetailItem.getExtras().getParcelable("recipe");
        }

        String mensure, quantity, name;
        if (intentDetailItem != null) {

            ArrayList<Ingredient> ingredients = recipe.getIngredients();
            final ArrayList<Step> steps = recipe.getSteps();
            setTitle(recipe.getRecipe_name());
            LinearLayout detailLayoutRecipe = findViewById(R.id.ll_ingredient);
            for (int i = 0; i < ingredients.size(); i++) {
                View reviewLayout = LayoutInflater.from(this).inflate(R.layout.number_list_ingredients, detailLayoutRecipe, false);
                TextView detailView = reviewLayout.findViewById(R.id.tv_ingredient);
                name = ingredients.get(i).getIngredient();
                mensure = ingredients.get(i).getMeasure();
                quantity = String.valueOf(ingredients.get(i).getQuantity());
                detailView.setText(quantity + " " + mensure + " " + name);
                detailLayoutRecipe.addView(reviewLayout);

            }

            LinearLayout detailLayoutStep = findViewById(R.id.ll_step);
            String id, shortDescription;
            for (int i = 0; i < steps.size(); i++) {
                View reviewLayout = LayoutInflater.from(this).inflate(R.layout.number_list_steps, detailLayoutStep, false);
                TextView stepView = reviewLayout.findViewById(R.id.tv_step);
                id = String.valueOf(steps.get(i).getId());
                shortDescription = steps.get(i).getShortDescrition();
                stepView.setText(id + ") " + shortDescription + " ");
                detailLayoutStep.addView(reviewLayout);
                final int position = i;
                stepView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Class destinationClass = StepActivity.class;
                        Intent intentMovie = new Intent(v.getContext(), destinationClass);
                        intentMovie.putExtra("step", steps);
                        intentMovie.putExtra("position",position);
                        intentMovie.putExtra("recipe",recipe);
                        v.getContext().startActivity(intentMovie);
                    }
                });

            }

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int [] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.recipe_widget_layout);
            RecipeWidgetProvider.updateRecipeWidget(this,appWidgetManager,recipe,appWidgetIds);

        }

    }

}
