package com.jrm.backitup.Models;

/*
 *  @Author Jayesh (codeos)
 * I hate comments but this is for you to understand my strange code.
 * My function names will mostly explain the code.
 */
public class BF {
    private int id;
    private String code;
    private String name;
    private String extension;
    private String originalSize;
    private String ownerCode;
    private String savedTo;

    /*
    * Yes! getters and setters are in a single method. WHY?? i dont want nulls in database
    * so this way is okay? idk
    * In words,
    *  if the value is null for string and -1 for interger/double/float it GETS the value
    *  else it SETS the value and instantly GETS to cuz a = b = 4; at end a and b are 4
    */
    public int Id(int value) {
        return (value == -1) ? id : (id = value);
    }

    public String Code(String value) {
        return (value == null) ? code : (code = value);
    }

    public String Name(String value) {
        return (value == null) ? name : (name = value);
    }

    public String Extension(String value) {
        return (value == null) ? extension : (extension = value);
    }

    public String OriginalSize(String value) {
        return (value == null) ? originalSize : (originalSize = value);
    }

    public String OwnerCode(String value) {
        return (value == null) ? ownerCode : (ownerCode = value);
    }

    public String SavedTo(String value) {
        return (value == null) ? savedTo : (savedTo = value);
    }

}
