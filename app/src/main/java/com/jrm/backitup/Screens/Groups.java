package com.jrm.backitup.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.jrm.backitup.Connections.API;
import com.jrm.backitup.Connections.IAPI;
import com.jrm.backitup.Lists.GroupsAdp;
import com.jrm.backitup.Local.AppPref;
import com.jrm.backitup.Models.BU;
import com.jrm.backitup.R;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 *  @Author Jayesh (codeos)
 * I hate comments but this is for you to understand my strange code.
 * My function names will mostly explain the code.
 */
public class Groups extends AppCompatActivity {

    EditText searchBox;
    ConstraintLayout searchPanel;

    //currentUser will hold the values we stored in shared preference while signing in
    JSONObject currentUser = null;

    // recyclerview and list stuff for groups
    RecyclerView grpList;
    GroupsAdp groupAdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups);
        try {
            currentUser = new JSONObject(new AppPref().localData(getApplicationContext(), 'G', "BU", ""));

            searchBox = (EditText) findViewById(R.id.grpSearchBox);
            searchPanel = (ConstraintLayout) findViewById(R.id.grpSearchPanel);
            grpList = (RecyclerView) findViewById(R.id.grpList);

            grpList.setLayoutManager(new LinearLayoutManager(this));
            groupAdp = new GroupsAdp(this);
            loadList();

            searchBox.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        groupAdp.search(s);
                        grpList.setAdapter(groupAdp);
                    }catch(Exception error) {
                        toast(error.getMessage(), 0);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) { }
            });

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
    public void redirectToDashboard(View view) {
        redirect(Dashboard.class);
    }

    public void redirectToCreateGroup(View view) {
        redirect(Dashboard.class);
    }

    public void showSearchPanel(View view) {
        searchPanel.setVisibility(View.VISIBLE);
    }

    public void hideSearchPanel(View view) {
        searchPanel.setVisibility(View.GONE);
        groupAdp.refresh();
        grpList.setAdapter(groupAdp);
    }

    // list handling
    public void loadList() {
        try {
            BU user = new BU();
            user.Code(currentUser.getString("code"));
            loadGroups(new JSONArray().put(user));
        }catch(Exception error) {
            toast(error.getMessage(), 0);
        }
    }

    // api handling
    private void loadGroups(JSONArray data) {
        new API().callServer(getApplicationContext(), 1, "listGrps", data, new IAPI() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if(response.getString("status").equals("1")) {
                        toast(response.getString("msg"), 1);
                    }else{
                        groupAdp.add(response.getJSONArray("userGroups"),response.getJSONArray("publicGroups"));
                        grpList.setAdapter(groupAdp);
                    }
                }catch(Exception error){
                    toast(error.getMessage(), 0);
                }
            }

            @Override
            public void onError(VolleyError error) {
                toast(error.getMessage(), 1);
            }
        });
    }

}