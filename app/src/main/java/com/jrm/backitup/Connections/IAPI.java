package com.jrm.backitup.Connections;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

/*
 *  @Author Jayesh (codeos)
 * I hate comments but this is for you to understand my strange code.
 * My function names will mostly explain the code.
 */
public interface IAPI {
    void onSuccess(JSONObject response);
    void onError(VolleyError error);
    /* where is the setHeaders function? dont pass values through headers
        its just too much
     */
}
