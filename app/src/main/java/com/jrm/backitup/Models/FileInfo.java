package com.jrm.backitup.Models;

import java.util.ArrayList;

/*
 *  @Author Jayesh (codeos)
 * I hate comments but this is for you to understand my strange code.
 * My function names will mostly explain the code.
 */
public class FileInfo {
    private ArrayList<BF> fileInfos;
    private ArrayList<String> fileData;

    /*
     * Yes! getters and setters are in a single method. WHY?? i dont want nulls in database
     * so this way is okay? idk
     * In words,
     *  if the value is null for string and -1 for interger/double/float it GETS the value
     *  else it SETS the value and instantly GETS to cuz a = b = 4; at end a and b are 4
     */
    public ArrayList<BF> FileInfos(ArrayList<BF> value) {
        return (value == null) ? fileInfos : (fileInfos = value);
    }

    public ArrayList<String> FileData(ArrayList<String> value) {
        return (value == null) ? fileData : (fileData = value);
    }
}
