package com.example.sanmyintoo.testapplicaiton;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Add_Location extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__location);
    }
    public void toSearchPeople(View view) {
        Intent intent = new Intent(this, Add_members.class);
        startActivity(intent);
    }
}
