
package com.example.pupezaur.Utils;

public class User {

    String uid;
    String firstName;
    String lastName;
    String phoneNumber;
    String adminPhoneNumber;
    String name;
    String notificationKey;
    boolean isAdmin;

    public User(){}

    public User(String uid) {
        this.uid = uid;
    }

    public String getNotificationKey() {
        return notificationKey;
    }
    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }

    public void setName(String firstName, String lastName) {
        this.name = firstName + " " + lastName;
    }
    public String getName() {
        return firstName + " " + lastName;
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

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", adminPhoneNumber='" + adminPhoneNumber + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}