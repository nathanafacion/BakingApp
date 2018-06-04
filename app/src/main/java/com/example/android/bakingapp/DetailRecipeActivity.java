package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
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

public class DetailRecipeActivity extends AppCompatActivity implements  DetailRecipeActivityFragment.OnImageClickListerner{

    private static final String TAG = DetailRecipeActivity.class.getSimpleName();
    static Recipe recipe;
    private boolean mTwoPane = false;
    private int stepPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);


        if(findViewById(R.id.detail_step_fragment) != null) {
            Intent intentDetailItem = getIntent();
            mTwoPane = true;
            if (intentDetailItem != null && intentDetailItem.hasExtra("recipe")) {
                recipe = intentDetailItem.getExtras().getParcelable("recipe");
                StepActivityFragment newFragment = new StepActivityFragment();
                newFragment.setArguments(recipe.getSteps().get(stepPosition),stepPosition);
                getSupportFragmentManager().beginTransaction().replace(R.id.detail_step_fragment, newFragment).commit();
            }
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("stepPosition",stepPosition);
    }

    @Override
    public void onStepSelected(int position, ArrayList<Step> stepList) {
        if(mTwoPane == false){
            Class destinationClass = StepActivity.class;
            Intent intentMovie = new Intent(this, destinationClass);
            intentMovie.putExtra("step",stepList );
            intentMovie.putExtra("position",position);
            startActivity(intentMovie);
        } else {
            stepPosition = position;
            StepActivityFragment newFragment = new StepActivityFragment();
            newFragment.setArguments(recipe.getSteps().get(position),position);
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_step_fragment, newFragment).commit();
        }
    }
}
