package com.jrm.backitup.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.jrm.backitup.Connections.API;
import com.jrm.backitup.Connections.IAPI;
import com.jrm.backitup.Lists.MembersAdp;
import com.jrm.backitup.Local.AppPref;
import com.jrm.backitup.Models.BG;
import com.jrm.backitup.R;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 *  @Author Jayesh (codeos)
 * I hate comments but this is for you to understand my strange code.
 * My function names will mostly explain the code.
 */
public class GroupInfo extends AppCompatActivity {


    //currentUser will hold the values we stored in shared preference while signing in
    JSONObject currentUser = null;

    //collecting extras from bundle, sent by GroupsAdp
    JSONObject currentGroup;

    // recyclerview and list stuff
    RecyclerView membersList;
    MembersAdp membersAdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_info);
        try {
            currentUser = new JSONObject(new AppPref().localData(getApplicationContext(), 'G', "BU", ""));
            currentGroup = new JSONObject(getIntent().getExtras().getString("group"));

            membersList = (RecyclerView) findViewById(R.id.infoMemberList);

            // adapter and stuff for the list
            membersList.setLayoutManager(new LinearLayoutManager(this));
            membersAdp = new MembersAdp(this);

            loadList();

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

    // list handling
    public void loadList() {
        try {
            BG group = new BG();
            group.Code(currentGroup.getString("code"));
            group.OwnerCode(currentGroup.getString("ownerCode"));
            loadMemberList(new JSONArray().put(group));
        }catch(Exception error) {
            toast("Could not load list", 0);
        }
    }

    // api handling
    private void loadMemberList(JSONArray data) {
        new API().callServer(getApplicationContext(), 1, "membersList", data, new IAPI() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if(response.getString("status").equals("1")) {
                        toast(response.getString("msg"), 0);
                    }else{
                        // load list adapter....
                        membersAdp.add(response.getJSONArray("data"));
                        membersList.setAdapter(membersAdp);
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