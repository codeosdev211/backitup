package com.jrm.backitup.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.jrm.backitup.Connections.API;
import com.jrm.backitup.Connections.IAPI;
import com.jrm.backitup.Lists.GroupListAdp;
import com.jrm.backitup.Local.AppPref;
import com.jrm.backitup.Local.Query;
import com.jrm.backitup.Models.BG;
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

    // currentUser will hold the values we stored in shared preference while signing in
    JSONObject currentUser = null;

    // layout that contains the search stuff
    ConstraintLayout searchPanel;

    // recyclerview for the user's groups
    RecyclerView groupList, searchList;
    RecyclerView.LayoutManager layoutMng;
    GroupListAdp groupAdp, searchAdp;

    // database query handler
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups);
        try {
            currentUser = new JSONObject(new AppPref().localData(getApplicationContext(), 'G', "BU", ""));
            query = new Query(getApplicationContext());

            searchBox = (EditText) findViewById(R.id.grpSearchBox);
            searchPanel = (ConstraintLayout) findViewById(R.id.grpSearchPanel);
            groupList = (RecyclerView) findViewById(R.id.grpList);
            searchList = (RecyclerView) findViewById(R.id.grpSearchList);

            //load list related stuff
            layoutMng = new LinearLayoutManager(this);
            groupList.setLayoutManager(layoutMng);
            searchList.setLayoutManager(layoutMng);
            groupAdp = new GroupListAdp(this);
            searchAdp = new GroupListAdp(this);

            BU user = new BU();
            user.Code(currentUser.getString("code"));
            loadGroupsToDb(new JSONArray().put(user));
            query.readGroups("");

            /* search on text change by calling local database not the server. */
            searchBox.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
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
    public void openSearch(View view) {
         searchPanel.setVisibility(View.VISIBLE);
    }

    public void closeSearch(View view) {
        searchPanel.setVisibility(View.GONE);
    }

    // list handling


    // api handling
    private void loadGroupsToDb(JSONArray data) {
        new API().callServer(getApplicationContext(), 1, "listGroups", data, new IAPI() {

            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if(response.getString("status").equals("1")) {
                        toast(response.getString("msg"), 1);
                    }else{
                        // load to database
                        JSONArray groups = response.getJSONArray("data");
                        for(int grp = 0; grp < groups.length(); grp++) {
                           query.writeGroup(groups.getJSONObject(grp));
                        }
                    }
                }catch(Exception error) {
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