package com.example.sanmyintoo.testapplicaiton;

import java.util.ArrayList;

public class Event_Variables {
    public static String eventName;
    public static String eventDescription;
    public static String eventPhotoUrl;
    public static String eventDate;
    public static String eventTime;
    public static String latitude;
    public static String longitude;
//    public static HashMap<String, String> Acceptedmembers = new HashMap<String, String>();
//    public static HashMap<String, String> Inviteddmembers = new HashMap<String, String>();

    public static ArrayList<String> Acceptedmembers = new ArrayList<>();
    public static ArrayList<String> Invitedmembers = new ArrayList<>();


    public Event_Variables() {
    }

    public static String getEventName() {
        return eventName;
    }

    public static void setEventName(String eventName) {
        Event_Variables.eventName = eventName;
    }

    public static String getEventDescription() {
        return eventDescription;
    }

    public static void setEventDescription(String eventDescription) {
        Event_Variables.eventDescription = eventDescription;
    }

    public static String getEventPhotoUrl() {
        return eventPhotoUrl;
    }

    public static void setEventPhotoUrl(String eventPhotoUrl) {
        Event_Variables.eventPhotoUrl = eventPhotoUrl;
    }

    public static String getEventDate() {
        return eventDate;
    }

    public static void setEventDate(String eventDate) {
        Event_Variables.eventDate = eventDate;
    }

    public static String getEventTime() {
        return eventTime;
    }

    public static void setEventTime(String eventTime) {
        Event_Variables.eventTime = eventTime;
    }

    public static String getLatitude() {
        return latitude;
    }

    public static void setLatitude(String latitude) {
        Event_Variables.latitude = latitude;
    }

    public static String getLongitude() {
        return longitude;
    }

    public static void setLongitude(String longitude) {
        Event_Variables.longitude = longitude;
    }

    public static ArrayList<String> getAcceptedmembers() {
        return Acceptedmembers;
    }

    public static void setAcceptedmembers(ArrayList<String> acceptedmembers) {
        Acceptedmembers = acceptedmembers;
    }

    public static ArrayList<String> getInvitedmembers() {
        return Invitedmembers;
    }

    public static void setInvitedmembers(ArrayList<String> invitedmembers) {
        Invitedmembers = invitedmembers;
    }
}
