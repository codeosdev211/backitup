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

/*
*  @Author Jayesh (codeos)
* I hate comments but this is for you to understand my strange code.
* My function names will mostly explain the code.
*/

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * The app creates/updates a shared preference when signing in to the account by the name "isLoggedIn".
        * This is a simple way to check if the user is signedin/ loggedin and redirect based on the value
        * "Yes" - redirects to Dashboard activity and
        * "No" - redirects to Login activity
        */
        if(new AppPref().localData(getApplicationContext(), 'G', "isLoggedIn", "").equals("Yes")) {
            redirect(Dashboard.class);
        }else{
            redirect(Login.class);
        }
    }

    private void redirect(Class<?> destination) {
        Intent goTo = new Intent(getApplicationContext(), destination);
        startActivity(goTo);
        finish();
    }
}