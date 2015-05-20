package com.tomfin46.myworldiscomics.Service;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.tomfin46.myworldiscomics.DataModel.Enums.ResourceTypes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * Created by Tom on 11/03/2015.
 */
public class BackboneService {

    final static String TAG = "BackboneService";

    public static void Get(Context ctx, int id, ResourceTypes.ResourcesEnum resourceType, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = UrlConstructor.constructCv(resourceType, Integer.toString(id));
        JSONObject postParameter = null;
        MakeRequest(ctx, Request.Method.GET, url, postParameter, listener, errorListener);
    }

    public static void Get(Context ctx, int id, ResourceTypes.ResourcesEnum resourceType, String filter, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = UrlConstructor.constructCv(resourceType, Integer.toString(id), filter);
        JSONObject postParameter = null;
        MakeRequest(ctx, Request.Method.GET, url, postParameter, listener, errorListener);
    }

    public static void Post(Context ctx, String description, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = UrlConstructor.ConstructDesc(description);
        MakeRequest(ctx, Request.Method.POST, url, description, listener, errorListener);
    }

    public static void Post(Context ctx, String url, JSONObject postParameter, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        MakeRequest(ctx, Request.Method.POST, url, postParameter, listener, errorListener);
    }

    public static void Get(Context ctx, ResourceTypes.ResourcesEnum resourceType, String sort, String filter, int limit, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        String url = UrlConstructor.constructCv(resourceType, sort, filter, limit);
        MakeRequest(ctx, url, listener, errorListener);
    }

    public static void Search(Context ctx, String query, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        try {
            query = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = UrlConstructor.constructCv(ResourceTypes.ResourcesEnum.Search, query);
        MakeRequest(ctx, url, listener, errorListener);
    }

    private static void MakeRequest(Context ctx, int requestMethodType, String url, JSONObject postParameter, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestMethodType, url, postParameter, listener, errorListener);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConstants.TimeoutMs, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AddRequestToQueue(ctx, jsonObjectRequest);
    }

    private static void MakeRequest(Context ctx, int requestMethodType, String url, final String requestBody, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestMethodType, url, null, listener, errorListener) {

            @Override
            public byte[] getBody() {
                return requestBody.getBytes(Charset.forName("UTF-8"));
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConstants.TimeoutMs, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AddRequestToQueue(ctx, jsonObjectRequest);
    }

    private static void MakeRequest(Context ctx, String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, listener, errorListener);
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(ServiceConstants.SearchTimeoutMs, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AddRequestToQueue(ctx, jsonArrayRequest);
    }

    private static void AddRequestToQueue(Context ctx, JsonRequest request) {
        RequestQueueSingleton.getInstance(ctx).addToRequestQueue(request);
    }
}