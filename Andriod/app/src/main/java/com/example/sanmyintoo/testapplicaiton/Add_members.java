package com.example.sanmyintoo.testapplicaiton;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Add_members extends AppCompatActivity {

    FirebaseAuth mAuth;

    private EditText searchText;
    private ImageView searchBtn;
    RecyclerView mSearchResult;
    private DatabaseReference muserDb;
    ArrayList<String> invitedmember = new ArrayList<>();
    Button continueBtn;
    String eventName, eventDes, url, date,time, address;
    LatLng Latlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);

        muserDb = FirebaseDatabase.getInstance().getReference("UserDataForSearch");
        mAuth = FirebaseAuth.getInstance();


        searchText = (EditText) findViewById(R.id.searchText);
        searchBtn = (ImageView) findViewById(R.id.searchBtn);
        mSearchResult = (RecyclerView) findViewById(R.id.searchlist);
        mSearchResult.setLayoutManager(new LinearLayoutManager(this));
        continueBtn = (Button) findViewById(R.id.continuebtn);

        Bundle extras = getIntent().getExtras();
        eventName = extras.getString("EventName");
        eventDes = extras.getString("EventDes");
        url = extras.getString("ImageUrl");
        date = extras.getString("Date");
        time = extras.getString("Time");
        address = extras.getString("Address");
        Latlng = extras.getParcelable("latlng");


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchname = searchText.getText().toString();
                firebaseuserSearch(searchname);
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_members.this, Confirm.class);
                intent.putExtra("EventName", eventName);
                intent.putExtra("EventDes", eventDes);
                intent.putExtra("ImageUrl", url);
                intent.putExtra("Date", date);
                intent.putExtra("Time", time);
                intent.putExtra("Address", address);
                intent.putExtra("latlng",Latlng);
                intent.putStringArrayListExtra("MemberList", invitedmember);

                startActivity(intent);
            }
        });

    }

    private void firebaseuserSearch(String searchname) {

        Query firebaseSearchQuery = muserDb.orderByChild("username").startAt(searchname).endAt(searchname + "\uf8ff");
        FirebaseRecyclerOptions<User> u = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(firebaseSearchQuery, User.class)
                .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(u) {

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listlayout, viewGroup, false);

                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final UserViewHolder holder, int position, @NonNull final User model) {
                holder.username.setText(model.getUsername());
                Glide.with(getApplicationContext()).load(model.getProfileUrl()).into(holder.profilepic);

                holder.invitebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean checkUserid = true;
                        if (!model.getUserID().equals(null)) {
                            if (invitedmember.contains(model.getUserID())) {
                                Toast.makeText(Add_members.this, "User is already there .. ", Toast.LENGTH_SHORT).show();
                            } else if (mAuth.getCurrentUser().getUid().equals(model.getUserID())) {
                                Toast.makeText(Add_members.this, "You can't invite yourself dude", Toast.LENGTH_SHORT).show();
                            } else {
                                invitedmember.add(model.getUserID());
                            }
                        } else {
                            Toast.makeText(Add_members.this, "User ID is null .. ", Toast.LENGTH_LONG).show();
                        }
                        Toast.makeText(Add_members.this, invitedmember.toString(), Toast.LENGTH_LONG).show();
                    }
                });


//               holder.itemView.setOnClickListener(new View.OnClickListener() {
//                   @Override
//                   public void onClick(View v) {
//                       AlertDialog.Builder builder = new AlertDialog.Builder(Add_members.this);
//                       builder.setMessage("Are you sure you want to invite?")
//                               .setCancelable(false)
//                               .setPositiveButton("Invite", new DialogInterface.OnClickListener() {
//                                   public void onClick(DialogInterface dialog, int id) {
//                                      // Toast.makeText(Add_members.this, model.getUserID(), Toast.LENGTH_LONG).show();
//                                       invitedmember.add(model.getUserID());
//                                       Toast.makeText(Add_members.this, invitedmember.toString(), Toast.LENGTH_LONG).show();
//                                   }
//                               })
//                               .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                   public void onClick(DialogInterface dialog, int id) {
//                                       dialog.cancel();
//                                   }
//                               });
//                       AlertDialog alert = builder.create();
//                       alert.show();
//                   }
//               });

            }
        };
        mSearchResult.setAdapter(adapter);
        adapter.startListening();

    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public CircleImageView profilepic;
        public Button invitebtn;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.userName);
            profilepic = (CircleImageView) itemView.findViewById(R.id.profilepic);
            invitebtn = (Button) itemView.findViewById(R.id.invite_btn);
        }

    }


    public void toConfirm(View view) {
        Intent intent = new Intent(this, Confirm.class);
        startActivity(intent);
    }
}
