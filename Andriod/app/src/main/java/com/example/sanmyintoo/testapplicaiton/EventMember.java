package com.example.sanmyintoo.testapplicaiton;

import java.util.ArrayList;

public class EventMember {
    public ArrayList<String> acceptedmember = new ArrayList<>();
    public ArrayList<String> invitedmember = new ArrayList<>();

    public EventMember(){

    }

    public EventMember(ArrayList<String> acceptedmember, ArrayList<String> invitedmember) {
        this.acceptedmember = acceptedmember;
        this.invitedmember = invitedmember;
    }

    public ArrayList<String> getAcceptedmember() {
        return acceptedmember;
    }

    public ArrayList<String> getInvitedmember() {
        return invitedmember;
    }
}

