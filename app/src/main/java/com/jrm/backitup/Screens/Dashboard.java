package com.jrm.backitup.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jrm.backitup.Local.AppPref;
import com.jrm.backitup.R;

import org.json.JSONObject;

public class Dashboard extends AppCompatActivity {

    JSONObject currentUser = null;
    private final int requestCode = 911;
    private final int fileCode = 211;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        try {
            currentUser = new JSONObject(new AppPref().localData(getApplicationContext(), 'G', "BU", ""));
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

    private void checkPermission(String permissionName) {
        if(ContextCompat.checkSelfPermission(
            getApplicationContext(), permissionName) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(
                    Dashboard.this,
                    new String[] { permissionName },
                    requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);

        if(requestCode == this.requestCode && results.length > 0
            && results[0] == PackageManager.PERMISSION_GRANTED) {

            toast("Permissons Granted", 0);
        }
    }

    @Override
    public void onActivityResult(int fileCode, int resultCode, Intent data) {
        super.onActivityResult(fileCode, resultCode, data);

        if (fileCode == this.fileCode && resultCode == -1) {
            Uri fileUri = data.getData();
            String filePath = fileUri.getPath();
            toast(filePath, 1);
            // continue here later ... task (convert file to array of bytes
        }
    }

    // onclick
    public void chooseFiles(View view) {
        Intent file = new Intent(Intent.ACTION_GET_CONTENT);
        file.setType("*/*");
        file = Intent.createChooser(file, "Select files...");
        startActivityForResult(file, fileCode);
    }






}