package com.example.sanmyintoo.testapplicaiton;

public class Event {
    public String eventname;
    public String eventdescription;
    public String eventdate;
    public String eventtime;
    public String eventphotourl;
    public Double latitude;
    public Double longitude;
    public String address;
    public String eventid;

    public Event (){

    }

    public Event(String eventname, String eventdescription, String eventdate, String eventtime, String eventphotourl,Double latitude, Double longitude, String address, String eventid) {
        this.eventname = eventname;
        this.eventdescription = eventdescription;
        this.eventdate = eventdate;
        this.eventtime = eventtime;
        this.eventphotourl = eventphotourl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.eventid = eventid;
    }

    public String getEventname() {
        return eventname;
    }

    public String getEventdescription() {
        return eventdescription;
    }

    public String getEventdate() {
        return eventdate;
    }

    public String getEventtime() {
        return eventtime;
    }

    public String getEventphotourl() {
        return eventphotourl;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getEventid() {
        return eventid;
    }
}
