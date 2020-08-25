
package com.example.pupezaur.Utils;

public class User {
    String aid;
    String uid;
    String name;
    String email;
    boolean isAdmin;

    public User(){}

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    @Override
    public String toString() {
        return "Users{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", isAdmin='" + isAdmin + '\'' +
                '}';
    }
}