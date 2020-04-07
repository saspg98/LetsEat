package assignment.project.letseat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import assignment.project.letseat.Adpaters.MainRecyclerViewAdapter;
import assignment.project.letseat.Common.MealdbClient;
import assignment.project.letseat.Common.SharedPreference;
import assignment.project.letseat.Models.Meal;
import assignment.project.letseat.Models.Meals;
import assignment.project.letseat.NetworkingNCaching.Cache_Network;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MealDetailsActivity extends AppCompatActivity {

    private List<Meal> meals = new ArrayList<Meal>();
    private RecyclerView recyclerView;
    private MainRecyclerViewAdapter mAdapter;
    SharedPreference sharedPreference;
    private final String API_KEY = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        Meal meal = (Meal) args.getSerializable(getString(R.string.MEAL));
        String prevActivity = args.getString(getResources().getString(R.string.prev_activity));

        getSupportActionBar().setTitle(meal.getStrMeal());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.meal_recycler_view);

        mAdapter = new MainRecyclerViewAdapter(MealDetailsActivity.this,meals);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        sharedPreference = new SharedPreference();

        if(prevActivity.equals("Search Activity")){
            getMeal(meal.getStrMeal());
        }
        if(prevActivity.equals("Favourite Activity")){
            prepareMealData(meal);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void getMeal(String mealName) {

        MealdbClient mealdbClient = Cache_Network.getMealdbClient();

        Call<Meals> call = mealdbClient.getMeals(API_KEY,mealName);
        Log.w("URL", call.request().url().toString());

        final double reqTime = System.currentTimeMillis();
        call.enqueue(new Callback<Meals>() {

            @Override
            public void onResponse(Call<Meals> call, Response<Meals> response) {

                double mElapsedTime = System.currentTimeMillis() - reqTime;

                String source = "";

                if (response.raw().cacheResponse() != null) {
                    source = "Cache";                           //Notifies whether data is from cache or not
                    Log.e("Data From", "Cache");
                }

                if (response.raw().networkResponse() != null) {
                    source = "API";
                    Log.e("Data From", "API");          //Notifies whether data is from cache or not
                }

                //Displays the elapsed time between request and response in case cache is used or API call
                Toast.makeText(getBaseContext(), "Data from: " + source + "  Time(ms): " +
                        String.valueOf(mElapsedTime), Toast.LENGTH_SHORT).show();

                if (!response.isSuccessful()) {
                    Log.e("Failure", "Null value from API call");
                } else {
                    List<Meal> meals = (ArrayList<Meal>) response.body().getMeals();
                    prepareMealData(meals.get(0));
                    /*double elapsedTime= - response.raw().sentRequestAtMillis() +
                            response.raw().receivedResponseAtMillis();*/

                    // Log.e("Send request At:",String.valueOf(response.raw().sentRequestAtMillis()));
                    // Log.e("Elapsed time",String.valueOf(elapsedTime));
                    //  Log.e("Monitored elapsed time",String.valueOf(mElapsedTime));
                }
            }

            @Override
            public void onFailure(Call<Meals> call, Throwable t) {
                Log.e("Failure", t.toString());
                Log.e("Failure", "Milgai error");
                Toast.makeText(getBaseContext(), "Cannot connect to API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareMealData(Meal meal) {
        if(meal!=null) {
            if(checkFavoriteItem(meal)){
                meal.setIsFavourite(true);
            }else{
                meal.setIsFavourite(false);
            }
            meals.add(meal);
            mAdapter.notifyDataSetChanged();
        }else{
            Toast.makeText(getBaseContext(),"No Meal Available",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkFavoriteItem(Meal checkMeal) {
        boolean check = false;
        List<Meal> favorites = sharedPreference.getFavourites(MealDetailsActivity.this);
        if (favorites != null) {
            for (Meal meal : favorites) {
                if (meal.equals(checkMeal)) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

}
