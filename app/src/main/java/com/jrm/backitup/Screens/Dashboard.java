package com.jrm.backitup.Screens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.jrm.backitup.Connections.API;
import com.jrm.backitup.Connections.IAPI;
import com.jrm.backitup.Local.AppPref;
import com.jrm.backitup.Local.FileHelper;
import com.jrm.backitup.Models.BF;
import com.jrm.backitup.Models.BU;
import com.jrm.backitup.R;

import org.json.JSONArray;
import org.json.JSONObject;


public class Dashboard extends AppCompatActivity {

    JSONObject currentUser = null;
    private final int requestCode = 911;
    private final int fileCode = 211;
    JSONArray fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        try {
            currentUser = new JSONObject(new AppPref().localData(getApplicationContext(), 'G', "BU", ""));
            checkPermission();
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
            fileList = new JSONArray();
            if (fileCode == this.fileCode && resultCode == -1) {
                // if multiple select then into if else single selection
                if(data.getClipData() != null) {
                    for(int each = 0; each < data.getClipData().getItemCount(); each++) {
                        appendFileToList(data.getClipData().getItemAt(each).getUri());
                    }
                }else{
                    appendFileToList(data.getData());
                }
                toast("Files Selected", 1);
            }
        }catch(Exception error) {
            toast(error.getMessage(), 1);
        }
    }

    private void appendFileToList(Uri fileUri) {
        try {
            BF selected = new BF();
            FileHelper helper = new FileHelper();
            selected.Id(0);
            selected.Code("");
            selected.Name(helper.getAttribute(getApplicationContext(), fileUri, OpenableColumns.DISPLAY_NAME));
            selected.OriginalSize(helper.getAttribute(getApplicationContext(), fileUri, OpenableColumns.SIZE));
            selected.OwnerCode(currentUser.getString("code"));
//            selected.Extension(helper.getAttribute(getApplicationContext(), fileUri, OpenableColumns.DISPLAY_NAME).replaceFirst("^.*/[^/]*(\\.[^\\./]*|)$", "$1"));
            selected.Extension("none");
//            selected.FileData(new String(helper.readFile(getApplicationContext(), fileUri)));
            selected.FileData(Base64.encode(helper.readFile(getApplicationContext(), fileUri), 0).toString());
            fileList.put(selected);
        }catch(Exception error) {
            toast(error.getMessage(), 1);
        }
    }

    // onclick
    private void chooseFiles() {
            Intent file = new Intent(Intent.ACTION_GET_CONTENT);
            file.setType("*/*");
            file.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            file = Intent.createChooser(file, "Select files...");
            startActivityForResult(file, fileCode);
    }

    private void uploadFiles() {
        if(fileList.length() == 0) {
            toast("No files selected", 1);
        }else{
            sendFiles(fileList);
        }
    }

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
    private void loadList(JSONArray data) {
        try {
            if(data.length() == 0 || data == null) {
                toast("No Files Found!", 1);
            }else{
                // load adapter
            }
        }catch(Exception error) {
            toast(error.getMessage(), 1);
        }
    }

    // api handling
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
//                        loadList(response.getJSONArray("data"));
                        new FileHelper().writeFile(response.getJSONArray("data").getJSONObject(0));
                        toast("file written i guess!", 1);
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






}