package com.reactlibrary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class HttpModuleModule extends ReactContextBaseJavaModule {

    ReactApplicationContext _reactContext;

    public  String headers;

    @ReactMethod
    public void setHeaders(String headers){
        Log.d("HTTPModule ", "SetHeaders:" + headers);
        this.headers = headers;
    }

    @Override
    public String getName() {
        return "HTTPModule";
    }

    public HttpModuleModule(ReactApplicationContext context) {
        super(context);
        _reactContext = context;
    }

    // If headers/body are not needed - send them empty instead of null
    @ReactMethod
    public void makeGetRequest(final String url,final String headers, final String body, final Callback callback) {
        Log.d("HTTPModule", "In makeGetRequest: URL " + url + " Headers: " + headers + " body: " + body);
        ConnectivityManager cm = (ConnectivityManager) getReactApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            RequestQueue mRequestQueue = Volley.newRequestQueue(getReactApplicationContext());
            StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("HTTPModule", "Response for url " + url + ": " + response.toString());
                    callback.invoke(null, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("HTTPModule", "Error in getRequest: " + error);
                    NetworkResponse response = error.networkResponse;
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        Log.i("HTTPModule", "network error response: " + res);
                        callback.invoke(res);
                    } catch (UnsupportedEncodingException unsupportedEncodingException) {
                        Log.e("HTTPModule", " getCall UnsupportedEncodingException " + unsupportedEncodingException);
                        callback.invoke("Error in post call" + unsupportedEncodingException);
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Gson gson = new Gson();
                    Map<String, String> map = new HashMap<String, String>();
                    map = (Map<String, String>) gson.fromJson(headers, map.getClass());
                    Log.d("HTTPModule", "Headers: " + map.toString());
                    return map;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    Log.d("HTTPModule ", "Request body:" + body);
                    return body.getBytes();
                }
            };

            mStringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mRequestQueue.add(mStringRequest);
        } else {
            Log.w("HTTPModule", "No internet connection");
            callback.invoke("No internet connection", null);
        }
    }

    // If headers/body are not needed - send them empty instead of null
    @ReactMethod
    public void makePostRequest(final String url,final String headers, final String body, final Callback callback) {
        Log.d("HTTPModule", "In makePostRequest: URL " + url + " Headers: " + headers + " body: " + body);
        ConnectivityManager cm = (ConnectivityManager) getReactApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            RequestQueue mRequestQueue = Volley.newRequestQueue(getReactApplicationContext());
            StringRequest mStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("HTTPModule", "Response for url " + url + ": " + response.toString());
                    callback.invoke(null, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("HTTPModule", "Error in post Request: " + error);
                    NetworkResponse response = error.networkResponse;
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        Log.i("HTTPModule", "network error response: " + res);
                        callback.invoke(res);
                    } catch (UnsupportedEncodingException unsupportedEncodingException) {
                        Log.e("HTTPModule", " getCall UnsupportedEncodingException " + unsupportedEncodingException);
                        callback.invoke("Error in post call" + unsupportedEncodingException);
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Gson gson = new Gson();
                    Map<String, String> map = new HashMap<String, String>();
                    map = (Map<String, String>) gson.fromJson(headers, map.getClass());
                    Log.d("HTTPModule", "Headers: " + map.toString());
                    return map;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }

                @Override
                public byte[] getBody() {
                    Log.d("HTTPModule ", "Request body:" + body);
                    return body.getBytes();
                }
            };

//            mStringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mStringRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
            mRequestQueue.add(mStringRequest);
        } else {
            Log.w("HTTPModule", "No internet connection");
            callback.invoke("No internet connection", null);
        }
    }
}
