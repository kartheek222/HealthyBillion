package com.kartheek.healthybillion;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import java.util.Map;

/**
 * Sends a request to the server and handles the received responses, errors from the server.
 *
 * @author Kartheek, Krishna
 */
public interface OnStringResponseListener {

    /**
     * Invoked when a response is received from the server.
     *
     * @param response  String response from the server.
     * @param requestId Id of the request.
     */
    void onResponse(String response, int requestId);

    /**
     * Invoked when a error response is received from the server.
     *
     * @param errorResponse Received error by sending the request.
     * @param requestId     Id of the request.
     */
    void onErrorResponse(VolleyError errorResponse, int requestId);

    /**
     * Invoked on a background thread before calling the onResponse, used to parse the received response in the background thread.
     *
     * @param response  An instance of {@link NetworkResponse} containing the received response and other data.
     * @param requestId Id of the request.
     */
    void parseNetworkResponse(NetworkResponse response, int requestId);

    /**
     * Returns the parameter for corresponding the request with the given id.
     *
     * @param requestId Id of the request.
     * @return {@link Map} of String containing the parameter for the request.
     */
    Map<String, String> getParams(int requestId);
}
