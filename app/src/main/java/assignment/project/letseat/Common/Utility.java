package assignment.project.letseat.Common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import assignment.project.letseat.Models.Meal;
import assignment.project.letseat.Models.Meals;
import assignment.project.letseat.NetworkingNCaching.Cache_Network;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utility {

    public static void setMealImage(ImageView mealImage, String strMealThumb) {
        if (strMealThumb != null) {
            Picasso.get().load(strMealThumb)
                    .fit()
                    .into(mealImage);
        }
    }
}
