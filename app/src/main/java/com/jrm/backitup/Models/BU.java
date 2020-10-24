package com.jrm.backitup.Models;

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
