package assignment.project.letseat.NetworkingNCaching;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.squareup.picasso.BuildConfig;

import timber.log.Timber;


//To check whether the android application has access to the internet or is connected to internet or not.
public class AdeptAndroid extends Application {

    private static AdeptAndroid instance;

    @Override
    public void onCreate()
    {
        super.onCreate();

        instance = this;

        if (BuildConfig.DEBUG)
        {
            Timber.plant(new Timber.DebugTree());
        }

        Log.d("CREATION","Creating our Application");
    }

    // to make and sent instance of this class
    public static AdeptAndroid getInstance ()
    {
        return instance;
    }

    //called by isNetworkAvailable to check internet connectivity
    public static boolean hasNetwork ()
    {
        return instance.checkIfHasNetwork();
    }

    public boolean checkIfHasNetwork()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
