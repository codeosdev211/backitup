package com.jrm.backitup.Connections;

public class AppURL {
    private String baseURL = "";

    public String getURL(String type) {
        switch(type) {
            case "signin":
                return baseURL + "/auth/signin";
            default:
                break;
        }
        return "";
    }
}
