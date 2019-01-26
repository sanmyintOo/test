package com.example.sanmyintoo.testapplicaiton;

import android.net.Uri;
import android.widget.EditText;

public class User {
    private String username,profileUrl, userID;

    public User() {

    }

    public User(String username, String profileUrl, String userID) {
        this.username = username;
        this.profileUrl = profileUrl;
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getUserID() {
        return userID;
    }
}
