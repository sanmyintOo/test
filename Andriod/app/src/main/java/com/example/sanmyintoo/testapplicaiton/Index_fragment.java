package com.example.sanmyintoo.testapplicaiton;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
public class Index_fragment extends Fragment implements View.OnClickListener {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private ArrayList<String> groupId;
    private ArrayList<String> groupIdReq;
    View fragmentView;
    TextView groupid, information, information_;
    String value;
    RecyclerView recyclerView, recyclerViewRequest;
    ArrayList<Event_Retrieve> list;
    ArrayList<Event_Retrieve> listrequested;


    public Index_fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth= FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        fragmentView = inflater.inflate(R.layout.fragment_index_fragment, container, false);

//        groupid = (TextView) fragmentView.findViewById(R.id.eventID);
        information = (TextView) fragmentView.findViewById(R.id.information);
        information_ = (TextView) fragmentView.findViewById(R.id.information_);

        ImageView addevent = (ImageView) fragmentView.findViewById(R.id.fab);
        addevent.setOnClickListener(
                new ImageView.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getContext(), Add_EventInformation.class);
                        startActivity(i);
                    }
                }
        );

        LoadrequestedEvents();
        LoadUpcomingEvents();

        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void LoadUpcomingEvents() {
        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.myRecyclerView_);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<Event_Retrieve>();
        groupId = new ArrayList<>();

//      Retreving accepted event list for current user
        FirebaseDatabase.getInstance().getReference().child("Users/" + mUser.getUid() + "/Groups/Accepted").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    value = ds.child("EventID").getValue().toString();
                    groupId.add(value);
                }
                retrieveGroups(groupId);

                if (groupId.isEmpty()){
                    information_.setVisibility(View.VISIBLE);
                }
            }

            private void retrieveGroups(ArrayList<String> groupId) {
                if (groupId != null) {
                    for (String value : groupId) {
                        FirebaseDatabase.getInstance().getReference().child("Events/" + value).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Event_Retrieve e = dataSnapshot.getValue(Event_Retrieve.class);
                                list.add(e);

                                MyAdapterUpcomingEvents adapter = new MyAdapterUpcomingEvents(Index_fragment.this.getActivity(), list);
                                recyclerView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(Index_fragment.this.getActivity(), "oops", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void LoadrequestedEvents() {

        recyclerViewRequest = (RecyclerView) fragmentView.findViewById(R.id.myRecyclerView_requested_);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerViewRequest.setLayoutManager(layoutManager);
        listrequested = new ArrayList<Event_Retrieve>();
        groupIdReq = new ArrayList<>();


//      Retreving requested event list for current user
        FirebaseDatabase.getInstance().getReference().child("Users/" + mUser.getUid() + "/Groups/RequestedGroups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    value = ds.child("EventID").getValue().toString();
                    groupIdReq.add(value);
                }

                retrieveGroups(groupIdReq);
                if (groupIdReq.isEmpty()){
                    information.setVisibility(View.VISIBLE);
                }
            }

            private void retrieveGroups(ArrayList<String> groupId) {
                if (groupId != null) {
                    for (String value : groupId) {
                        FirebaseDatabase.getInstance().getReference().child("Events/" + value).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Event_Retrieve e = dataSnapshot.getValue(Event_Retrieve.class);

                                listrequested.add(e);

                                MyAdapter adapter = new MyAdapter(Index_fragment.this.getActivity(), listrequested);
                                recyclerViewRequest.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(Index_fragment.this.getActivity(), "oops", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        }
    }

