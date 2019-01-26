package com.example.sanmyintoo.testapplicaiton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {


    FirebaseAuth mAuth;
    TextView EventName, EventDescription, EventDate, EventTime, EventPhotoUrl, Latitude, Longitude, Member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mAuth = FirebaseAuth.getInstance();

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            String address = extras.getString("Address");
//            LatLng latlng = extras.getParcelable("latlng");
//
//
//                    Toast.makeText(getApplicationContext(), "Data: " + address +"\n" + latlng.latitude + "\n" + latlng.longitude, Toast.LENGTH_SHORT).show();
//        }

        EventName = (TextView) findViewById(R.id.EventName);
        EventDescription = (TextView) findViewById(R.id.EventDescription);
        EventDate = (TextView) findViewById(R.id.EventDate);
        EventTime = (TextView) findViewById(R.id.EventTime);
        EventPhotoUrl = (TextView) findViewById(R.id.EventPhotoUrl);
        Latitude = (TextView) findViewById(R.id.Latitude);
        Longitude = (TextView) findViewById(R.id.Longitude);
        Member = (TextView) findViewById(R.id.members);

ShowData();

        findViewById(R.id.save_Btn).setOnClickListener(this);

    }

    private void ShowData() {
        Bundle extras = getIntent().getExtras();
        String eventName = extras.getString("EventName");
        String eventDes = extras.getString("EventDes");
        String url = extras.getString("ImageUrl");
        String date = extras.getString("Date");
        String time = extras.getString("Time");
        String Address = extras.getString("Address");
        LatLng latlng = extras.getParcelable("latlng");
        ArrayList<String> member = extras.getStringArrayList("MemberList");


        EventName.setText(eventName);
        EventDescription.setText(eventDes);
        EventTime.setText(time);
        EventDate.setText(date);
        EventPhotoUrl.setText(url);
        Latitude.setText(Address);
        Longitude.setText(latlng.toString());
        Member.setText(member.toString());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_Btn:
              //  Save();
                break;
        }
    }

//    private void Save() {
//        final Event event = new Event(
//                Event_Variables.eventName,
//                Event_Variables.eventDescription,
//                Event_Variables.eventDate,
//                Event_Variables.eventTime,
//                Event_Variables.eventPhotoUrl,
//                Event_Variables.latitude,
//                Event_Variables.longitude
//        );
//        final EventMember eventmember = new EventMember(
//                Event_Variables.Acceptedmembers,
//                Event_Variables.Invitedmembers
//        );
//
//        final String eventID = "" + System.currentTimeMillis();
//        final HashMap<String, String> groups = new HashMap<String, String>();
//        groups.put("GroupID", eventID);
//        FirebaseDatabase.getInstance().getReference("Events")
//                .child(eventID)
//                .setValue(event)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            FirebaseDatabase.getInstance().getReference("Events_Members")
//                            .child(eventID)
//                            .setValue(eventmember)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid() + "/Groups/Accepted").push()
//                                    .setValue(groups)
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if(task.isSuccessful()){
//                                                if(eventmember.invitedmember != null){
//                                                    ArrayList<String> invitedMember = eventmember.invitedmember;
//                                                    for(String a : invitedMember){
//                                                        String value = a;
//                                                        saveRequestedEvent(value, groups);
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    });
//                                }
//                            });
//                        }
//                    }
//                });
//
//
//    }
//    //        FirebaseDatabase.getInstance().getReference("Events")
////                .child(eventID)
////                .setValue(event)
////                .addOnCompleteListener(new OnCompleteListener<Void>() {
////                    @Override
////                    public void onComplete(@NonNull Task<Void> task) {
////                        if (task.isSuccessful()) {
////                            FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/Groups/Accepted").push()
////                                    .setValue(groups)
////                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
////                                        @Override
////                                        public void onComplete(@NonNull Task<Void> task) {
////                                            if (task.isSuccessful()) {
////                                                if (eventmember.invitedmember != null) {
////                                                   ArrayList<String> invitedMember = event.invitedmember;
////                                                    for (String a : invitedMember) {
////                                                        String value = a;
////                                                        saveRequestedEvent(value, groups);
////                                                    }
////                                                }
////                                            }
////                                        }
////                                    });
////                        }
////                    }
////                });
//
//    private void saveRequestedEvent(String value, HashMap<String, String> groups) {
//        FirebaseDatabase.getInstance().getReference("Users/" + value + "/Groups/RequestedGroups").push()
//                .setValue(groups)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
}
