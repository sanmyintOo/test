package com.example.sanmyintoo.testapplicaiton;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class activity_event_detail extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button acceptBtn, cancelBtn;

    CircleImageView Event_pic;
    TextView Event_name, Event_date, Event_time, Event_address;
    RecyclerView recyclerView, recyclerViewAcceptedMember;
    ArrayList<User> Userlist;
    ArrayList<User> UserlistAccepted;
    MyAdapter_MemberList adapter;
    MyAdapter_AcceptedMemberList accepted_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Bundle extras = getIntent().getExtras();
        final String groupID = extras.getString("EventID");
        final String groupname = extras.getString("EventName");

        mAuth = FirebaseAuth.getInstance();
        Event_pic = (CircleImageView) findViewById(R.id.event_image_detail);
        Event_name = (TextView) findViewById(R.id.event_name_detail);
        Event_date = (TextView) findViewById(R.id.Date_detail);
        Event_time = (TextView) findViewById(R.id.Time_detail);
        Event_address = (TextView) findViewById(R.id.Address_detail);


        loaddata();
        loadInvitedMember();
    }

    private void loadInvitedMember() {
        recyclerViewAcceptedMember = (RecyclerView) findViewById(R.id.myRecyclerViewforMemberaccepted_eventDetail);
        recyclerViewAcceptedMember.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAcceptedMember.setHasFixedSize(true);
        UserlistAccepted = new ArrayList<User>();

        Bundle extras = getIntent().getExtras();
        final String eventid = extras.getString("EventID");


        FirebaseDatabase.getInstance().getReference().child("Events_Members/" + eventid + "/acceptedmember")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String value = ds.child("UserID").getValue().toString();
                            FirebaseDatabase.getInstance().getReference().child("Users/" + value + "/UserData").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    User user = dataSnapshot.getValue(User.class);
                                    UserlistAccepted.add(user);

                                    accepted_adapter = new MyAdapter_AcceptedMemberList(activity_event_detail.this,UserlistAccepted);
                                    recyclerViewAcceptedMember.setAdapter(accepted_adapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loaddata() {
        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerViewforMember_eventDetail);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        Userlist = new ArrayList<User>();
        Bundle extras = getIntent().getExtras();
        final String eventid = extras.getString("EventID");



        FirebaseDatabase.getInstance().getReference().child("Events/" + eventid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Event_Retrieve e = dataSnapshot.getValue(Event_Retrieve.class);

                if(!e.equals(null)){
                    Glide.with(activity_event_detail.this)
                            .load(Uri.parse(e.eventphotourl))
                            .into(Event_pic);
                    Event_name.setText(e.getEventname());
                    Event_date.setText(e.getEventdate());
                    Event_time.setText(e.getEventtime());
                    Event_address.setText(e.getAddress());
                }
                FirebaseDatabase.getInstance().getReference().child("Events_Members/" + eventid + "/invitedmember")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    String value = ds.child("UserID").getValue().toString();
                                    FirebaseDatabase.getInstance().getReference().child("Users/" + value + "/UserData").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            User user = dataSnapshot.getValue(User.class);
                                            Userlist.add(user);

                                            adapter = new MyAdapter_MemberList(activity_event_detail.this,Userlist);
                                            recyclerView.setAdapter(adapter);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
