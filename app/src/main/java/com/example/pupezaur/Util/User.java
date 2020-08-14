package com.example.pupezaur.Util;

public class User {
    String id;
    public static String username, email;

    public User(){}

    public String getUid() {
        return id;
    }

    public void setUid(String uid) {
        this.id = uid;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    @Override
    public String toString() {
        return "Users{" +
                "uid='" + id + '\'' +
                ", name='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
