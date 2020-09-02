package com.example.pupezaur.Utils;

public class Schedule {
    String id;
    String startHour;
    String endHour;
    String key;

    public Schedule() {}

    public Schedule(String startHour, String endHour) {
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id='" + id + '\'' +
                ", startHour='" + startHour + '\'' +
                ", endHour='" + endHour + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}