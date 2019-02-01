package com.example.sanmyintoo.testapplicaiton;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ChatRoom extends AppCompatActivity {

    TextView groupName, Message;

    String groupID, groupname, currentUserID, currentUserName, currentDate, currentTime;
    ImageView send;
    EditText sendText;
    RecyclerView myRecyclerView;
    MessageAdapter adapter;
    ArrayList<Message> messagelist = new ArrayList<Message>();
    LinearLayoutManager linearLayoutManager;

    FirebaseAuth mAuth;
    DatabaseReference GroupNameRef, GroupMessageKeyRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);



       // myRecyclerView.setHasFixedSize(true);

        Bundle extras = getIntent().getExtras();
        groupID = extras.getString("EventID");
        groupname = extras.getString("EventName");

        groupName = (TextView) findViewById(R.id.groupname);
        send = (ImageView) findViewById(R.id.send_btn);
        sendText = (EditText) findViewById(R.id.inputext);
//        Message = (TextView) findViewById(R.id.text_message);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        currentUserName = mAuth.getCurrentUser().getDisplayName();

        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Message").child(groupID);

        inalize();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMessage();
                sendText.setText("");
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        GroupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {

                    Message m = dataSnapshot.getValue(Message.class);
                    messagelist.add(m);
                    adapter.notifyDataSetChanged();

                    myRecyclerView.smoothScrollToPosition(myRecyclerView.getAdapter().getItemCount());

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void inalize() {
        groupName.setText(groupname);
        adapter = new MessageAdapter(ChatRoom.this, messagelist);
        myRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRecyclerView.setAdapter(adapter);
    }

    private void SaveMessage() {
        String inputmessage = sendText.getText().toString();
        String messageKey = GroupNameRef.push().getKey();

        if (TextUtils.isEmpty(inputmessage)) {
            Toast.makeText(this, "Please write message first..", Toast.LENGTH_SHORT).show();
        } else {
            Calendar ccalfordate = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            currentDate = currentDateFormat.format(ccalfordate.getTime());


            Calendar ccalfortime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormat.format(ccalfordate.getTime());

            HashMap<String, Object> groupmessageKey = new HashMap<>();
            GroupNameRef.updateChildren(groupmessageKey);

            GroupMessageKeyRef = GroupNameRef.child(messageKey);

            String UserProfile = mAuth.getCurrentUser().getPhotoUrl().toString();

            HashMap<String, Object> messageInfo = new HashMap<>();
            messageInfo.put("Name", currentUserName);
            messageInfo.put("Message", inputmessage);
            messageInfo.put("Date", currentDate);
            messageInfo.put("Time", currentTime);
            messageInfo.put("PhotoUrl", UserProfile);

            GroupMessageKeyRef.updateChildren(messageInfo);

        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,Index.class);
        startActivity(i);
    }

    public void toIndex(View view) {
        Intent intent = new Intent(this, Index.class);
        startActivity(intent);
    }

    public void event_detail(View view) {
        Intent intent = new Intent(this, EventInfo.class);
        intent.putExtra("EventID", groupID);
        intent.putExtra("EventName", groupname);
        startActivity(intent);
    }

}
