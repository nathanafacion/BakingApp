package com.example.android.bakingapp.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.bakingapp.utils.Ingredient;
import com.example.android.bakingapp.utils.Step;

import java.util.ArrayList;

public class Recipe  implements Parcelable{
    private final int id ;
    private final String recipe_name;
    private final int servings;
    private final String image;
    private final ArrayList<Ingredient> ingredients;
    private final ArrayList<Step> steps;


    public Recipe(int id, String recipe_name, ArrayList<Ingredient> ingredients, ArrayList<Step> steps, int servings, String image) {
        this.id = id;
        this.recipe_name = recipe_name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public String getImage() {
        return image;
    }

    public int getServing() {
        return servings;
    }

    protected Recipe(Parcel in) {
        id = in.readInt();
        recipe_name = in.readString();
        servings = in.readInt();
        image = in.readString();
        ingredients = in.readArrayList(Ingredient.class.getClassLoader());
        steps = in.readArrayList(Step.class.getClassLoader());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(recipe_name);
        dest.writeInt(servings);
        dest.writeString(image);
        dest.writeList(ingredients);
        dest.writeList(steps);
    }
    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
