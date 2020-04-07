package assignment.project.letseat.NetworkingNCaching;

//Interface implemented in MainActivity to display message on GUI in case of no connection
//or Cache Unavailability(in case data stored in cache is more than 2 days)

public interface InternetConnectionListener {

    void onInternetUnavailable();

    void onCacheUnavailable();
}
