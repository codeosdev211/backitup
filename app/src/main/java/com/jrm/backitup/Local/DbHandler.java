package com.jrm.backitup.Local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONObject;

public class DbHandler extends SQLiteOpenHelper {

    Context context;

    public DbHandler(Context context) {
        super(context, "bui.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Query.createBG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old, int recent) {
        db.execSQL(Query.dropBG);
        onCreate(db);
    }

    public void insert(String table, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(table, null, values);
    }

    public void truncate(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, null, null);
    }

    public JSONArray select(String query) throws Exception {
        JSONArray groups = new JSONArray();
        JSONObject eachGrp = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor == null) {
            return groups;
        }
        cursor.moveToFirst();
        while(cursor.moveToNext()) {
            eachGrp = new JSONObject();
            for(int col = 0; col < cursor.getColumnCount(); col++) {
                eachGrp.put(cursor.getColumnName(col), cursor.getString(col));
            }
            groups.put(eachGrp);
        }
        cursor.close();
        return groups;
    }

}
