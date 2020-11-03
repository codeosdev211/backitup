package com.jrm.backitup.Local;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.InputStream;

public class FileHelper {


    public byte[] readFile(Context context, Uri fileData) throws Exception {
        return new CompressionUtils().compress(new String(IOUtils.toByteArray(
                context.getContentResolver().openInputStream(fileData))));
    }

    public void writeFile(JSONObject fileData) throws Exception {
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + fileData.getString("name");
        byte[] file = new CompressionUtils().decompress(fileData.getString("fileData"));
        IOUtils.write(file, new FileOutputStream(destination));
//        IOUtils.write(new CompressionUtils().decompress(fileData.getString("fileData").getBytes("UTF-8")), new FileOutputStream(destination));
    }

    public String getAttribute(Context context, Uri data, String attribute) {
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
