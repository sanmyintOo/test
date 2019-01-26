package com.example.sanmyintoo.testapplicaiton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TimePicker;

public class add_time extends AppCompatActivity {

    TimePicker mTime;
    String eventName, eventDes, url, date,time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time);
        mTime = (TimePicker) findViewById(R.id.timepicker);

        Bundle extras = getIntent().getExtras();
        eventName = extras.getString("EventName");
        eventDes = extras.getString("EventDes");
        url = extras.getString("ImageUrl");
        date = extras.getString("Date");
        mTime.is24HourView();

        mTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//              time = hourOfDay + ":" + minute;

                String status = "AM";

                if(hourOfDay > 11)
                {
                    status = "PM";
                }
                int hour_of_12_hour_format;

                if(hourOfDay > 11){
                    hour_of_12_hour_format = hourOfDay - 12;
                }
                else {
                    hour_of_12_hour_format = hourOfDay;
                }
                time = hour_of_12_hour_format + " : " + minute + " " + status;

            }
        });
    }
    public void toMap(View view) {
        Intent intent = new Intent(this, Add_Location.class);

        intent.putExtra("EventName", eventName);
        intent.putExtra("EventDes", eventDes);
        intent.putExtra("ImageUrl", url);
        intent.putExtra("Date", date);
        intent.putExtra("Time", time);

        startActivity(intent);
    }
}
