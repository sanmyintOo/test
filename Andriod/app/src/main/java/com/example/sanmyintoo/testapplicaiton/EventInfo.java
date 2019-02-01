package com.example.sanmyintoo.testapplicaiton;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventInfo extends AppCompatActivity {

    LinearLayout event_detail, leaveBtn;
    TextView eventName;
    FirebaseAuth mAuth;
    String key, groupID, groupname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        Bundle extras = getIntent().getExtras();
        groupID = extras.getString("EventID");
        groupname = extras.getString("EventName");

        event_detail = (LinearLayout) findViewById(R.id.event_detail);
        leaveBtn = (LinearLayout) findViewById(R.id.leaveBtn);
        eventName = (TextView) findViewById(R.id.EVENT_NAME);
        mAuth = FirebaseAuth.getInstance();
        eventName.setText(groupname);

        event_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventInfo.this, activity_event_detail.class);
                i.putExtra("EventID", groupID);
                i.putExtra("EventName", groupname);
               startActivity(i);
            }
        });

        leaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EventInfo.this);
                builder.setMessage("Are you sure you want to leave?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                delete();
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

    private void delete() {
        FirebaseDatabase.getInstance().getReference().child("Events_Members/" + groupID + "/acceptedmember")
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

        FirebaseDatabase.getInstance().getReference().child("Users/"+ mAuth.getCurrentUser().getUid() + "/Groups/Accepted")
                .orderByChild("EventID")
                .equalTo(groupID)
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

        Intent i = new Intent(EventInfo.this, Index.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(EventInfo.this, ChatRoom.class);
        i.putExtra("EventID", groupID);
        i.putExtra("EventName", groupname);
        startActivity(i);
    }
}
