package com.jrm.backitup.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.jrm.backitup.Connections.API;
import com.jrm.backitup.Connections.IAPI;
import com.jrm.backitup.Local.AppPref;
import com.jrm.backitup.Models.BU;
import com.jrm.backitup.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*
 *  @Author Jayesh (codeos)
 * I hate comments but this is for you to understand my strange code.
 * My function names will mostly explain the code.
 */

public class Login extends AppCompatActivity {

    private EditText loginEmail, loginPassword;
    private final static String regexEmail = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    // this string below is the error message we will prepare when validating stuff.
    private String errMsg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        try {
            loginEmail = (EditText) findViewById(R.id.loginEmail);
            loginPassword = (EditText) findViewById(R.id.loginPassword);

        }catch(Exception error) {
            toast("Could not load page", 0);
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

    private void validate() {
       errMsg = "";
       if(loginEmail.getText().toString().length() == 0 ||
           !loginEmail.getText().toString().matches(regexEmail)) {
               errMsg += "Invalid Email Address\n";
       }
       if(loginPassword.getText().toString().length() < 6) {
           errMsg += "Password must have a minimum of 6 characters\n";
       }
    }


    // onclick
    public void goToRegister(View view) {
        redirect(Register.class);
    }

    public void recoverPassword(View view) {
        redirect(RecoverPassword.class);
    }

    public void signinUser(View view) {
        try {
            validate();
            if(errMsg.length() != 0) {
                toast(errMsg, 1);
            }else{
                BU user = new BU();
                user.Email(loginEmail.getText().toString().trim());
                user.Password(loginPassword.getText().toString().trim());
                JSONArray data = new JSONArray();
                data.put(user);
                callAPI(data);
            }
        }catch(Exception error) {
            toast("Could not connect to the server", 0);
        }
    }

    // api handling
    private void callAPI(JSONArray data) {
        new API().callServer(getApplicationContext(), 1, "login", data, new IAPI() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if(response.getString("status").equals("1")) {
                        toast(response.getString("msg"), 1);
                    }else{
                        /* creating/ updating shared preference for the user's information and the app loggedin status */
                        toast("Logging In...", 0);
                        AppPref localPref = new AppPref();
                        localPref.localData(getApplicationContext(), 'S', "BU", response.getJSONArray("data").getJSONObject(0).toString());
                        localPref.localData(getApplicationContext(), 'S', "isLoggedIn", "Yes");
                        redirect(Dashboard.class);
                    }
                }catch(Exception error) {
                    toast(error.getMessage(), 1);
                }
            }

            @Override
            public void onError(VolleyError error) {
                toast("Server Error" + error.getMessage(), 1);
            }
        });
    }

}