package com.example.pupezaur.Utils;

import java.util.Date;

public class Message {
    String message;
    String name;
    String key;
    String userId;
    private boolean isSeen;

    long messageTime;

    public Message() {}

    public Message(String message, String name, String userId, boolean isSeen) {
        this.message = message;
        this.name = name;
        this.userId = userId;
        this.isSeen = isSeen;

        messageTime = new Date().getTime();
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getMessageTime() {return messageTime;}

    public void setMessageTime(long messageTime) {this.messageTime = messageTime;}

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", userId='" + userId + '\'' +
                ", isSeen=" + isSeen +
                ", messageTime=" + messageTime +
                '}';
    }
}