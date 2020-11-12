package com.jrm.backitup.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.jrm.backitup.Local.AppPref;
import com.jrm.backitup.R;

import org.json.JSONObject;

/*
 *  @Author Jayesh (codeos)
 * I hate comments but this is for you to understand my strange code.
 * My function names will mostly explain the code.
 */
public class Groups extends AppCompatActivity {

    // currentUser will hold the values we stored in shared preference while signing in
    JSONObject currentUser = null;

    // layout that contains the search stuff
    ConstraintLayout searchPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups);
        try {
            currentUser = new JSONObject(new AppPref().localData(getApplicationContext(), 'G', "BU", ""));

            searchPanel = (ConstraintLayout) findViewById(R.id.grpSearchPanel);


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

    public void openSearch(View view) {
         searchPanel.setVisibility(View.VISIBLE);
    }

    public void closeSearch(View view) {
        searchPanel.setVisibility(View.GONE);
    }

}