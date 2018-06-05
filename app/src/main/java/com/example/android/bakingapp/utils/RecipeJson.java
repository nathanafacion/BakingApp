package com.example.android.bakingapp.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeJson {

    private static final String TAG = RecipeJson.class.getSimpleName();

    public static ArrayList<Recipe> getRecipeFromJson(String JsonStr)
            throws JSONException {
        final String key_id = "id";
        final String key_recipe_name = "name";
        final String key_image = "image";
        final String key_servings = "servings";
        final String key_steps = "steps";
        final String key_ingredients = "ingredients";

        JSONArray recipeJsonAll = new JSONArray(JsonStr);
        JSONObject recipeJson;
        JSONArray  ingredientJson, stepJson;

        int recipe_id, recipe_servings;
        String recipe_image, recipe_name;
        ArrayList<Ingredient> recipe_ingredients;
        ArrayList<Step> recipe_steps;
        ArrayList<Recipe> allRecipes = new ArrayList<Recipe>();

        for (int i=0 ; i < recipeJsonAll.length(); i++) {
            recipeJson = recipeJsonAll.getJSONObject(i);
            recipe_id = recipeJson.getInt(key_id);
            recipe_servings = recipeJson.getInt(key_servings);
            recipe_image = recipeJson.getString(key_image);
            recipe_name = recipeJson.getString(key_recipe_name);
            ingredientJson = recipeJson.getJSONArray(key_ingredients);
            stepJson = recipeJson.getJSONArray(key_steps);

            recipe_ingredients = getIngredientsFromJson(ingredientJson);
            recipe_steps = getStepsFromJson(stepJson);
            Recipe newRecipe = new Recipe(recipe_id,recipe_name, recipe_ingredients, recipe_steps, recipe_servings, recipe_image);
            allRecipes.add(newRecipe);
        }



        return allRecipes;
}

    private static ArrayList<Step> getStepsFromJson(JSONArray stepJson) throws JSONException {
        String key_id = "id" ;
        String key_shortDescription = "shortDescription";
        String key_description = "description";
        String key_url = "videoURL";
        String key_thumbnailUrl = "thumbnailURL";

        int step_id;
        String step_shortDescrition;
        String step_description;
        String step_video_url;
        String step_thumbnailUrl;

        ArrayList<Step> allSteps = new ArrayList<Step>();
        JSONObject step;

        for (int i=0 ; i < stepJson.length(); i++) {
            step = stepJson.getJSONObject(i);
            step_id = step.getInt(key_id);
            step_shortDescrition = step.getString(key_shortDescription);
            step_description = step.getString(key_description);
            step_video_url = step.getString(key_url);
            step_thumbnailUrl = step.getString(key_thumbnailUrl);

            Step newStep = new Step(step_id, step_shortDescrition, step_description, step_video_url, step_thumbnailUrl);
            allSteps.add(newStep);
        }
        return allSteps;
    }

    private static ArrayList<Ingredient> getIngredientsFromJson(JSONArray ingredientJson) throws JSONException {
        String key_ingredient = "ingredient" ;
        String key_measure = "measure";
        String key_quantity = "quantity";

        String ingredient_ingredient;
        String ingredient_measure;
        int ingredient_quantity;

        ArrayList<Ingredient> allIngredients = new ArrayList<Ingredient>();
        JSONObject ingredient;

        for (int i=0 ; i < ingredientJson.length(); i++) {
            ingredient = ingredientJson.getJSONObject(i);

            ingredient_ingredient = ingredient.getString(key_ingredient);
            ingredient_measure = ingredient.getString(key_measure);
            ingredient_quantity = ingredient.getInt(key_quantity);

            Ingredient newIngredient = new Ingredient(ingredient_ingredient,ingredient_measure,ingredient_quantity);
            allIngredients.add(newIngredient);
        }
        return allIngredients;

    }


}
