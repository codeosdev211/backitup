package com.jrm.backitup.Screens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.jrm.backitup.Connections.API;
import com.jrm.backitup.Connections.IAPI;
import com.jrm.backitup.Lists.ShareAdp;
import com.jrm.backitup.Local.AppPref;
import com.jrm.backitup.Models.BF;
import com.jrm.backitup.Models.BFG;
import com.jrm.backitup.Models.BG;
import com.jrm.backitup.Models.BU;
import com.jrm.backitup.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/*
*  @Author Jayesh (codeos)
*  I hate comments but this is for you to understand my strange code.
*  My function names will mostly explain the code.
*/
public class ShareRoom extends AppCompatActivity {

    // currentUser will hold the values we stored in shared preferences while signing in
    JSONObject currentUser;

    // recyclerview and list  stuff for shared files
    RecyclerView srList;
    ShareAdp shareAdp;

    ArrayAdapter<JSONObject> filesAdapter;
    //collecting extras from bundle, sent by GroupsAdp
    JSONObject currentGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_room);
        try {
            currentUser = new JSONObject(new AppPref().localData(getApplicationContext(), 'G', "BU", ""));
            currentGroup = new JSONObject(getIntent().getExtras().getString("group"));

            srList = (RecyclerView) findViewById(R.id.srList);

            srList.setLayoutManager(new LinearLayoutManager(this));
            shareAdp = new ShareAdp(this, currentUser.getString("code"));

            loadList();
            loadUserFiles();

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
    public void redirectToGroups(View view) {
        redirect(Groups.class);
    }

    public void shareFiles(View view) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            ViewGroup parent = findViewById(android.R.id.content);
            View layout = LayoutInflater.from(view.getContext()).inflate(R.layout.share_layout, parent, false);
            builder.setView(layout);
            AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.show();

            // elements for dialog
            Spinner userFiles = layout.findViewById(R.id.userFiles);
            userFiles.setAdapter(filesAdapter);

            //onclicks for dialog
            Button shareFile = layout.findViewById(R.id.shareFile);
            shareFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // call api for upload.
                    try {
                        BFG shareLog = new BFG();
                        shareLog.FileCode(filesAdapter.getItem(userFiles.getSelectedItemPosition()).getString("code"));
                        shareLog.GroupCode(currentGroup.getString("code"));
                        shareLog.AddedBy(currentUser.getString("code"));
                        sendFile(new JSONArray().put(shareLog));
                    }catch(Exception error) {
                        toast(error.getMessage(), 0);
                    }
                }
            });

        }catch(Exception error) {
            toast("Could not load dialog", 0);
        }
    }

    // list handling
    private void loadList() {
        try {
            BG group = new BG();
            group.Code(currentGroup.getString("code"));
            loadGroupShares(new JSONArray().put(group));
        }catch(Exception error) {
            toast(error.getMessage(), 0);
        }
    }

    // api handling
    private void loadGroupShares(JSONArray data) {
        new API().callServer(getApplicationContext(), 1, "sharedOnGrp", data, new IAPI() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if(response.getString("status").equals("1")) {
                        toast(response.getString("msg"), 1);
                    }else{
                        shareAdp.add(response.getJSONArray("data"));
                        srList.setAdapter(shareAdp);
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

    private void sendFile(JSONArray data) {
        new API().callServer(getApplicationContext(), 1, "shareOnGroup", data, new IAPI() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if(response.getString("status").equals("1")) {
                        toast(response.getString("msg"), 1);
                    }else{
                        toast("File Shared", 0);
                        loadList();
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
    private void loadUserFiles() throws Exception {
        BU user = new BU();
        user.Code(currentUser.getString("code"));

        new API().callServer(getApplicationContext(), 1, "listFile", new JSONArray().put(user), new IAPI() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if(response.getString("status").equals("1")) {
                        toast(response.getString("msg"), 1);
                    }else{
                        ArrayList<JSONObject> userFiles = new ArrayList<>();
                        JSONArray files = response.getJSONArray("data");
                        for(int each = 0; each < files.length(); each++) {
                            userFiles.add(files.getJSONObject(each));
                        }
                        filesAdapter = new ArrayAdapter<JSONObject>(getApplicationContext(),
                                android.R.layout.simple_spinner_dropdown_item, userFiles);
                        filesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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