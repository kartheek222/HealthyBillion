package com.kartheek.healthybillion.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Manager for the queue
 *
 * @author Kartheek
 */
public class RequestManager {

    /**
     * t{@link RequestQueue} containing all the requests which are send by our application.
     */
    private static RequestQueue mRequestQueue;

    /**
     * Nothing to see here.
     */
    private RequestManager() {
        // no instances
    }

    /**
     * @param context application context
     */
    public static void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Returns the request queue associated with our application.Parameter context should be at the application level for creating
     * the RequestQueue.
     *
     * @return {@link RequestQueue} associated with our application.
     * @throws IllegalStateException if the request queue is not initialized.
     */
    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("Not initialized");
        }
    }

    /**
     * Cancels all the pending requests in the request queue.
     */
    public static void cancelAllRequests() {
        // TODO Auto-generated method stub
        if (getRequestQueue() != null) {
            RequestManager.getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {

                @Override
                public boolean apply(Request<?> arg0) {
                    // return true for cancelling the requests.
                    return true;
                }
            });
        }
    }
}
