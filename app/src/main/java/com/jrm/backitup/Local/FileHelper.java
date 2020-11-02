package com.jrm.backitup.Local;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.InputStream;

public class FileHelper {

    public byte[] readFile(Context context, Uri fileData) throws Exception {
        return new CompressionUtils().compress(IOUtils.toByteArray(context.getContentResolver().openInputStream(fileData)));
    }

    public void writeFile(JSONObject fileData) throws Exception {
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + fileData.getString("fileName");
        IOUtils.write(new CompressionUtils().decompress(fileData.getString("fileData").getBytes()), new FileOutputStream(destination));
    }




}
