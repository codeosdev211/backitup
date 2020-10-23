package com.jrm.backitup.Connections;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

public interface IAPI {
    void onSuccess(JSONObject response);
    void onError(VolleyError error);
    Map<String, String> setHeaders();
}
