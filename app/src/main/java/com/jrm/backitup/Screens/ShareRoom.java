package com.jrm.backitup.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jrm.backitup.Local.AppPref;
import com.jrm.backitup.R;

import org.json.JSONObject;

/*
*  @Author Jayesh (codeos)
*  I hate comments but this is for you to understand my strange code.
*  My function names will mostly explain the code.
*/
public class ShareRoom extends AppCompatActivity {

    // currentUser will hold the values we stored in shared preferences while signing in
    JSONObject currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_room);
        try {
            currentUser = new JSONObject(new AppPref().localData(getApplicationContext(), 'G', "BU", ""));

        }catch(Exception error) {
            toast("Could not load page", 1);
        }
    }
    // defaults
    private void redirect(Class<?> destination) {
        Intent goTo = new Intent(getApplicationContext(), destination);
        startActivity(goTo);
        finish();
    }

    private void toast(String message, int duration) {
        Toast.makeText(getApplicationContext(), message, duration).show();
    }

    // onclick
    public void redirectToGroups(View view) {
        redirect(Groups.class);
    }



}