package com.jrm.backitup.Connections;

/*
 *  @Author Jayesh (codeos)
 * I hate comments but this is for you to understand my strange code.
 * My function names will mostly explain the code.
 */
public class AppURL {
    private String baseURL = "http://192.168.1.11:8001";

    /*
    * handle urls to keep it clean.
    */
    public String getURL(String type) {
        switch(type) {
            case "login":
                return baseURL + "/user/login";
            case "register":
                return baseURL + "/user/register";
            case "sendFile":
                return baseURL + "/files/add";
            case "listFile":
                return baseURL + "/files/list";
            case "listStats":
                return baseURL + "/user/stats";
            case "downloadFile":
                return baseURL + "/files/download";
            case "listGroups":
                return baseURL + "/groups/list";
            case "createGrp":
                return baseURL + "/groups/create";
            default:
                break;
        }
        return "";
    }
}
