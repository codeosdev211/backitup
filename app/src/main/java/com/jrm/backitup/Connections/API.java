package com.jrm.backitup.Connections;

import android.content.Context;
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

import java.util.Map;

public class API {

    public void callServer(Context context, int method,
                           String urlType, JSONArray requestData,
                           final IAPI apiInterface) {
        try {
            JSONObject data = null;
            if(data != null) {
                data = new JSONObject(new Gson().toJson(requestData));
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
                            return apiInterface.setHeaders();
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
