package com.example.sanmyintoo.testapplicaiton;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class eventdetail extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button acceptBtn, cancelBtn;

    CircleImageView Event_pic;
    TextView Event_name, Event_date, Event_time, Event_address;
    RecyclerView recyclerView, recyclerViewAcceptedMember;
    ArrayList<User> Userlist;
    ArrayList<User> UserlistAccepted;
    MyAdapter_MemberList adapter;
    MyAdapter_AcceptedMemberList accepted_adapter;
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventdetail);

        mAuth = FirebaseAuth.getInstance();
        Event_pic = (CircleImageView) findViewById(R.id.event_image_detail);
        Event_name = (TextView) findViewById(R.id.event_name_detail);
        Event_date = (TextView) findViewById(R.id.Date_detail);
        Event_time = (TextView) findViewById(R.id.Time_detail);
        Event_address = (TextView) findViewById(R.id.Address_detail);

        acceptBtn = (Button) findViewById(R.id.accept_btn);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(eventdetail.this);
                builder.setMessage("Are you sure want to join an event?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                save();
                                eventdetail.this.finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(eventdetail.this);
                builder.setMessage("Are you sure want to decline this event?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                delete();
                                eventdetail.this.finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        loaddata();
        loadInvitedMember();

    }

    private void delete() {
        Bundle extras = getIntent().getExtras();
        final String eventid = extras.getString("EventID");
        final String eventname = extras.getString("EventName");

        FirebaseDatabase.getInstance().getReference().child("Events_Members/" + eventid + "/invitedmember")
                .orderByChild("UserID")
                .equalTo(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                            key = childSnapshot.getKey();
                            childSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        FirebaseDatabase.getInstance().getReference().child("Users/"+ mAuth.getCurrentUser().getUid() + "/Groups/RequestedGroups")
                .orderByChild("EventID")
                .equalTo(eventid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                            String eventkey = childSnapshot.getKey();
                            childSnapshot.getRef().removeValue();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        Intent i = new Intent(this, Index.class);
        startActivity(i);


    }


    private void save() {
        Bundle extras = getIntent().getExtras();
        final String eventid = extras.getString("EventID");
        final String eventname = extras.getString("EventName");

        HashMap <String, String> data = new HashMap<String, String>();

        data.put("UserID", mAuth.getCurrentUser().getUid());
        FirebaseDatabase.getInstance().getReference()
                .child("Events_Members/"+eventid+"/acceptedmember").push()
                .setValue(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                HashMap <String, String> event = new HashMap<String, String>();
                event.put("EventID", eventid);
                FirebaseDatabase.getInstance().getReference()
                        .child("Users/"+ mAuth.getCurrentUser().getUid() + "/Groups/Accepted").push()
                        .setValue(event);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Events_Members/" + eventid + "/invitedmember")
                .orderByChild("UserID")
                .equalTo(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                            key = childSnapshot.getKey();
                            childSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("Users/"+ mAuth.getCurrentUser().getUid() + "/Groups/RequestedGroups")
                .orderByChild("EventID")
                .equalTo(eventid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                           String eventkey = childSnapshot.getKey();
                            childSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        Intent i = new Intent(this, ChatRoom.class);
        i.putExtra("EventID", eventid);
        i.putExtra("EventName", eventname);
        startActivity(i);

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

                                    accepted_adapter = new MyAdapter_AcceptedMemberList(eventdetail.this,UserlistAccepted);
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
                    Glide.with(eventdetail.this)
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

                                            adapter = new MyAdapter_MemberList(eventdetail.this,Userlist);
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
