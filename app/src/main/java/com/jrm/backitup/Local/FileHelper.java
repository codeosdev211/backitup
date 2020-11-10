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

/*
 *  @Author Jayesh (codeos)
 * I hate comments but this is for you to understand my strange code.
 * My function names will mostly explain the code.
 */
public class FileHelper {

    /*
    * hear me out!
    * this class is the base for this applcation. Just DOWNLOAD the file or UPLOAD it.
    * the data is sent to server in the following format
    * {
    *   "fileInfos" : [
    *                   {...}, {...},
    *                   { the BF class }
    *                 ],
    *   "fileData" : [
    *                   "filedata1 in byte array format converted to string",
    *                   "sdfasdfjksl;fkjsal;jdf",
    *                   "dklajshfjkshfgklshgkjshg"
    *                 ]
    * }
    * here the fileList is FileInfos and dataList is fileData,
    * i dont store file in database, WHY? its slow.
    *
    */
    private Context context;
    private ArrayList<BF> fileList;
    private ArrayList<String> dataList;
    private int fileCount;
    private String ownerCode;


    public FileHelper(Context context, String ownerCode) {
        this.context = context;
        this.fileList = new ArrayList<>();
        this.dataList = new ArrayList<>();
        this.fileCount = 0;
        this.ownerCode = ownerCode;
    }

    public int getSize() {
        return fileCount;
    }

    public void appendFile(Uri eachFile) {
        try {
            /* loading BF object and data string from Uri
            * apache common utils, codec etc used to convert
            */
            BF file = new BF();
            String fileName = getAttribute(eachFile, OpenableColumns.DISPLAY_NAME);
            file.Name(fileName);
            file.OriginalSize(getAttribute(eachFile, OpenableColumns.SIZE));
            file.Extension(FilenameUtils.getExtension(fileName));
            file.OwnerCode(this.ownerCode);
            fileList.add(file);
            InputStream stream = context.getApplicationContext().getContentResolver().openInputStream(eachFile);
            dataList.add(Base64.encodeToString(IOUtils.toByteArray(stream), 0));
            fileCount++;
        }catch(Exception error) {
            Toast.makeText(context.getApplicationContext(), "Could not append a file", Toast.LENGTH_SHORT).show();
        }
    }

    /* this returns the json format discussed above in object form */
    public FileInfo getList() {
        FileInfo data = new FileInfo();
        data.FileInfos(fileList);
        data.FileData(dataList);
        return data;
    }

    public void writeFile(JSONObject fileData) throws Exception {
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + fileData.getString("fileName");
        IOUtils.write(Base64.decode(fileData.get("fileData").toString().getBytes(), 0), new FileOutputStream(destination));
    }

    /* DISPLAY_NAME for file name and SIZE for size ,
        just pass OpenableColumns.DISPLAY_NAME or OpenableColumns.SIZE
        to the function
     */
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
