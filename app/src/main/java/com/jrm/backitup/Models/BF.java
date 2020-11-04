package com.jrm.backitup.Models;

public class BF {
    private int id;
    private String code;
    private String name;
    private String extension;
    private String originalSize;
    private String ownerCode;
    private String savedTo;

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
