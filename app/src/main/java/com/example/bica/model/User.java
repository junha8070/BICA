package com.example.bica.model;

import com.example.bica.ChipData;

import java.util.ArrayList;

public class User {

    String email;
    String username;
    String phonenum;
    ArrayList<String> group = new ArrayList<>();

    public ArrayList<String> getGroup() {
        return group;
    }

    public void setGroup(ArrayList<String> group) {
        this.group = group;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public User(){

    }
}
