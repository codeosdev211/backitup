package com.jrm.backitup.Models;

import java.util.ArrayList;

public class FileInfo {
    private ArrayList<BF> fileInfos;
    private ArrayList<String> fileData;

    public ArrayList<BF> FileInfos(ArrayList<BF> value) {
        return (value == null) ? fileInfos : (fileInfos = value);
    }

    public ArrayList<String> FileData(ArrayList<String> value) {
        return (value == null) ? fileData : (fileData = value);
    }
}
