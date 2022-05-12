package com.example.bica;

import java.util.ArrayList;

public class ChipData {

    private ArrayList<String> chip = new ArrayList<>();
    private String userEmail;

    public ArrayList<String> getChip() {
        return chip;
    }

    public void setChip(ArrayList<String> chip) {
        this.chip = chip;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

}
