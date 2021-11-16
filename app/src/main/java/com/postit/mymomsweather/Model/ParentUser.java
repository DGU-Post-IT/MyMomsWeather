package com.postit.mymomsweather.Model;

import java.util.ArrayList;

public class ParentUser {
    String name;
    String email;
    ArrayList<String> follower;
    ArrayList<String> requested;
    String phone;
    int age;
    int sex;
    String callDuration;

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public ParentUser() {
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ParentUser(String name, String email, String phone, int age, int sex) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.sex = sex;
        this.follower = new ArrayList<>();
        this.requested = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getFollower() {
        return follower;
    }

    public void setFollower(ArrayList<String> follower) {
        this.follower = follower;
    }

    public ArrayList<String> getRequested() {
        return requested;
    }

    public void setRequested(ArrayList<String> requested) {
        this.requested = requested;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
