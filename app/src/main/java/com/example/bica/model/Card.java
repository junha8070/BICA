package com.example.bica.model;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.DocumentReference;

@Entity
public class Card {

    @PrimaryKey(autoGenerate = true)    // ID 자동생성
    private int roomId = 0;   // 하나의 사용자에 대한 고유 ID 값

    int image;
    String email;
    String company;
    String depart;
    String name;
    String position;
    String phone;
    String address;
    String occupation;
    String memo;

    public Card(int roomId, int image, String company, String depart, String name, String position, String phone, String email, String address, String occupation, String memo){
        this.roomId = roomId;
        this.image = image;
        this.company = company;
        this.depart = depart;
        this.name = name;
        this.position = position;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.occupation = occupation;
        this.memo = memo;
    }
    public Card(){

    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}