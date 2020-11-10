package com.jrm.backitup.Connections;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*
 *  @Author Jayesh (codeos)
 * I hate comments but this is for you to understand my strange code.
 * My function names will mostly explain the code.
 */
public class API {

    /*
    * The HTTP requests take place HERE.
    * why int for method where you expect Request.Method.POST OR
    * Request.Method.GET, etc?
    * its base type is int. keeps it clean.
    *
    * seen the new API().callServer(....) calls right?
    * with the interface IAPI we dont need to write this whole function coded below everytime.
    * The library used is Volley, i know "Have you tried RetroFit?" or something that sounds similar idk.?!
    * I haven't , I will maybe.
    *
    *
    * anyway,
    * the final JSON request body will be like
    * {
    *   "values": [
    *              .........
    *              .........
    *             ]
    * }
    */

    public void callServer(Context context, int method,
                           String urlType, JSONArray requestData,
                           final IAPI apiInterface) {
        try {
            JSONObject data = null;
            if(requestData != null) {
                data = new JSONObject(new Gson().toJson(requestData));
                Log.d("request", data.toString());
            }
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JsonObjectRequest request = new JsonObjectRequest(
                    method, new AppURL().getURL(urlType), data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            apiInterface.onSuccess(response);
                        }
                    }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                apiInterface.onError(error);
                            }
                    }){
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> headers = new HashMap();
                            headers.put("Content-Type", "application/json;utf-8;");
                            headers.put("Accept", "application/json");
                            return headers;
                        }
                };

            request.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(request);
        }catch(Exception error) {
            Toast.makeText(context.getApplicationContext(),
                    "Could not queue or send request",
                    Toast.LENGTH_SHORT).show();
        }
    }






}
