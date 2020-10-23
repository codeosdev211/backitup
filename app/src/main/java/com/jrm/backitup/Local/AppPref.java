package com.jrm.backitup.Local;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPref {

    public String localData(Context context,char type, String localKey, String localData) {
        SharedPreferences preference = context.getSharedPreferences("AppData", Context.MODE_PRIVATE);
        if(type == 'S') {
            SharedPreferences.Editor editor = preference.edit();
            editor.putString(localKey, localData);
            editor.apply();
        }else if(type == 'G') {
            return preference.getString(localKey, "");
        }
        return "";
    }


}
