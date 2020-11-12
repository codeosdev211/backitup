package com.jrm.backitup.Screens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jrm.backitup.Connections.API;
import com.jrm.backitup.Connections.IAPI;
import com.jrm.backitup.Lists.FileListAdp;
import com.jrm.backitup.Local.AppPref;
import com.jrm.backitup.Local.FileHelper;
import com.jrm.backitup.Models.BF;
import com.jrm.backitup.Models.BU;
import com.jrm.backitup.R;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.zip.Deflater;


/*
 *  @Author Jayesh (codeos)
 * I hate comments but this is for you to understand my strange code.
 * My function names will mostly explain the code.
 */

public class Dashboard extends AppCompatActivity {

    // currentUser will hold the values we stored in shared preference while signing in
    JSONObject currentUser = null;
    // CODES for permission, verification, etc.
    private final int requestCode = 911;
    private final int fileCode = 211;
    // This FileHelper class helps in converting files to byte array and vice versa
    FileHelper fileHelper;

    TextView userName, totalFiles, totalGroups, fileName, fileSize;
    // This will hold the unique value for the selected file
    String selectedFile = "";

    // recyclerview for displaying file names
    RecyclerView fileList;
    RecyclerView.LayoutManager layoutMng;
    FileListAdp fileAdp;

    // Layout that contains selected file details
    ConstraintLayout detailPanel;
    Animation openPanel, closePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        try {
            currentUser = new JSONObject(new AppPref().localData(getApplicationContext(), 'G', "BU", ""));
            checkPermission();

            userName = (TextView) findViewById(R.id.dashUserName);
            totalFiles = (TextView) findViewById(R.id.dashTotalFiles);
            totalGroups = (TextView) findViewById(R.id.dashTotalGroups);
            fileName = (TextView) findViewById(R.id.dashDetailsName);
            fileSize = (TextView) findViewById(R.id.dashDetailsSize);
            fileList = (RecyclerView) findViewById(R.id.dashFilesList);
            detailPanel = (ConstraintLayout) findViewById(R.id.dashfileDetails);

            // loading animations
            openPanel = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
            closePanel = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);

            // loading  adapter for recycler view
            userName.setText("Hey, " + currentUser.getString("firstName"));
            layoutMng = new LinearLayoutManager(this);
            fileList.setLayoutManager(layoutMng);
            fileAdp = new FileListAdp(this);

