package com.example.sanmyintoo.testapplicaiton;

import java.util.ArrayList;

public class EventMember_retreve {
    public String acceptedmember;
    public String invitedmember;

    public EventMember_retreve(){

    }

    public EventMember_retreve(String acceptedmember, String invitedmember) {
        this.acceptedmember = acceptedmember;
        this.invitedmember = invitedmember;
    }

    public String getAcceptedmember() {
        return acceptedmember;
    }

    public String getInvitedmember() {
        return invitedmember;
    }
}
