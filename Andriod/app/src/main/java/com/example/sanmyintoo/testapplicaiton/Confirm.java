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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Confirm extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    RecyclerView recyclerView;
    ArrayList<User> Userlist;
    MyAdapter_MemberList adapter;
    TextView eventname, eventdate, eventtime, eventaddress;
    CircleImageView image;
    Button cancel, accept;
    Boolean checkResult = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerViewforMember);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        Userlist = new ArrayList<User>();

        eventname = (TextView) findViewById(R.id.event_name);
        eventdate = (TextView) findViewById(R.id.Date);
        eventtime = (TextView) findViewById(R.id.Time);
        eventaddress = (TextView) findViewById(R.id.Address);
        image = (CircleImageView) findViewById(R.id.event_image);

        accept = (Button) findViewById(R.id.acceptbtn);
        cancel = (Button) findViewById(R.id.cancelbtn);

        loadData();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Confirm.this);
                builder.setMessage("Are you sure you want to cancel?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(Confirm.this, Index.class);
                                startActivity(i);
                                Confirm.this.finish();
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

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Confirm.this);
                builder.setMessage("Are you sure want to create an event?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                save_event();
                                // Confirm.this.finish();
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

    }


    private void loadData() {
        Bundle extras = getIntent().getExtras();
        String eventName = extras.getString("EventName");
        String eventDes = extras.getString("EventDes");
        String url = extras.getString("ImageUrl");
        String date = extras.getString("Date");
        String time = extras.getString("Time");
        String Address = extras.getString("Address");
        LatLng latlng = extras.getParcelable("latlng");
        ArrayList<String> member = extras.getStringArrayList("MemberList");

        eventname.setText(eventName);
        eventdate.setText(date);
        eventtime.setText(time);
        eventaddress.setText(Address);
        Glide.with(this)
                .load(Uri.parse(url))
                .into(image);

        for (String userID : member) {
            String value = userID;
//                  member.add(value);
            FirebaseDatabase.getInstance().getReference().child("Users/" + value + "/UserData").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    User user = dataSnapshot.getValue(User.class);
                    Userlist.add(user);

                    adapter = new MyAdapter_MemberList(Confirm.this, Userlist);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void save_event() {
        final String eventID = "" + System.currentTimeMillis();
        Bundle extras = getIntent().getExtras();

        final String ename = extras.getString("EventName");
        String edes = extras.getString("EventDes");
        String eurl = extras.getString("ImageUrl");
        String edate = extras.getString("Date");
        String etime = extras.getString("Time");
        String eAddress = extras.getString("Address");
        LatLng elatlng = extras.getParcelable("latlng");
        ArrayList<String> emember = extras.getStringArrayList("MemberList");
        ArrayList<String> e_acceptedmember = new ArrayList<>();
        e_acceptedmember.add(mAuth.getCurrentUser().getUid());

        Double lat = elatlng.latitude;
        Double lng = elatlng.longitude;

        final Event event = new Event(
                ename,
                edes,
                edate,
                etime,
                eurl,
                lat,
                lng,
                eAddress,
                eventID
        );
        final EventMember eventmember = new EventMember(
                e_acceptedmember,
                emember

        );


        final HashMap<String, String> groups = new HashMap<String, String>();
        groups.put("EventID", eventID);

        final HashMap<String, String> acceptedmember = new HashMap<String, String>();
        acceptedmember.put("UserID", mAuth.getCurrentUser().getUid());

        final HashMap<String, String> invitedmember = new HashMap<String, String>();
        ArrayList<String> invitedMember = eventmember.invitedmember;
        for (String a : invitedMember) {
            invitedmember.put("UserID", a);
        }

        FirebaseDatabase.getInstance().getReference("Events")
                .child(eventID)
                .setValue(event)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(Confirm.this, "Event creating is sucess!!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Confirm.this, ChatRoom.class);
                            i.putExtra("EventID", eventID);
                            i.putExtra("EventName", ename);
                            startActivity(i);
                            finish();

                            FirebaseDatabase.getInstance().getReference("Events_Members")
                                    .child(eventID + "/acceptedmember").push()
                                    .setValue(acceptedmember)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            ArrayList<String> invitedMember = eventmember.invitedmember;
                                            for (String i : invitedMember) {
                                                final HashMap<String, String> invitedmember = new HashMap<String, String>();
                                                invitedmember.put("UserID", i);
                                                FirebaseDatabase.getInstance().getReference("Events_Members")
                                                        .child(eventID + "/invitedmember").push()
                                                        .setValue(invitedmember)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                            }
                                                        });
                                            }
                                            FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/Groups/Accepted").push()
                                                    .setValue(groups)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                if (eventmember.invitedmember != null) {
                                                                    ArrayList<String> invitedMember = eventmember.invitedmember;
                                                                    for (String a : invitedMember) {
                                                                        String value = a;
                                                                        saveRequestedEvent(value, groups);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                        }
                    }
                });
    }

    private void saveRequestedEvent(String value, HashMap<String, String> groups) {
        FirebaseDatabase.getInstance().getReference("Users/" + value + "/Groups/RequestedGroups").push()
                .setValue(groups)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            checkResult = true;
                        }
                    }
                });

    }

}
