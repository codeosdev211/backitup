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
import com.jrm.backitup.Models.BG;
import com.jrm.backitup.R;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 * @Author Jayesh (codeos)
 * i hate comments but this is for you to understand my strange code.
 * My function names will mostly explain the code.
 */


public class CreateGroup extends AppCompatActivity {

    /*
     * so this page should have been a dialog box or something for just a single field right? Yes
     * But im planning to add a list to of other users on the platform for the user to add without
     * asking them in the first place so that they have to stay in the group even if they
     * dont want to and cannot leave cuz they dont want the user to feel bad i guess.!
     */

    EditText groupName;

    // currentUser will hold the values we stored in shared preferences while signing in
    JSONObject currentUser = null;

    // this string below is the error message we will prepare when validating stuff
    String errMsg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);
        try {
            currentUser = new JSONObject(new AppPref().localData(getApplicationContext(), 'G', "BU", ""));

            groupName = (EditText) findViewById(R.id.crgrpName);

        }catch(Exception error){
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

    private void validate() {
        errMsg = "";
        if(groupName.getText().toString().length() == 0) {
            errMsg += "Please enter Group name\n";
        }
    }


    // onclick
    public void redirectToGroups(View view) {
        redirect(Groups.class);
    }

    public void createGroup(View view) {
        try {
            validate();
            if(errMsg.length() == 0) {
                toast(errMsg, 0);
            }else{
                BG group = new BG();
                group.Name(groupName.getText().toString().trim());
                group.OwnerCode(currentUser.getString("code"));
                addGrpToServer(new JSONArray().put(group));
            }
        }catch(Exception error) {
            toast("Could not create Group", 0);
        }
    }
    // api handling
    private void addGrpToServer(JSONArray data) {
        new API().callServer(getApplicationContext(), 1, "createGrp", data, new IAPI() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if(response.getString("status").equals("1")) {
                        toast(response.getString("msg") ,1);
                    }else{
                        toast("Group created!It will be added to your list...", 0);
                    }
                }catch(Exception error) {
                    toast(error.getMessage(), 0);
                }
            }

            @Override
            public void onError(VolleyError error) {
                toast(error.getMessage(), 0);
            }
        });
    }


}