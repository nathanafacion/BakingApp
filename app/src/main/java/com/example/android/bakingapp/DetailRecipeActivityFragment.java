package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingapp.utils.Ingredient;
import com.example.android.bakingapp.utils.Recipe;
import com.example.android.bakingapp.utils.RecipeWidgetProvider;
import com.example.android.bakingapp.utils.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailRecipeActivityFragment  extends Fragment {

    private static Recipe recipe;

    public interface OnImageClickListerner{
        void onStepSelected(int position, ArrayList<Step> stepList);
    }

    OnImageClickListerner mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mCallback = (OnImageClickListerner)context;

        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement interface onImageClickListener");
        }
    }

    public DetailRecipeActivityFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentDetailItem = getActivity().getIntent();

        View rootView = inflater.inflate(R.layout.detail_fragment,container,false);
        if(intentDetailItem != null && intentDetailItem.hasExtra("recipe")) {
            recipe = intentDetailItem.getExtras().getParcelable("recipe");
        }

        String mensure, quantity, name;
        if (intentDetailItem != null) {

            ArrayList<Ingredient> ingredients = recipe.getIngredients();
            final ArrayList<Step> steps = recipe.getSteps();
            getActivity().setTitle(recipe.getRecipe_name());
            LinearLayout detailLayoutRecipe = rootView.findViewById(R.id.ll_ingredient);
            for (int i = 0; i < ingredients.size(); i++) {
                View reviewLayout = LayoutInflater.from(getContext()).inflate(R.layout.number_list_ingredients, detailLayoutRecipe, false);
                TextView detailView = reviewLayout.findViewById(R.id.tv_ingredient);
                name = ingredients.get(i).getIngredient();
                mensure = ingredients.get(i).getMeasure();
                quantity = String.valueOf(ingredients.get(i).getQuantity());
                detailView.setText(quantity + " " + mensure + " " + name);
                detailLayoutRecipe.addView(reviewLayout);

            }

            LinearLayout detailLayoutStep = rootView.findViewById(R.id.ll_step);
            String id, shortDescription;
            for (int i = 0; i < steps.size(); i++) {
                View reviewLayout = LayoutInflater.from(getContext()).inflate(R.layout.number_list_steps, detailLayoutStep, false);
                final TextView stepView = reviewLayout.findViewById(R.id.tv_step);
                id = String.valueOf(steps.get(i).getId());
                shortDescription = steps.get(i).getShortDescrition();
                stepView.setText(id + ") " + shortDescription + " ");
                ImageView thumbnail = reviewLayout.findViewById(R.id.step_thumbnail);
                if(steps.get(i).getThumbnailUrl() != null && steps.get(i).getThumbnailUrl().isEmpty()== false){
                    try {
                        Picasso.with(getContext()).load(steps.get(i).getThumbnailUrl()).into(thumbnail);
                    } catch (Exception e){
                        thumbnail.setVisibility(View.GONE);
                    }
                } else {
                    thumbnail.setVisibility(View.GONE);
                }
                detailLayoutStep.addView(reviewLayout);
                final int position = i;
                stepView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.onStepSelected(position, steps);
                    }
                });

            }

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
            int [] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getContext(), RecipeWidgetProvider.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.recipe_widget_layout);
            RecipeWidgetProvider.updateRecipeWidget(getContext(),appWidgetManager,recipe,appWidgetIds);

        }


        return rootView;
    }
}
