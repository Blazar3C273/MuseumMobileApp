package tk.hackspace.MusemMobileApp.network;

import android.content.Context;

import tk.hackspace.MusemMobileApp.R;

/**
 * Created by Tolik on 14.12.2014.
 */
public class URLFactory {

    public static String getItemURL(String itemID, Context context) {
        String dbName = context.getString(R.string.db_name);
        String serverURL = context.getString(R.string.server_uri);
        return serverURL + dbName + "/" + itemID;
    }

    public static String getImageURL(String itemID, String pictureFileName, Context context) {
        return getItemURL(itemID, context) + "/" + pictureFileName;
    }

    public static String getVideoURL(String itemID, String filename, Context context) {
        return getItemURL(itemID, context) + "/" + filename;
    }
}
