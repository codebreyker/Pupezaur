package com.example.pupezaur.Utils;

public class Admin extends User {

    String uid;
    String name;
    String phoneNumber;
    String adminPhoneNumber;
    String notificationKey;
    boolean isAdmin;

    public Admin(String uid, String name, String phoneNumber, String adminPhoneNumber, boolean isAdmin) {
        this.uid = uid;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.adminPhoneNumber = adminPhoneNumber;
        this.isAdmin = isAdmin;
    }

    public Admin() {
    }

    @Override
    public String getNotificationKey() {
        return notificationKey;
    }

    @Override
    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAdminPhoneNumber() { return adminPhoneNumber; }

    public void setAdminPhoneNumber(String adminPhoneNumber) { this.adminPhoneNumber = adminPhoneNumber; }

    public boolean isAdmin() { return isAdmin; }

    public void setAdmin(boolean admin) { isAdmin = admin; }

    @Override
    public String toString() {
        return "Admin{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", notificationKey='" + notificationKey + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}

