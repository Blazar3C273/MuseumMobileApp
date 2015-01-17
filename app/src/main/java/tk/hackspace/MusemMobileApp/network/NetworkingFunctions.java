package tk.hackspace.MusemMobileApp.network;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.Date;

import tk.hackspace.MusemMobileApp.R;

/**
 * Created by Tolik on 13.12.2014.
 */
public class NetworkingFunctions {
    private static final String TAG = NetworkingFunctions.class.getSimpleName();
    private static final long VALID_PING_TIME_MILSEC = 10000;
    private static NetworkingFunctions instance;
    private Context context;
    private boolean isServerAvailable = false;
    private AsyncHttpClient asyncHttpClient;
    private Date lastPing;

    public static NetworkingFunctions getInstance() {
        return instance;
    }

    private NetworkingFunctions(Context context) {
        this.context = context;
        asyncHttpClient = new AsyncHttpClient();
    }

    public static NetworkingFunctions initNetworkingFunctions(Context context) {
        NetworkingFunctions.instance = new NetworkingFunctions(context);
        instance.isServerAvailable();
        return instance;
    }

    public boolean isServerAvailable() {
        //TODO check server state
        AsyncHttpClient client = new AsyncHttpClient();
        ResponseHandlerInterface responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                setIsServerAvalible(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                setIsServerAvalible(false);
                Log.e(TAG, "Network error. Status code:" + statusCode + " Response string:" + responseString);
            }
        };

        long pastPingTime = VALID_PING_TIME_MILSEC;
        if (lastPing != null) {
            pastPingTime = new Date().getTime() - lastPing.getTime();
        }

        if (lastPing == null || pastPingTime >= VALID_PING_TIME_MILSEC) {
            client.get(context, context.getString(R.string.server_uri), responseHandler);
            lastPing = new Date();
        }
        return isServerAvailable;
    }


    public void setIsServerAvalible(boolean isServerAvalible) {

        this.isServerAvailable = isServerAvalible;
    }

    public AsyncHttpClient getAsyncHttpClient() {
        return asyncHttpClient;
    }


}
