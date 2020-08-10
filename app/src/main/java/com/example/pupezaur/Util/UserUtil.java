package com.example.pupezaur.Util;

public class UserUtil {
    public static String name;
    public static String id;

    public UserUtil(String id, String name) {
        UserUtil.id = id;
        UserUtil.name = name;
    }

    public UserUtil() {

    }

    public static String getName() {
        return name;
    }

    public void setName(String name) {
        UserUtil.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        UserUtil.id = id;
    }
}
