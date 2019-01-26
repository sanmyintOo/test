package com.example.sanmyintoo.testapplicaiton;

public class Event_Retrieve {

    public String eventdate;
    public String eventdescription;
    public String eventname;
    public String eventphotourl;
    public String eventtime;
    public Double latitude;
    public Double longitude;
    public String address;
    public String eventid;



//    public ArrayList<String> acceptedmembers;
//    public ArrayList<String> invitedmembers;

    public Event_Retrieve(){

    }

    public Event_Retrieve( String eventdate, String eventdescription, String eventname, String eventphotourl, String eventtime, Double latitude, Double longitude, String address, String eventid) {

        this.eventdate = eventdate;
        this.eventdescription = eventdescription;
        this.eventname = eventname;
        this.eventphotourl = eventphotourl;
        this.eventtime = eventtime;
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
