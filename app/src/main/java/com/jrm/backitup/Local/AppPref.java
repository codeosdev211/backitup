package com.jrm.backitup.Local;

import android.content.Context;
import android.content.SharedPreferences;

/*
 *  @Author Jayesh (codeos)
 * I hate comments but this is for you to understand my strange code.
 * My function names will mostly explain the code.
 */
public class AppPref {

    /*
        This function handles the shared preference
        everything is string so its easy to handle small amount of data
        like json string for the user info
        and isLoggedIn thing
        S for Set
        G for Get, if your where like WHAT IS THIS TRASH!?
     */
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
