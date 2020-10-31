package com.jrm.backitup.Connections;

public class AppURL {
    private String baseURL = "http://192.168.1.11:8001";

    public String getURL(String type) {
        switch(type) {
            case "login":
                return baseURL + "/user/login";
            case "register":
                return baseURL + "/user/register";
            default:
                break;
        }
        return "";
    }
}
