package assignment.project.letseat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import assignment.project.letseat.Adpaters.FavouritesRecyclerViewAdapter;
import assignment.project.letseat.Adpaters.MainRecyclerViewAdapter;
import assignment.project.letseat.Common.SharedPreference;
import assignment.project.letseat.Models.Meal;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Favourites extends AppCompatActivity {

    private List<Meal> favourites = new ArrayList<Meal>();
    private RecyclerView recyclerView;
    private FavouritesRecyclerViewAdapter mAdapter;
    SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        getSupportActionBar().setTitle("Favourites");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = findViewById(R.id.favourite_recycler_view);

        mAdapter = new FavouritesRecyclerViewAdapter(this,favourites);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        getFavourites();
    }

    @Override
    protected void onRestart() {
        getFavourites();
        super.onRestart();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void getFavourites() {

        if(!this.favourites.isEmpty()){
            for(int i=0;i<favourites.size();i++){
                favourites.remove(i);
                mAdapter.notifyItemRemoved(i);
                mAdapter.notifyItemRangeChanged(i,favourites.size());
            }
        }

        sharedPreference = new SharedPreference();

        List<Meal> favourites = sharedPreference.getFavourites(this);

        if (favourites == null) {
            Toast.makeText(this,this.getResources().getString(R.string.no_favorites_items)+" "
                    +getResources().getString(R.string.no_favorites_msg),Toast.LENGTH_SHORT).show();
        }else {
            if (favourites.size() == 0) {
                Toast.makeText(this, this.getResources().getString(R.string.no_favorites_items) + " "
                        + getResources().getString(R.string.no_favorites_msg), Toast.LENGTH_SHORT).show();
                return;
            }
            for (int i = 0; i < favourites.size(); i++) {
                favourites.get(i).setIsFavourite(true);
            }
            this.favourites.addAll(favourites);
        }

        mAdapter.notifyDataSetChanged();
    }
}
