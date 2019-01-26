package com.example.sanmyintoo.testapplicaiton;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapterUpcomingEvents extends RecyclerView.Adapter<MyAdapterUpcomingEvents.MyViewHolder>{
    Context context;
    ArrayList<Event_Retrieve> events;

    public MyAdapterUpcomingEvents(Context c, ArrayList<Event_Retrieve> e){
        context = c;
        events = e;
    }
    @NonNull
    @Override
    public MyAdapterUpcomingEvents.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyAdapterUpcomingEvents.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterUpcomingEvents.MyViewHolder holder, final int position) {

        if(events.size() != 0 ){

            holder.eventname.setText(events.get(position).getEventname());
            holder.eventdate.setText(events.get(position).getEventdate());
            holder.eventtime.setText(events.get(position).getEventtime());
            Glide.with(this.context).load(events.get(position).getEventphotourl()).into(holder.eventImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String eventid = events.get(position).getEventid().toString();
                    String eventname = events.get(position).getEventname();
                    Intent i = new Intent(context,ChatRoom.class);
                    i.putExtra("EventID", eventid);
                    i.putExtra("EventName", eventname);
                    context.startActivity(i);

                }
            });
        }
        else  {

        }


    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView eventname, eventtime, eventdate;
        ImageView eventImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            eventname = (TextView) itemView.findViewById(R.id.Event_Name);
            eventtime = (TextView) itemView.findViewById(R.id.Event_Time);
            eventdate = (TextView) itemView.findViewById(R.id.Event_Date);
            eventImage = (ImageView) itemView.findViewById(R.id.eventpic);
        }
    }

}
