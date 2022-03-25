package com.example.bica.model;

public class Card {

    int image;
    String company;
    String depart;
    String name;
    String position;
    String phone;
    String email;
    String address;
    String occupation;
    String groupname;
    String memo;

    public Card(int image, String company, String depart, String name, String position, String phone, String email, String address, String occupation, String groupname, String memo){
        this.image = image;
        this.company = company;
        this.depart = depart;
        this.name = name;
        this.position = position;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.occupation = occupation;
        this.groupname = groupname;
        this.memo = memo;
    }
    public Card(){

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

    public String getGroupname() { return groupname; }

    public void setGroupname(String groupname) { this.groupname = groupname; }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}