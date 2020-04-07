package assignment.project.letseat.NetworkingNCaching;

import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.util.concurrent.TimeUnit;

import assignment.project.letseat.Common.MealdbClient;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Used for enabling and interacting with cache
//Used to build http client required for sending and receiving request and response respectively
//'Retrofit' is the REST client used for implementing all cache and network facility.

public class Cache_Network {

    public static final String BASE_URL = "https://www.themealdb.com/";
    public static final int DISK_CACHE_SIZE = (15 * 1024 * 1024); // 15 MB cache allotted for app
    private static MealdbClient mealdbClient;
    private static InternetConnectionListener mInternetConnectionListener;

    public static void  setInternetConnectionListener(InternetConnectionListener listener) {
        mInternetConnectionListener = listener;
        Log.d("Removed","Listener is removed because activity is paused.");
    }

    public static void removeInternetConnectionListener() {
        mInternetConnectionListener = null;
        Log.d("Removed","Listener is removed because activity is paused.");
    }

    //get Mealdb client for making request to API
    public static MealdbClient getMealdbClient() {
        if (mealdbClient == null) {
            mealdbClient = provideRetrofit(BASE_URL).create(MealdbClient.class);  // call to get Retrofit object
        }
        return mealdbClient;
    }

    //To build retrofit object using retrofit builder
    //Here we are adding override http client
    private static Retrofit provideRetrofit(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    //Check for network read and connect timeout in 60 sec
    //Also enables cache storage in the application
    //Interceptors added through this to check internet and cache availability
    private static OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();

        Log.e("Building",okhttpClientBuilder.toString());

        okhttpClientBuilder.connectTimeout(60, TimeUnit.SECONDS);
        okhttpClientBuilder.readTimeout(60, TimeUnit.SECONDS);

        okhttpClientBuilder.addInterceptor(new OfflineCacheInterceptor() {
            @Override
            public boolean isInternetAvailable() {
                return AdeptAndroid.hasNetwork();
            }

            @Override
            public void onInternetUnavailable() {
                if (mInternetConnectionListener != null) {
                    mInternetConnectionListener.onInternetUnavailable();
                }
            }

            @Override
            public void onCacheUnavailable() {
                if (mInternetConnectionListener != null) {
                    mInternetConnectionListener.onCacheUnavailable();
                }
            }
        });

        okhttpClientBuilder.addNetworkInterceptor(new NetworkCacheInterceptor(){

            @Override
            public boolean isInternetAvailable() {
                return AdeptAndroid.hasNetwork();
            }

            @Override
            public void onInternetUnavailable() {
                if (mInternetConnectionListener != null) {
                    mInternetConnectionListener.onInternetUnavailable();
                }
            }

            @Override
            public void onCacheUnavailable() {
                if (mInternetConnectionListener != null) {
                    mInternetConnectionListener.onCacheUnavailable();
                }
            }
        });

        okhttpClientBuilder.cache(provideCache());

        return okhttpClientBuilder.build();
    }

    //Declares cache size and directory of the cached data
    private static Cache provideCache ()
    {
        Cache cache = null;
        try
        {
            cache = new Cache( new File( AdeptAndroid.getInstance().getCacheDir(), "http-cache" ),
                    DISK_CACHE_SIZE );
        }
        catch (Exception e)
        {
            Log.d( "Error", "Could not create Cache!" );
        }
        return cache;
    }
}
