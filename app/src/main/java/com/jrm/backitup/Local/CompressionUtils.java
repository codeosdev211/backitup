package com.jrm.backitup.Local;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class CompressionUtils {

    public byte[] compress(byte[] fileData) throws Exception {
        Deflater deflater = new Deflater();
        deflater.setInput(fileData);

        ByteArrayOutputStream stream = new ByteArrayOutputStream(fileData.length);
        deflater.finish();
        byte[] buffer = new byte[1024];
        int count = 1;
        while(!deflater.finished()) {
            count = deflater.deflate(buffer);
            stream.write(buffer, 0, count);
        }
        stream.close();
        return stream.toByteArray();
    }

    public byte[] decompress(byte[] fileData) throws Exception {
        Inflater inflater = new Inflater();
        inflater.setInput(fileData);

        ByteArrayOutputStream stream = new ByteArrayOutputStream(fileData.length);
        byte[] buffer = new byte[1024];
        int count = 0;
        while(!inflater.finished()) {
            count = inflater.inflate(buffer);
            stream.write(buffer, 0, count);
        }
        stream.close();
        return stream.toByteArray();
    }



}
