package com.example.pupezaur.Utils;

public class Admin extends User {

    String uid;
    String aid;
    String name;
    String phoneNumber;
    String adminPhoneNumber;
    String users, chats;
    boolean isAdmin;

//    public Admin(String aid, String uid, String name, String phoneNumber, String adminPhoneNumber, boolean isAdmin, String users, String chats) {
//        this.users = users;
//        this.chats = chats;
//        this.aid = aid;
//        this.uid = uid;
//        this.name = name;
//        this.phoneNumber = phoneNumber;
//        this.adminPhoneNumber = adminPhoneNumber;
//        this.isAdmin = isAdmin;
//    }

    public Admin() {
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getChats() {
        return chats;
    }

    public void setChats(String chats) {
        this.chats = chats;
    }

    public String getAid() { return aid; }

    public void setAid(String aid) { this.aid = aid; }

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
        return uid +
                    "isAdmin='" + isAdmin + '\'' +
                    ", adminId=" + uid + '\'' +
                    ", adminName=" + name + '\'' +
                    ", adminPhoneNumber=" + adminPhoneNumber + '\'' +
                    "Users{" +
//                            User.class +
                        ", uid{" + uid + '\'' +
                        ", name=" + name + '\'' +
                        ", phoneNumber=" + phoneNumber + '\'' +
                        ", adminPhoneNumber=" + adminPhoneNumber + '\'' +
                        ", isAdmin=" + isAdmin + '\'' +
                    "Chats{" +
                        Message.class +
//                "Message =" + '\'' +
//                ", Name =" + '\'' +
//                          "Phone Number ='" + '\'' +
                '}';
    }
}

//
//                ", phoneNumber='" + adminPhoneNumber+ '\'' +
//
//                "uid='" + uid + '\'' +
//                ", phoneNumber='" + phoneNumber + '\'' +
//                '}';


//    public Message() {}
//
//    public Message(String message, String name) {
//        this.message = message;
//        this.name = name;

