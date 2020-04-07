package assignment.project.letseat.Common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.gson.Gson;

import assignment.project.letseat.Models.Meal;

public class SharedPreference {

    public static final String PREFS_NAME = "MEAL_APP";
    public static final String FAVOURITES = "Meal_Favorite";

    public SharedPreference() {
        super();
    }

    // These four methods are used for maintaining favourites.
    public void saveFavourites(Context context, List<Meal> favourites) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavourites = gson.toJson(favourites);

        editor.putString(FAVOURITES, jsonFavourites);

        editor.commit();
    }

    public void addFavorite(Context context, Meal meal) {
        List<Meal> favourites = getFavourites(context);
        if (favourites == null)
            favourites = new ArrayList<Meal>();
        favourites.add(meal);
        saveFavourites(context, favourites);
    }

    public void removeFavorite(Context context, Meal meal) {
        ArrayList<Meal> favourites = getFavourites(context);
        if (favourites != null) {
            for(int i=0;i<favourites.size();i++){
                if(favourites.get(i).getIdMeal().equals(meal.getIdMeal())){
                    favourites.remove(i);
                    break;
                }
            }
            saveFavourites(context, favourites);
        }
    }

    public ArrayList<Meal> getFavourites(Context context) {
        SharedPreferences settings;
        List<Meal> favourites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVOURITES)) {
            String jsonFavourites = settings.getString(FAVOURITES, null);
            Gson gson = new Gson();
            Meal[] favoriteItems = gson.fromJson(jsonFavourites,
                    Meal[].class);

            favourites = Arrays.asList(favoriteItems);
            favourites = new ArrayList<Meal>(favourites);
        } else
            return null;

        return (ArrayList<Meal>) favourites;
    }
}
