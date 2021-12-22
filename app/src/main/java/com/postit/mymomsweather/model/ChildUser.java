package com.postit.mymomsweather.model;

public class ChildUser {
    String name;
    String email;
    int age;
    int sex;

    public ChildUser() {
    }

    public ChildUser(String name, String email, int age, int sex) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.sex = sex;
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