            // api calls
            displayStats();
            callList();
        }catch(Exception error) {
            toast("Could not load page", 1);
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

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(
            getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
        && ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ) {

            ActivityCompat.requestPermissions(
                    Dashboard.this,
                    new String[] {
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);

        if(requestCode == this.requestCode && results.length > 0
            && results[0] == PackageManager.PERMISSION_GRANTED
            && results[1] == PackageManager.PERMISSION_GRANTED) {
            toast("Permissons Granted", 0);
        }
    }

    @Override
    public void onActivityResult(int fileCode, int resultCode, Intent data) {
        super.onActivityResult(fileCode, resultCode, data);
        try {
            /* now the question why?what?, the file helper i mentioned above will
            * will handle the uri data we provide to it and convert it to our preferable whatever
            */
            fileHelper = new FileHelper(getApplicationContext(), currentUser.getString("code"));
            if (fileCode == this.fileCode && resultCode == -1) {
                // if multiple select then into if else single selection
                if(data.getClipData() != null) {
                    for(int each = 0; each < data.getClipData().getItemCount(); each++) {
                        fileHelper.appendFile(data.getClipData().getItemAt(each).getUri());
                    }
                }else{
                    fileHelper.appendFile(data.getData());
                }
                toast("Files Selected", 0);
            }
        }catch(Exception error) {
            toast(error.getMessage(), 1);
        }
    }



    // onclick
    public void logout(View view) {
        new AppPref().localData(getApplicationContext(), 'S', "isLoggedIn", "No");
        redirect(Login.class);
    }

    // the sync button
    public void reloadList(View view) {
        displayStats();
        callList();
    }

    public void redirectToGroups(View view) {
        redirect(Groups.class);
    }

    // these two down here for animation
    private void openDetails() {
        detailPanel.startAnimation(openPanel);
        detailPanel.setVisibility(View.VISIBLE);
    }

    public void closeDetails(View view) {
        detailPanel.startAnimation(closePanel);
        detailPanel.setVisibility(View.GONE);
    }

    private void chooseFiles() {
            Intent file = new Intent(Intent.ACTION_GET_CONTENT);
            file.setType("*/*");
            file.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            file = Intent.createChooser(file, "Select files...");
            startActivityForResult(file, fileCode);
    }

    private void uploadFiles() {
        if(fileHelper.getSize() == 0) {
            toast("No files selected", 1);
        }else{
            sendFiles(new JSONArray().put(fileHelper.getList()));
        }
    }

    public void downloadFile(View view) {
        try {
            BF reqFile = new BF();
            reqFile.OwnerCode(currentUser.getString("code"));
            reqFile.Code(selectedFile);
            requestDownload(new JSONArray().put(reqFile));
        }catch(Exception error) {
            toast("Could not request download", 0);
        }
    }

    private void displayStats() {
        try {
            BU user = new BU();
            user.Code(currentUser.getString("code"));
            loadStats(new JSONArray().put(user));
        }catch(Exception error) {
            toast("Could not load stats", 0);
        }
    }

    /* this will display a dialog box where you choose/selects/pick files and upload
        with the provided buttons
     */
    public void selectFiles(View view) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            ViewGroup parent = findViewById(android.R.id.content);
            View layout = LayoutInflater.from(view.getContext()).inflate(R.layout.select_files, parent, false);
            builder.setView(layout);
            AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.show();

            // onclicks for dialog
            Button selectBtn = layout.findViewById(R.id.selectBtn);
            Button uploadBtn = layout.findViewById(R.id.uploadBtn);
            selectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseFiles();
                }
            });
            uploadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadFiles();
                }
            });
        }catch(Exception error) {
            toast("Could not load dialogbox", 0);
        }
    }

    // list handling
    // this one will prepare request and send to the server
    private void callList() {
        try {
            JSONArray request = new JSONArray();
            BU user = new BU();
            user.Code(currentUser.getString("code"));
            request.put(user);
            listFiles(request);
        }catch(Exception error) {
            toast(error.getMessage(), 1);
        }
    }

    /* the clicks on the list we load will be handled by the following function to load
        the data to file details layout
        and animate it up on the screen. TADA?!?! right?
    */
    public void onListItemClick(JSONObject file) {
        try {
            fileName.setText(file.getString("name"));
            double size = (Double.parseDouble(file.getString("originalSize"))) / 1000.00;
            fileSize.setText("Size: " + String.valueOf(size) + " KB");
            selectedFile = file.getString("code");
            openDetails();
        }catch(Exception error) {
            toast("Could not perform list item click", 0);
        }
    }

    // api handling
    /*
    * I know a lot of request to the server, its for clg no big deal.
    */
    private void requestDownload(JSONArray data) {
        new API().callServer(getApplicationContext(), 1, "downloadFile", data, new IAPI() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if(response.getString("status").equals("1")) {
                        toast(response.getString("msg"), 1);
                    }else{
                        FileHelper helper = new FileHelper(getApplicationContext(), currentUser.getString("code"));
                        helper.writeFile(response);
                        toast("File Downloaded", 0);
                    }
                }catch(Exception error) {
                    toast(error.getMessage(), 1);
                }
            }

            @Override
            public void onError(VolleyError error) {
                toast(error.getMessage(), 1);
            }
        });
    }
    private void sendFiles(JSONArray data) {
        new API().callServer(getApplicationContext(), 1, "sendFile", data, new IAPI() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if(response.getString("status").equals("1")) {
                        toast(response.getString("msg"), 1);
                    }else{
                        toast("File(s) uploaded!", 0);
                    }
                }catch(Exception error) {
                    toast(error.getMessage(), 1);
                }
            }

            @Override
            public void onError(VolleyError error) {
                toast(error.getMessage(), 1);
            }
        });
    }

    private void listFiles(JSONArray data) {
        new API().callServer(getApplicationContext(), 1, "listFile", data, new IAPI() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if(response.getString("status").equals("1")) {
                        toast(response.getString("msg"), 1);
                    }else{
                        /* checking the len(data) and loading the adapter,recyclerview
                            in the else block
                         */
                        JSONArray files = response.getJSONArray("data");
                        if(files.length() == 0 || files == null) {
                            toast("No Files Found!", 1);
                        }else{
                            fileList.removeAllViews();
                            fileAdp.add(files);
                            fileList.setAdapter(fileAdp);
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

    private void loadStats(JSONArray data) {
        new API().callServer(getApplicationContext(), 1, "listStats", data, new IAPI() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if(response.getString("status").equals("1")) {
                        toast(response.getString("msg"), 1);
                    }else{
                        totalFiles.setText("Total Files Uploaded: " + response.getJSONArray("data").getJSONObject(0).getString("totalFiles"));
                        totalGroups.setText("Your Total Groups: " + response.getJSONArray("data").getJSONObject(0).getString("totalGroups"));
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
