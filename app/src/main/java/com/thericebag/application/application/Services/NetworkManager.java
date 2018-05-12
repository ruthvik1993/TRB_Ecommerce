package com.thericebag.application.application.Services;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.thericebag.application.application.utility.AppUtil;

import java.util.HashMap;

/**
 * Created by abhilash on 01-09-2016.
 */

public final class NetworkManager {

    private static final String TAG = NetworkManager.class.getSimpleName();

    private static final int TIMEOUT = 0;

    private static NetworkManager sNetworkManager;
    private final RequestQueue mRequestQueue;
    private Context mContext;

    private NetworkManager(Context context) {
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(mContext);
    }

    public static NetworkManager getInstance(Context context) {
        if (sNetworkManager == null) {
            synchronized (NetworkManager.class) {
                sNetworkManager = new NetworkManager(context);
            }
        }
        return sNetworkManager;
    }

    public void sendRequest(TheRiceBagRequest theRiceBagRequest) {
        if (AppUtil.isNetworkAvailable(mContext)) {
            if (TIMEOUT > 0) {
                theRiceBagRequest.setRetryPolicy(new DefaultRetryPolicy(
                        TIMEOUT,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            }
            mRequestQueue.add(theRiceBagRequest);
        } else {
            NetworkResponse errorResponse = new NetworkResponse(404, new byte[]{0}, new HashMap<String, String>(), false);
            theRiceBagRequest.deliverError(new VolleyError(errorResponse));
        }
    }

    public void cancelRequest(TheRiceBagRequest theRiceBagRequest) {
        if (theRiceBagRequest != null) {
            mRequestQueue.cancelAll(theRiceBagRequest.getTag());
        }
    }

}
