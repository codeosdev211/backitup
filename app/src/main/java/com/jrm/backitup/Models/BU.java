package com.jrm.backitup.Models;

/*
 *  @Author Jayesh (codeos)
 * I hate comments but this is for you to understand my strange code.
 * My function names will mostly explain the code.
 */
public class BU {
    private int id;
    private String code;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int totalGroups;
    private int totalFiles;
    private String createdOn;
    private String isActive;

    /*
     * Yes! getters and setters are in a single method. WHY?? i dont want nulls in database
     * so this way is okay? idk
     * In words,
     *  if the value is null for string and -1 for interger/double/float it GETS the value
     *  else it SETS the value and instantly GETS too cuz a = b = 4; at end a and b are 4
     */
    public int Id(int value) {
        return (value == -1) ? id : (id = value);
    }

    public String Code(String value) {
        return (value == null) ? code : (code = value);
    }

    public String FirstName(String value) {
        return (value == null) ? firstName : (firstName = value);
    }

    public String LastName(String value) {
        return (value == null) ? lastName : (lastName = value);
    }

    public String Email(String value) {
        return (value == null) ? email : (email = value);
    }

    public String Password(String value) {
        return (value == null) ? password : (password = value);
    }

    public int TotalGroups(int value) {
        return (value == -1) ? totalGroups : (totalGroups = value);
    }

    public int TotalFiles(int value) {
        return (value == -1) ? totalFiles : (totalFiles = value);
    }

    public String CreatedOn(String value) {
        return (value == null) ? createdOn : (createdOn = value);
    }

    public String IsActive(String value) {
        return (value == null) ? isActive : (isActive = value);
    }

}
