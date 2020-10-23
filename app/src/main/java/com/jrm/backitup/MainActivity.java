package com.jrm.backitup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.jrm.backitup.Screens.Login;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent goTo = new Intent(getApplicationContext(), Login.class);
        startActivity(goTo);
        finish();

    }
}