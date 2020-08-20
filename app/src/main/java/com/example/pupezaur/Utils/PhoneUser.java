package com.example.pupezaur.Utils;

public class PhoneUser {

    String uid, user, phoneNumber, adminPhoneNumber;
    boolean isAdmin;

    public PhoneUser(String uid, String user, String phoneNumber, String adminPhoneNumber, boolean isAdmin) {
        this.uid = uid;
        this.user = user;
        this.phoneNumber = phoneNumber;
        this.adminPhoneNumber = adminPhoneNumber;
        this.isAdmin = isAdmin;
    }

    public PhoneUser() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAdminPhoneNumber() {
        return adminPhoneNumber;
    }

    public void setAdminPhoneNumber(String adminPhoneNumber) {
        this.adminPhoneNumber = adminPhoneNumber;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
