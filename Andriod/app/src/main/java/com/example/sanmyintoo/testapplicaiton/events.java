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
public class events extends Fragment {

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


    public events() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_events, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        groupid = (TextView) fragmentView.findViewById(R.id.eventID);
        information = (TextView) fragmentView.findViewById(R.id.information);
        information_ = (TextView) fragmentView.findViewById(R.id.information_);

        LoadrequestedEvents();
        LoadUpcomingEvents();

        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void LoadUpcomingEvents() {
        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.myRecyclerView);

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

                                MyAdapterUpcomingEvents adapter = new MyAdapterUpcomingEvents(events.this.getActivity(), list);
                                recyclerView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(events.this.getActivity(), "oops", Toast.LENGTH_SHORT).show();
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

        recyclerViewRequest = (RecyclerView) fragmentView.findViewById(R.id.myRecyclerView_requested);

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

                                MyAdapter adapter = new MyAdapter(events.this.getActivity(), listrequested);
                                recyclerViewRequest.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(events.this.getActivity(), "oops", Toast.LENGTH_SHORT).show();
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
}
