package Model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by HP on 6/10/2017.
 */

public class CheckConnectivity {
    Context ctx;

    public CheckConnectivity(Context context) {
        this.ctx = context;
    }

    public static boolean isNetworkAvaliable(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
            }
        }
        return false;
    }

}
