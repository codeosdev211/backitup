package com.jrm.backitup.Models;

/*
 *  @Author Jayesh (codeos)
 * I hate comments but this is for you to understand my strange code.
 * My function names will mostly explain the code.
 */
public class BFG {
    private int id;
    private String fileCode;
    private String groupCode;
    private String addedOn;
    private String addedBy;

    /*
     * Yes! getters and setters are in a single method. WHY?? i dont want nulls in database
     * so this way is okay? idk
     * In words,
     *  if the value is null for string and -1 for interger/double/float it GETS the value
     *  else it SETS the value and instantly GETS too cuz a = b = 4; at end a and b are 4
     */

    public int ID(int value) {
        return (value == -1) ? id : (id = value);
    }

    public String FileCode(String value) {
        return (value == null) ? fileCode : (fileCode = value);
    }

    public String GroupCode(String value) {
        return (value == null) ? groupCode : (groupCode = value);
    }

    public String AddedOn(String value) {
        return (value == null) ? addedOn : (addedOn = value);
    }

    public String AddedBy(String value) {
        return (value == null) ? addedBy : (addedBy = value);
    }
}
