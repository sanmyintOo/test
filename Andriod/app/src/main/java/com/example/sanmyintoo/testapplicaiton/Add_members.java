package com.example.sanmyintoo.testapplicaiton;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Add_members extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);
    }
    public void toConfirm(View view) {
        Intent intent = new Intent(this, Confirm.class);
        startActivity(intent);
    }
}
