package com.example.android.bakingapp;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.android.bakingapp.adapter.RecipeAdapter;
import com.example.android.bakingapp.utils.NetworkUtils;
import com.example.android.bakingapp.utils.Recipe;

import java.util.ArrayList;
import java.util.List;


public class MainActivityFragment extends Fragment {

    private static final String TAG = MainActivityFragment.class.getSimpleName();
    private static RecipeAdapter recipeAdapter;
    public static  GridView gridView;
    private static List<Recipe> recipeList = new ArrayList<Recipe>();
    public static TextView emptyFavoriteView;
    private static Parcelable state;
    private RecyclerView mRecyclerView;

    public MainActivityFragment() {
        if (recipeList == null || recipeList.size() == 0) {
            loadRecipeData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main, container, false);

        mRecyclerView = rootView.findViewById(R.id.recyclerview_recipes);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(false);

        recipeAdapter = new RecipeAdapter(getActivity(), recipeList);
        mRecyclerView.setAdapter(recipeAdapter);

        return rootView;
    }

    public static void loadRecipeData() {
        //showMoviesDataView();
        new FetchRecipeTask().execute();
    }

    public static class FetchRecipeTask extends AsyncTask<String, Void, List<Recipe>> {
        private final String TAG = MainActivityFragment.FetchRecipeTask.class.getSimpleName();


        protected void onPostExecute(List<Recipe> recipeData) {

            for(Recipe r: recipeData){
                Log.i("AAA",r.getRecipe_name());
            }
            Log.v(TAG,"onPostExecute: " +  recipeData);
            if (recipeData != null && recipeData.size()> 0)
                recipeAdapter.setRecipeData(recipeData);
            else {
                gridView.setVisibility(View.GONE);
                emptyFavoriteView.setVisibility(View.VISIBLE);
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected List<Recipe> doInBackground(String... params) {
            try {
                List<Recipe> jsonAllRecipes;
                recipeList.clear();
                jsonAllRecipes = NetworkUtils.getRecipesList();
                recipeList.addAll(jsonAllRecipes);
                return jsonAllRecipes;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }
}