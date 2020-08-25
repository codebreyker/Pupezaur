package com.example.pupezaur.Utils;

public class ScheduleUtil {
    String startHour;
    String endHour;
    String schedule;

    public ScheduleUtil() {}

    public ScheduleUtil(String startHour, String endHour, String schedule) {
        this.startHour = startHour;
        this.endHour = endHour;
        this.schedule = schedule;
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

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "schedule='" + schedule + '\'' +
                ", startHour='" + startHour + '\'' +
                ", endHour='" + endHour + '\'' +
                '}';
    }
}