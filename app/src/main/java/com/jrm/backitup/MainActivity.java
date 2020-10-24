package com.jrm.backitup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.jrm.backitup.Local.AppPref;
import com.jrm.backitup.Screens.Dashboard;
import com.jrm.backitup.Screens.Login;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(new AppPref().localData(getApplicationContext(), 'G', "isLoggedIn", "").equals("No")) {
            redirect(Login.class);
        }else{
            redirect(Dashboard.class);
        }
    }

    private void redirect(Class<?> destination) {
        Intent goTo = new Intent(getApplicationContext(), destination);
        startActivity(goTo);
        finish();
    }
}