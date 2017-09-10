package com.example.TulasProject;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Xaimy on 9/7/2017.
 */

public class User
{
    public String name;
    public String email;
    public String userLocation;
    public String time;
    public String DeviceID;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String email, String userLocation, String time, String DeviceID)
    {
        this.name = name;
        this.email = email;
        this.userLocation = userLocation;
        this.time = time;
        this.DeviceID = DeviceID;
    }
}