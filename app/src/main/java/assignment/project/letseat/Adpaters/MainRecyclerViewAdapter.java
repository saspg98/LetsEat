package assignment.project.letseat.Adpaters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import assignment.project.letseat.Common.SharedPreference;
import assignment.project.letseat.Common.Utility;
import assignment.project.letseat.Models.Meal;
import assignment.project.letseat.R;


public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MyViewHolder> {

    Context context;
    private List<Meal> meals;
    SharedPreference sharedPreference;
    boolean flag = true;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout mealImageHolder;
        public LinearLayout mealInfo;

        public MyViewHolder(View view) {
            super(view);
            mealImageHolder = view.findViewById(R.id.meal_image_holder);
            mealInfo = view.findViewById(R.id.meal_info);
        }
    }

    public MainRecyclerViewAdapter(Context context,List<Meal> meals) {
        this.context = context;
        this.meals = meals;
        sharedPreference = new SharedPreference();
    }

    @NonNull
    @Override
    public MainRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecyclerViewAdapter.MyViewHolder holder, int position) {
        final Meal meal = meals.get(position);

        TextView mealName = holder.mealImageHolder.findViewById(R.id.meal_name);
        mealName.setText(meal.getStrMeal());

        ImageView mealImage = holder.mealImageHolder.findViewById(R.id.meal_image);
        Utility.setMealImage(mealImage, meal.getStrMealThumb());

        ImageButton youtube = holder.mealImageHolder.findViewById(R.id.youtube);
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (meal.getStrYoutube() != null) {
                    Uri uri = Uri.parse(meal.getStrYoutube());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    view.getContext().startActivity(intent);
                }
            }
        });

        ImageButton favourite = holder.mealImageHolder.findViewById(R.id.favourite);
        if(meal.getIsFavourite()){
            favourite.setImageResource(R.drawable.red_heart);
        }else{
            favourite.setImageResource(R.drawable.grey_heart);
        }
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(meal.getIsFavourite()){
                    meal.setIsFavourite(false);
                    favourite.setImageResource(R.drawable.grey_heart);
                    sharedPreference.removeFavorite(view.getContext(),meal);
                    Toast.makeText(view.getContext(),context.getResources().getString(R.string.remove_favourite),Toast.LENGTH_SHORT).show();
                }else{
                    meal.setIsFavourite(true);
                    favourite.setImageResource(R.drawable.red_heart);
                    sharedPreference.addFavorite(view.getContext(),meal);
                    Toast.makeText(view.getContext(),view.getContext().getResources().getString(R.string.add_favourite),Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(flag) {
            flag = false;

            if (meal.getStrCategory() != null) {
                View category = LayoutInflater.from(holder.mealInfo.getContext()).inflate(R.layout.detail_card, holder.mealInfo, false);
                TextView detailCategory = category.findViewById(R.id.detail);
                detailCategory.setText("Category");
                TextView descriptionCategory = category.findViewById(R.id.description);
                descriptionCategory.setText(meal.getStrCategory());
                holder.mealInfo.addView(category);
            }

            if (meal.getStrInstructions() != null) {
                View instruction = LayoutInflater.from(holder.mealInfo.getContext()).inflate(R.layout.detail_card, holder.mealInfo, false);
                TextView detailInstruction = instruction.findViewById(R.id.detail);
                detailInstruction.setText("Instructions");
                TextView descriptionInstruction = instruction.findViewById(R.id.description);
                descriptionInstruction.setText(meal.getStrInstructions());
                holder.mealInfo.addView(instruction);
            }

            String ingredients = getIngredients(meal);
            if (ingredients != null) {
                View ingredientsCard = LayoutInflater.from(holder.mealInfo.getContext()).inflate(R.layout.detail_card, holder.mealInfo, false);
                TextView detailIngredient = ingredientsCard.findViewById(R.id.detail);
                detailIngredient.setText("Ingredients");
                TextView descriptionIngredients = ingredientsCard.findViewById(R.id.description);
                descriptionIngredients.setText(ingredients);
                holder.mealInfo.addView(ingredientsCard);
            }
        }
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    private String getIngredients(Meal meal) {
        StringBuilder ingredients = new StringBuilder();
        if (!isNullOrEmpty(meal.getStrIngredient1())) {
            ingredients.append(meal.getStrIngredient1());
            if (!isNullOrEmpty(meal.getStrMeasure1())) {
                ingredients.append(" : ").append(meal.getStrMeasure1()).append("\n\n");
            } else {
                ingredients.append("\n\n");
            }
        }
        if (!isNullOrEmpty(meal.getStrIngredient2())) {
            ingredients.append(meal.getStrIngredient2());
            if (!isNullOrEmpty(meal.getStrMeasure2())) {
                ingredients.append(" : ").append(meal.getStrMeasure2()).append("\n\n");
            } else {
                ingredients.append("\n\n");
            }
        }
        if (!isNullOrEmpty(meal.getStrIngredient3())) {
            ingredients.append(meal.getStrIngredient3());
            if (!isNullOrEmpty(meal.getStrMeasure3())) {
                ingredients.append(" : ").append(meal.getStrMeasure3()).append("\n\n");
            } else {
                ingredients.append("\n\n");
            }
        }
        if (!isNullOrEmpty(meal.getStrIngredient4())) {
            ingredients.append(meal.getStrIngredient4());
            if (!isNullOrEmpty(meal.getStrMeasure4())) {
                ingredients.append(" : ").append(meal.getStrMeasure4()).append("\n\n");
            } else {
                ingredients.append("\n\n");
            }
        }
        if (!isNullOrEmpty(meal.getStrIngredient5())) {
            ingredients.append(meal.getStrIngredient5());
            if (!isNullOrEmpty(meal.getStrMeasure5())) {
                ingredients.append(" : ").append(meal.getStrMeasure5()).append("\n\n");
            } else {
                ingredients.append("\n\n");
            }
        }
        if (!isNullOrEmpty(meal.getStrIngredient6())) {
            ingredients.append(meal.getStrIngredient6());
            if (!isNullOrEmpty(meal.getStrMeasure6())) {
                ingredients.append(" : ").append(meal.getStrMeasure6()).append("\n\n");
            } else {
                ingredients.append("\n\n");
            }
        }
        if (!isNullOrEmpty(meal.getStrIngredient7())) {
            ingredients.append(meal.getStrIngredient7());
            if (!isNullOrEmpty(meal.getStrMeasure7())) {
                ingredients.append(" : ").append(meal.getStrMeasure7()).append("\n\n");
            } else {
                ingredients.append("\n\n");
            }
        }
        if (!isNullOrEmpty(meal.getStrIngredient8())) {
            ingredients.append(meal.getStrIngredient8());
            if (!isNullOrEmpty(meal.getStrMeasure8())) {
                ingredients.append(" : ").append(meal.getStrMeasure8()).append("\n\n");
            } else {
                ingredients.append("\n\n");
            }
        }
        if (!isNullOrEmpty(meal.getStrIngredient9())) {
            ingredients.append(meal.getStrIngredient9());
            if (!isNullOrEmpty(meal.getStrMeasure9())) {
                ingredients.append(" : ").append(meal.getStrMeasure9()).append("\n\n");
            } else {
                ingredients.append("\n\n");
            }
        }
        if (!isNullOrEmpty(meal.getStrIngredient10())) {
            ingredients.append(meal.getStrIngredient10());
            if (!isNullOrEmpty(meal.getStrMeasure10())) {
                ingredients.append(" : ").append(meal.getStrMeasure10()).append("\n\n");
            } else {
                ingredients.append("\n\n");
            }
        }
        if (!isNullOrEmpty(meal.getStrIngredient11())) {
            ingredients.append(meal.getStrIngredient11());
            if (!isNullOrEmpty(meal.getStrMeasure11())) {
                ingredients.append(" : ").append(meal.getStrMeasure11()).append("\n\n");
            } else {
                ingredients.append("\n\n");
            }
        }
        if (!isNullOrEmpty(meal.getStrIngredient12())) {
            ingredients.append(meal.getStrIngredient12());
            if (!isNullOrEmpty(meal.getStrMeasure12())) {
                ingredients.append(" : ").append(meal.getStrMeasure12()).append("\n\n");
            } else {
                ingredients.append("\n\n");
            }
        }
        if (!isNullOrEmpty(meal.getStrIngredient13())) {
            ingredients.append(meal.getStrIngredient13());
            if (!isNullOrEmpty(meal.getStrMeasure13())) {
                ingredients.append(" : ").append(meal.getStrMeasure13()).append("\n\n");
            } else {
                ingredients.append("\n\n");
            }
        }
        if (!isNullOrEmpty(meal.getStrIngredient14())) {
            ingredients.append(meal.getStrIngredient14());
            if (!isNullOrEmpty(meal.getStrMeasure14())) {
                ingredients.append(" : ").append(meal.getStrMeasure14()).append("\n\n");
            } else {
                ingredients.append("\n\n");
            }
        }
        if (!isNullOrEmpty(meal.getStrIngredient15())) {
            ingredients.append(meal.getStrIngredient15());
            if (!isNullOrEmpty(meal.getStrMeasure15())) {
                ingredients.append(" : ").append(meal.getStrMeasure15()).append("\n\n");
            } else {
                ingredients.append("\n\n");
            }
        }
        if (!isNullOrEmpty(meal.getStrIngredient16())) {
            ingredients.append(meal.getStrIngredient16());
            if (!isNullOrEmpty(meal.getStrMeasure16())) {
                ingredients.append(" : ").append(meal.getStrMeasure16()).append("\n\n");
            } else {
                ingredients.append("\n\n");
            }
        }
        if (!isNullOrEmpty(meal.getStrIngredient17())) {
            ingredients.append(meal.getStrIngredient17());
            if (!isNullOrEmpty(meal.getStrMeasure17())) {
                ingredients.append(" : ").append(meal.getStrMeasure17()).append("\n\n");
            } else {
                ingredients.append("\n\n");
            }
        }
        if (!isNullOrEmpty(meal.getStrIngredient18())) {
            ingredients.append(meal.getStrIngredient18());
            if (!isNullOrEmpty(meal.getStrMeasure18())) {
                ingredients.append(" : ").append(meal.getStrMeasure18()).append("\n\n");
            } else {
                ingredients.append("\n\n");
            }
        }
        if (!isNullOrEmpty(meal.getStrIngredient19())) {
            ingredients.append(meal.getStrIngredient19());
            if (!isNullOrEmpty(meal.getStrMeasure19())) {
                ingredients.append(" : ").append(meal.getStrMeasure19()).append("\n\n");
            } else {
                ingredients.append("\n\n");
            }
        }
        if (!isNullOrEmpty(meal.getStrIngredient20())) {
            ingredients.append(meal.getStrIngredient20());
            if (!isNullOrEmpty(meal.getStrMeasure20())) {
                ingredients.append(" : ").append(meal.getStrMeasure20()).append("\n\n");
            } else {
                ingredients.append("\n\n");
            }
        }
        return ingredients.toString();
    }

    private boolean isNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
    }
}
