package com.jrm.backitup.Local;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

public class Query {
    public static final String createBG = "CREATE TABLE BG ( code TEXT, name TEXT, owner TEXT, userCount TEXT);";
    public static final String dropBG = "DROP TABLE BG;";

    Context context;
    DbHandler handler;

    public Query(Context context) {
        this.context = context;
        handler = new DbHandler(context);
    }


    public void writeGroup(JSONObject data) throws Exception{
        ContentValues values = new ContentValues();
        values.put("code", data.getString("code"));
        values.put("name", data.getString("name"));
        values.put("owner", data.getString("owner"));
        values.put("userCount", data.getString("userCount"));
        handler.insert("BG", values);
    }

    public JSONArray readGroups(String searchValue) throws Exception {
       return handler.select("select * from BG where name like '%"+ searchValue +"%' OR owner like '%"+ searchValue +"%';");
    }

    public void deleteGroups() {
        handler.truncate("BG");
    }

}
