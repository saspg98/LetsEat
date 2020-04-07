package assignment.project.letseat.Common;

import assignment.project.letseat.Models.Meals;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

//Interface that will be used to make request/response to mealdb API.
public interface MealdbClient {

    //Use to make request to mealdb API if searched is made using meal name.
    @GET("api/json/v1/{key}/search.php")
    Call<Meals> getMeals(@Path("key") String API_KEY, @Query("s") String mealName);

    @GET("api/json/v1/{key}/random.php")
    @Headers("Cache-Control: no-cache")
    Call<Meals> getMeals(@Path("key") String API_KEY);

    @GET("api/json/v1/{key}/search.php")
    Call<Meals> getMeals(@Path("key") String API_KEY, @Query("f") char firstChar);

}
