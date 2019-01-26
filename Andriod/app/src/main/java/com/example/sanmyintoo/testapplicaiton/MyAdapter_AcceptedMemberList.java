package com.example.sanmyintoo.testapplicaiton;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter_AcceptedMemberList extends RecyclerView.Adapter<MyAdapter_AcceptedMemberList.MyViewHolder>{
    Context context;
    ArrayList<User> users;

    public MyAdapter_AcceptedMemberList(Context c, ArrayList<User> e){
        context = c;
        users = e;
    }
    @NonNull
    @Override
    public MyAdapter_AcceptedMemberList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyAdapter_AcceptedMemberList.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardviewformemberslist,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter_AcceptedMemberList.MyViewHolder myViewHolder, int i) {
        myViewHolder.accepted.setText(users.get(i).getUsername());
//        myViewHolder.requested.setText(users.get(i).getProfileUrl());
        Glide.with(this.context)
                .load(Uri.parse(users.get(i).getProfileUrl()))
                .into(myViewHolder.image);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView accepted, requested;
        CircleImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            accepted = (TextView) itemView.findViewById(R.id.accepted);
            image= (CircleImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
