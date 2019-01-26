package com.example.sanmyintoo.testapplicaiton;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

public class Add_Date extends AppCompatActivity {

    CalendarView calendarView;
    TextView calendar_date;
    String eventName, eventDes, url, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__date);

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendar_date = (TextView) findViewById(R.id.calendar_date);

        Bundle extras = getIntent().getExtras();
        eventName = extras.getString("EventName");
        eventDes = extras.getString("EventDes");
        url = extras.getString("ImageUrl");


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                String[] monthNames = {"JAN", "FEB", "MAR", "Aprial", "May", "JUN", "JUL", "August", "SEP", "OCT", "NOV", "DEC"};
                String month_name = monthNames[month];

                date = dayOfMonth + " " + month_name + " " + year;
                calendar_date.setText(date);

            }
        });
    }

    public void toTimePicker(View view) {
        Intent intent = new Intent(this, add_time.class);

        intent.putExtra("EventName", eventName);
        intent.putExtra("EventDes", eventDes);
        intent.putExtra("ImageUrl", url);
        intent.putExtra("Date", date);

        startActivity(intent);
    }
}
