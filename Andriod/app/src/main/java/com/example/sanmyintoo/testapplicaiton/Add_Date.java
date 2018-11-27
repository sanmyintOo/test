package com.example.sanmyintoo.testapplicaiton;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

public class Add_Date extends AppCompatActivity {

        CalendarView calendarView;
        TextView calendar_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__date);

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendar_date = (TextView) findViewById(R.id.calendar_date);
        

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                String date = dayOfMonth+ "/" + (month+1) + "/" + year;
                calendar_date.setText(date);
            }
        });
    }
    public void toTimePicker(View view) {
        Intent intent = new Intent(this, add_time.class);
        startActivity(intent);
    }
}
