package com.example.sanmyintoo.testapplicaiton;

public class Message {
    private String Name, Message, Date, Time, PhotoUrl;

    public Message(){

    }

    public Message(String Name, String Message, String Date, String Time, String PhotoUrl) {
        this.Name = Name;
        this.Message = Message;
        this.Date = Date;
        this.Time = Time;
        this.PhotoUrl = PhotoUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }
}
