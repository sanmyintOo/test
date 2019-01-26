package com.example.sanmyintoo.testapplicaiton;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    Context context;
    ArrayList<Message> messages;

    public MessageAdapter(Context c, ArrayList<Message> m){
        context = c;
        messages = m;
    }
    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.messagelayout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, final int position) {

        Glide.with(this.context).load(messages.get(position).getPhotoUrl()).into(holder.profilepic);
        holder.message.setText(messages.get(position).getMessage());
        holder.username.setText(messages.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView message, username;
       CircleImageView profilepic;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.message_);
            username = (TextView) itemView.findViewById(R.id.name_);
            profilepic = (CircleImageView) itemView.findViewById(R.id.profilepic_);

        }
    }
}
