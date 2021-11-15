package com.postit.mymomsweather.Model;

import org.threeten.bp.LocalDate;

public class CallRecord {
    LocalDate date;
    boolean type;
    String phoneNumber;
    Long duration;

    public CallRecord() {
    }

    public CallRecord(LocalDate date, boolean type, String phoneNumber, Long duration) {
        this.date = date;
        this.type = type;
        this.phoneNumber = phoneNumber;
        this.duration = duration;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
