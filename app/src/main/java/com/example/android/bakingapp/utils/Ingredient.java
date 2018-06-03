package com.example.android.bakingapp.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable{

    private final String ingredient;
    private final String measure;
    private final int quantity;

    public Ingredient(String ingredint, String measure, int quantity) {
        this.ingredient = ingredint;
        this.measure = measure;
        this.quantity = quantity;
    }


    protected Ingredient(Parcel in) {
        ingredient = in.readString();
        measure = in.readString();
        quantity = in.readInt();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getIngredient() {
        return ingredient;
    }

    public String getMeasure() {
        return measure;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ingredient);
        dest.writeString(measure);
        dest.writeInt(quantity);
    }
}
