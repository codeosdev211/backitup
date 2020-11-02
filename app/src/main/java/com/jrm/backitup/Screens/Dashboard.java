package com.jrm.backitup.Screens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.jrm.backitup.Local.AppPref;
import com.jrm.backitup.Local.CompressionUtils;
import com.jrm.backitup.R;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Objects;


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
            checkPermission();
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
        if (fileCode == this.fileCode && resultCode == -1) {
            // if multiple select then into if else single selection
            if(data.getClipData() != null) {
                for(int each = 0; each < data.getClipData().getItemCount(); each++) {
//                    appendFilesList(data.getClipData().getItemAt(each).getUri());
                }
            }else{
//                appendFilesList(data.getData());
                Uri fileUri = data.getData();
                CompressionUtils compression = new CompressionUtils();
                StringBuilder builder = new StringBuilder();
                InputStream stream = getContentResolver().openInputStream(fileUri);
                byte[] bytes =  compression.compress(IOUtils.toByteArray(stream));

                String destPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + getFileName(fileUri);
                IOUtils.write(compression.decompress(bytes), new FileOutputStream(destPath));
                toast("File Written", 1);

            }
        }
        }catch(Exception error) {
            toast(error.getMessage(), 1);
        }
    }

    private void appendFileToList(Uri fileUri) {
    }

    private void appendFile(Uri fileUri) {
        try {
            // converting files to byte array
            String fileName = getFileName(fileUri);
            InputStream stream = getContentResolver().openInputStream(fileUri);
//            byte[] data = getBytes(stream);
            byte[] data = IOUtils.toByteArray(stream);
            // download files code below
            String destPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + fileName;
            FileOutputStream outStream = new FileOutputStream(destPath);
//            outStream.write(data);
//            outStream.close();
            IOUtils.write(data, outStream);
            toast("written data", 1);

        }catch(Exception error) {
            toast("Error" + error.getMessage(), 1);
        }
    }

    private String getFileName(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if(cursor.getCount() <= 0) {
            toast("cant obtain file name", 1);
            cursor.close();
        }
        cursor.moveToFirst();

        String fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));

        cursor.close();
        return fileName;
    }


    // onclick
    private void chooseFiles() {
            Intent file = new Intent(Intent.ACTION_GET_CONTENT);
            file.setType("*/*");
            file.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            file = Intent.createChooser(file, "Select files...");
            startActivityForResult(file, fileCode);
    }

    private void uploadFiles() { }

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







}