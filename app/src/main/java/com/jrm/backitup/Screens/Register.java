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
import com.jrm.backitup.Models.BU;
import com.jrm.backitup.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    EditText regFirstName, regLastName, regEmail, regPassword;
    String errMsg = "";
    private final static String regexEmail = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        try {
            regFirstName = (EditText) findViewById(R.id.registerFirstName);
            regLastName = (EditText) findViewById(R.id.registerLastName);
            regEmail = (EditText) findViewById(R.id.registerEmail);
            regPassword = (EditText) findViewById(R.id.registerPassword);

        }catch(Exception error) {
            toast("Could not load page", 0);
        }
    }

    //defaults
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
        if(regFirstName.getText().toString().length() == 0) {
            errMsg += "Enter first name\n";
        }
        if(regLastName.getText().toString().length() == 0) {
            errMsg += "Enter last name\n";
        }
        if(regEmail.getText().toString().length() == 0 ||
                !regEmail.getText().toString().matches(regexEmail)) {
            errMsg += "Invalid Email Address\n";
        }
        if(regPassword.getText().toString().length() < 6) {
            errMsg += "Password must have a minimum of 6 characters\n";
        }
    }

    // onclick
    public void goToLogin(View view) {
        redirect(Login.class);
    }

    public void signupUser(View view) {
        try {
            validate();
            if(errMsg.length() != 0) {
                toast(errMsg, 1);
            }else{
                BU user = new BU();
                user.FirstName(regFirstName.getText().toString().trim());
                user.LastName(regLastName.getText().toString().trim());
                user.Email(regEmail.getText().toString().trim());
                user.Password(regPassword.getText().toString().trim());
                JSONArray data = new JSONArray();
                data.put(user);
                callAPI(data);
            }
        }catch(Exception error) {
            toast(error.getMessage(), 0);
        }
    }

    // api handling
    private void callAPI(JSONArray data) {
        new API().callServer(getApplicationContext(), 1, "register", data, new IAPI() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if(response.getString("status").equals("1")) {
                        toast(response.getString("msg"), 1);
                    }else{
                        toast("User Created, redirecting to login page...", 1);
                        redirect(Login.class);
                    }
                }catch(Exception error) {
                    toast(error.getMessage(), 1);
                }
            }

            @Override
            public void onError(VolleyError error) {
                toast("Server Error: "+ error.getMessage(), 1);
            }
        });
    }


}