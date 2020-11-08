package com.jrm.backitup.Local;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.widget.Toast;

import com.jrm.backitup.Models.BF;
import com.jrm.backitup.Models.FileInfo;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class FileHelper {

    private Context context;
    private ArrayList<BF> fileList;
    private ArrayList<String> dataList;
    private int fileCount;


    public FileHelper(Context context) {
        this.context = context;
        this.fileList = new ArrayList<>();
        this.dataList = new ArrayList<>();
        this.fileCount = 0;
    }

    public int getSize() {
        return fileCount;
    }

    public void appendFile(Uri eachFile) {
        try {
            BF file = new BF();
            String fileName = getAttribute(eachFile, OpenableColumns.DISPLAY_NAME);
            file.Name(fileName);
            file.OriginalSize(getAttribute(eachFile, OpenableColumns.SIZE));
            file.Extension(FilenameUtils.getExtension(fileName));
            fileList.add(file);
            InputStream stream = context.getApplicationContext().getContentResolver().openInputStream(eachFile);
            dataList.add(Base64.encodeToString(IOUtils.toByteArray(stream), 0));
            fileCount++;
        }catch(Exception error) {
            Toast.makeText(context.getApplicationContext(), "Could not append a file", Toast.LENGTH_SHORT).show();
        }
    }

    public FileInfo getList() {
        FileInfo data = new FileInfo();
        data.FileInfos(fileList);
        data.FileData(dataList);
        return data;
    }

    public void writeFile(JSONObject fileData) throws Exception {
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + fileData.getString("name");
        IOUtils.write(Base64.decode(fileData.get("fileData").toString().getBytes(), 0), new FileOutputStream(destination));
    }

    private String getAttribute(Uri data, String attribute) {
        Cursor cursor = context.getContentResolver().query(data, null, null, null, null);
        if(cursor.getCount() <= 0) {
            cursor.close();
            return "";
        }
        cursor.moveToFirst();
        String requested = cursor.getString(cursor.getColumnIndexOrThrow(attribute));
        cursor.close();
        return requested;
    }

}
