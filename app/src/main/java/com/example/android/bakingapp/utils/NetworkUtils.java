package com.example.android.bakingapp.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.android.bakingapp.utils.Recipe;
import com.example.android.bakingapp.utils.RecipeJson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String UrlBase = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";


    public static List<Recipe> getRecipesList() {
        Uri buildUri = Uri.parse(UrlBase);

        List<Recipe> jsonAllRecipes = null;
        try {
            URL url = new URL(buildUri.toString());
            Log.v(TAG, "getRecipesList URL: " + url);
            String jsonRecipeResponse = NetworkUtils.getResponseFromHttpURL(url);
            jsonAllRecipes = RecipeJson.getRecipeFromJson(jsonRecipeResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonAllRecipes;
    }

    public static String getResponseFromHttpURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        Log.v(TAG, "Connection: " + urlConnection.getResponseCode());
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            Log.v(TAG, "Has Input: " + hasInput);
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }



}
