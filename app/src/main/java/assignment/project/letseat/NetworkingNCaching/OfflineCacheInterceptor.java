package assignment.project.letseat.NetworkingNCaching;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

//This is invoked if connection to API is not established. Hence interceptor access data stored in cache if
//it is less than 2 days old and if greater than 2 day then empty response is returned.
//Also informs user about 'No Internet' connectivity by displaying on GUI.

public abstract class OfflineCacheInterceptor implements Interceptor {

    public abstract boolean isInternetAvailable();  //Method to check Internet Connectivity
    public abstract void onInternetUnavailable();   //Method to display 'No Internet' message on GUI
    public abstract void onCacheUnavailable();      //Method to display 'No Cache' message on GUI

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        if ( !isInternetAvailable() )
        {
            onInternetUnavailable();

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxStale( 2, TimeUnit.DAYS )
                    .build();

            request = request.newBuilder()
                    .cacheControl( cacheControl )
                    .build();
        }

        Response response = chain.proceed( request );

        if(response.cacheResponse()==null){
            Log.e("From","App Interceptor");
            onCacheUnavailable();
        }

        return response;
    }
}
