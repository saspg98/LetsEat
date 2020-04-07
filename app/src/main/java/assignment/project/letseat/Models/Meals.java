package assignment.project.letseat.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Meals {

    @SerializedName("meals")
    ArrayList<Meal> meals;

    public ArrayList<Meal> getMeals() {
        return meals;
    }

    public void setMeals(ArrayList<Meal> meals) {
        this.meals = meals;
    }


}
