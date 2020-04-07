package assignment.project.letseat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import assignment.project.letseat.Adpaters.MainRecyclerViewAdapter;
import assignment.project.letseat.Common.MealdbClient;
import assignment.project.letseat.Common.SharedPreference;
import assignment.project.letseat.Models.Meal;
import assignment.project.letseat.Models.Meals;
import assignment.project.letseat.NetworkingNCaching.Cache_Network;
import assignment.project.letseat.NetworkingNCaching.InternetConnectionListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xdroid.toaster.Toaster;

public class MainActivity extends AppCompatActivity implements InternetConnectionListener {

    private List<Meal> meals = new ArrayList<Meal>();
    private RecyclerView recyclerView;
    private MainRecyclerViewAdapter mAdapter;
    private final String API_KEY = "1";
    SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Today's Meal");

        recyclerView = findViewById(R.id.main_recycler_view);

        mAdapter = new MainRecyclerViewAdapter(getBaseContext(),meals);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        Cache_Network.setInternetConnectionListener(this);

        getRandomMeal();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar,menu);

        MenuItem searchMenu = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.println(Log.DEBUG,"SearchView","Submit Query Successful. !");
                searchView.clearFocus();
                searchMenu.collapseActionView();
                if(query.isEmpty()){
                    Toast.makeText(getBaseContext(),"Please enter some item.",Toast.LENGTH_SHORT).show();
                }else {
                    getMeals(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.favourite){
            startNewActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startNewActivity() {
        Intent intent = new Intent(this, Favourites.class);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        updateMealData(meals.get(0));
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Cache_Network.removeInternetConnectionListener();
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
            Toast.makeText(getBaseContext(),"No Meal Available :(",Toast.LENGTH_SHORT).show();
        }
    }

    private void updateMealData(Meal meal){
        if(meal!=null) {
            if(checkFavoriteItem(meal)){
                meal.setIsFavourite(true);
            }else{
                meal.setIsFavourite(false);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private boolean checkFavoriteItem(Meal checkMeal) {

        sharedPreference = new SharedPreference();

        boolean check = false;
        List<Meal> favorites = sharedPreference.getFavourites(this);
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

    private void createNextActivity(List<Meal> meals) {

        if(meals!=null && meals.size()!=0) {
            Intent nextActivity = new Intent(this, MealDetailsActivity.class);
            Bundle args = new Bundle();
            args.putSerializable(this.getResources().getString(R.string.MEAL), (Serializable) meals.get(0));
            args.putString(getString(R.string.prev_activity),"Search Activity");
            nextActivity.putExtra("BUNDLE", args);
            startActivity(nextActivity);
        }else{
            Toast.makeText(this,"No such Meal Exist !",Toast.LENGTH_SHORT).show();
        }
    }

    private void getMeals(String mealName) {

        MealdbClient mealdbClient = Cache_Network.getMealdbClient();

        Call<Meals> call = mealdbClient.getMeals(API_KEY,mealName);

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
                    try {
                        Log.e("Success", "Meal recieved");
                        createNextActivity(meals);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "No such meal exists!", Toast.LENGTH_SHORT).show();
                    }
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

    private void getRandomMeal() {

        MealdbClient mealdbClient = Cache_Network.getMealdbClient();

        Call<Meals> call = mealdbClient.getMeals(API_KEY);
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

    @Override
    public void onInternetUnavailable() {
        Toaster.toast("No Internet Available!", Toast.LENGTH_SHORT);
        Log.e("OnInternetUnavailable", "No internet!");
    }

    @Override
    public void onCacheUnavailable() {
        Toaster.toast("No Cache Available!", Toast.LENGTH_SHORT);
        Log.e("OnCacheUnavailable", "No stored cache!");
    }
}
