package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.utils.Recipe;

import java.util.ArrayList;
import java.util.List;

public class StepActivity extends AppCompatActivity {

    private static List<Recipe> recipeList = new ArrayList<Recipe>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_pan);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_step_detail);

    }

}
