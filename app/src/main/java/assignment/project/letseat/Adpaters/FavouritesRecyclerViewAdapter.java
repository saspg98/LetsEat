package assignment.project.letseat.Adpaters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import assignment.project.letseat.Common.SharedPreference;
import assignment.project.letseat.Common.Utility;
import assignment.project.letseat.MealDetailsActivity;
import assignment.project.letseat.Models.Meal;
import assignment.project.letseat.R;

public class FavouritesRecyclerViewAdapter extends RecyclerView.Adapter<FavouritesRecyclerViewAdapter.MyViewHolder>{

    List<Meal> favourites;
    Context context;
    SharedPreference sharedPreference;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mealName;
        public ImageView mealImage;
        public ImageButton favouriteToggle;

        public MyViewHolder(View view) {
            super(view);
            mealImage = view.findViewById(R.id.meal_image);
            mealName = view.findViewById(R.id.meal_name);
            favouriteToggle = view.findViewById(R.id.favourite);
        }
    }

    public FavouritesRecyclerViewAdapter(Context context,List<Meal> favourites) {
        this.context = context;
        this.favourites = favourites;
        sharedPreference = new SharedPreference();
    }

    @NonNull
    @Override
    public FavouritesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_brief_layout, parent, false);
        return new FavouritesRecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesRecyclerViewAdapter.MyViewHolder holder, int position) {
        Meal meal = favourites.get(position);

        holder.mealName.setText(meal.getStrMeal());
        holder.mealName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(meal,v.getContext());
            }
        });

        Utility.setMealImage(holder.mealImage,meal.getStrMealThumb());
        holder.mealImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(meal,v.getContext());
            }
        });

        holder.favouriteToggle.setImageResource(R.drawable.red_heart);
        holder.favouriteToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                meal.setIsFavourite(false);
                holder.favouriteToggle.setImageResource(R.drawable.grey_heart);
                sharedPreference.removeFavorite(view.getContext(),meal);
                favourites.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,favourites.size());
                notifyDataSetChanged();
                Toast.makeText(view.getContext(),context.getResources().getString(R.string.remove_favourite),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }

    private void startNewActivity(Meal meal,Context context) {
        Intent intent = new Intent(context, MealDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(context.getString(R.string.MEAL), (Serializable) meal);
        bundle.putString(context.getResources().getString(R.string.prev_activity),"Favourite Activity");
        intent.putExtra("BUNDLE",bundle);
        context.startActivity(intent);
    }
}
