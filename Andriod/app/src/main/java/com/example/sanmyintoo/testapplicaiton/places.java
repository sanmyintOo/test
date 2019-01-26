package com.example.sanmyintoo.testapplicaiton;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class places extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    View fragmentView;
    RecyclerView recyclerView;
    ArrayList<User> Userlist;
    MyAdapter_MemberList adapter;
    ArrayList<String> member = new ArrayList<String>();
    TextView a;
    ArrayList<String> member_data = new ArrayList<>();


    public places() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_places, container, false);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        a = (TextView) fragmentView.findViewById(R.id.place);
        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.myRecyclerViewforMembers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setHasFixedSize(true);
        Userlist = new ArrayList<User>();


        FirebaseDatabase.getInstance().getReference().child("Events_Members/1544412944912/invitedmember")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String value = ds.getValue().toString();
//                  member.add(value);
                            FirebaseDatabase.getInstance().getReference().child("Users/" + value + "/UserData").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                       User user = dataSnapshot.getValue(User.class);
                                       Userlist.add(user);

                                    adapter = new MyAdapter_MemberList(places.this.getActivity(),Userlist);
                                    recyclerView.setAdapter(adapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
