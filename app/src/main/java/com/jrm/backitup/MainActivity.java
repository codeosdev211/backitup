package com.jrm.backitup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jrm.backitup.Local.AppPref;
import com.jrm.backitup.Screens.Dashboard;
import com.jrm.backitup.Screens.Login;

public class MainActivity extends AppCompatActivity {

    LinearLayoutCompat hiddenPanel;
    boolean isOpen = false;
    Animation bottomDown, bottomUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if(new AppPref().localData(getApplicationContext(), 'G', "isLoggedIn", "").equals("Yes")) {
//            redirect(Dashboard.class);
//        }else{
//            redirect(Login.class);
//        }
        hiddenPanel = findViewById(R.id.hiddenPanel);
        bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
        bottomUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
    }

    public void animate(View view) {
        if(isOpen) {
            hiddenPanel.startAnimation(bottomDown);
            hiddenPanel.setVisibility(View.GONE);
            isOpen = false;
        }else{
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);
            isOpen = true;
        }
    }

    private void redirect(Class<?> destination) {
        Intent goTo = new Intent(getApplicationContext(), destination);
        startActivity(goTo);
        finish();
    }
}