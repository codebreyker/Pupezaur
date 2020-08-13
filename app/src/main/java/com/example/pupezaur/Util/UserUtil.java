package com.example.pupezaur.Util;

public class UserUtil {
    public static String name, email;
    public static String uid;

    public UserUtil() {

    }

    public static String getName() {
        return name;
    }

    public void setName(String name) {
        UserUtil.name = name;
    }

    public String getId() {
        return uid;
    }

    public void setId(String id) {
        UserUtil.uid = uid;
    }

    public static String getEmail() { return email;}

    public static void setEmail(String email) { UserUtil.email = email; }

    @Override
    public String toString() {
        return "User(" +
                "id=' " + uid + '\'' +
                ", name=' " + name + '\'' +
                ')';
    }
}

