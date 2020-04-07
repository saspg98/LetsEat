package assignment.project.letseat.NetworkingNCaching;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Response;

//This is invoked if connection to API is established and first checks whether data stored in cache
//is less than 1 min old or not if greater than 1 min then again makes request to API to get
//updated call.

public abstract class NetworkCacheInterceptor implements Interceptor {

    public abstract boolean isInternetAvailable();  //Method to check Internet Connectivity
    public abstract void onInternetUnavailable();   //Method to display 'No Internet' message on GUI
    public abstract void onCacheUnavailable();      //Method to display 'No Cache' message on GUI

    @Override
    public Response intercept(Chain chain) throws IOException {

        Response response = chain.proceed(chain.request());

        // re-write response header to force use of cache if cache stored is more than 2 minutes old
        CacheControl cacheControl = new CacheControl.Builder()
                .maxAge( 5, TimeUnit.MINUTES )
                .build();

        response =  response.newBuilder()
                .header( "Cache-Control", cacheControl.toString() )
                .build();

        if (response.cacheResponse() == null) {
            Log.e("From","Network Interceptor");
            onCacheUnavailable();
        }

        return response;
    }
}
