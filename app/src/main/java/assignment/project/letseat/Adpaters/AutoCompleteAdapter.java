//package assignment.project.letseat.Adpaters;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Filter;
//import android.widget.Filterable;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import assignment.project.letseat.Common.MealdbClient;
//import assignment.project.letseat.Models.Meal;
//import assignment.project.letseat.Models.Meals;
//import assignment.project.letseat.NetworkingNCaching.Cache_Network;
//import assignment.project.letseat.R;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class AutoCompleteAdapter extends ArrayAdapter implements Filterable {
//
//    private ArrayList mMeal;
//    private String API_KEY = "1";
//
//    public AutoCompleteAdapter(@NonNull Context context, int resource) {
//        super(context, resource);
//        mMeal = new ArrayList<>();
//    }
//
//    @Override
//    public int getCount() {
//        return mMeal.size();
//    }
//
//    @Nullable
//    @Override
//    public Object getItem(int position) {
//        return mMeal.get(position);
//    }
//
//    @NonNull
//    @Override
//    public Filter getFilter() {
//        Filter filter = new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence charSequence) {
//                FilterResults filterResults = new FilterResults();
//                if(charSequence != null){
//                    try{
//                        String term = charSequence.toString();
//                        getMealList(term.charAt(0));
//                    }catch (Exception e){
//                        Log.d("Filtering","EXCEPTION "+e);
//                    }
//                    filterResults.values = mMeal;
//                    filterResults.count = mMeal.size();
//                }
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                if(filterResults != null && filterResults.count > 0){
//                    notifyDataSetChanged();
//                }else{
//                    notifyDataSetInvalidated();
//                }
//            }
//        };
//
//        return filter;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        LayoutInflater inflater = LayoutInflater.from(getContext());
//        View view = inflater.inflate(R.layout.autocomplete_layout,parent,false);
//
//        Meal meal = (Meal) mMeal.get(position);
//        TextView countryName = (TextView) view.findViewById(R.id.meal_query);
//        countryName.setText(meal.getStrMeal());
//
//        return view;
//    }
//
//    private void getMealList(char ch) {
//
//        MealdbClient mealdbClient = Cache_Network.getMealdbClient();
//
//        Call<Meals> call = mealdbClient.getMeals(API_KEY,ch);
//        Log.w("URL", call.request().url().toString());
//
////        final double reqTime = System.currentTimeMillis();
//        call.enqueue(new Callback<Meals>() {
//
//            @Override
//            public void onResponse(Call<Meals> call, Response<Meals> response) {
//
////                double mElapsedTime = System.currentTimeMillis() - reqTime;
////
////                String source = "";
////
////                if (response.raw().cacheResponse() != null) {
////                    source = "Cache";                           //Notifies whether data is from cache or not
////                    Log.e("Data From", "Cache");
////                }
////
////                if (response.raw().networkResponse() != null) {
////                    source = "API";
////                    Log.e("Data From", "API");          //Notifies whether data is from cache or not
////                }
//
//                //Displays the elapsed time between request and response in case cache is used or API call
////                Toast.makeText(getBaseContext(), "Data from: " + source + "  Time(ms): " +
////                        String.valueOf(mElapsedTime), Toast.LENGTH_SHORT).show();
//
//                if (!response.isSuccessful()) {
//                    Log.e("Failure", "Null value from API call");
//                } else {
//                    List<Meal> meals = (ArrayList<Meal>) response.body().getMeals();
//                    try {
//                        Log.e("Success", "Meal recieved");
//                        updateSuggestions(meals);
//                    } catch (Exception e) {
//                        e.printStackTrace();
////                        Toast.makeText(getBaseContext(), "No such meal exists!", Toast.LENGTH_SHORT).show();
//                    }
//                    /*double elapsedTime= - response.raw().sentRequestAtMillis() +
//                            response.raw().receivedResponseAtMillis();*/
//                    // Log.e("Send request At:",String.valueOf(response.raw().sentRequestAtMillis()));
//                    // Log.e("Elapsed time",String.valueOf(elapsedTime));
//                    //  Log.e("Monitored elapsed time",String.valueOf(mElapsedTime));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Meals> call, Throwable t) {
//                Log.e("Failure", t.toString());
//                Log.e("Failure", "Milgai error");
////                Toast.makeText(getBaseContext(), "Cannot connect to API", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void updateSuggestions(List<Meal> meals) {
//        mMeal = (ArrayList) meals;
//    }
//}
