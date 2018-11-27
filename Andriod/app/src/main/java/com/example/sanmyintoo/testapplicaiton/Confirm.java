package com.example.sanmyintoo.testapplicaiton;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Confirm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
    }

    public void discardEvent(View view) {
        Intent intent = new Intent(this, Index.class);
        startActivity(intent);
    }
    public void toChatroom(View view) {
        Intent intent = new Intent(this, ChatRoom.class);
        startActivity(intent);
    }

    public void editDate(View view) {
        Intent intent = new Intent(this, Add_Date.class);
        startActivity(intent);
    }
    public void editLocation(View view) {
        Intent intent = new Intent(this, Add_Location.class);
        startActivity(intent);
    }
}
