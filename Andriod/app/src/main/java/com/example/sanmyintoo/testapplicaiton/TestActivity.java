package com.example.sanmyintoo.testapplicaiton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TestActivity extends AppCompatActivity {
    ImageView profileImage;
    EditText username;
    FirebaseAuth mAuth;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mAuth = FirebaseAuth.getInstance();
        profileImage = (ImageView) findViewById(R.id.TestprofileImageView);
        username = (EditText) findViewById(R.id.TestusernameText);
        text = (TextView)findViewById(R.id.TestText);

        loadUserInformation();
    }

    private void loadUserInformation() {
        FirebaseUser user = mAuth.getCurrentUser();


        if(user != null){
            if(user.getPhotoUrl() != null) {
                text.setText(user.getPhotoUrl().toString());
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(profileImage);
            }else {
                Toast.makeText(TestActivity.this, "No photo url",Toast.LENGTH_SHORT).show();
            }
            if(user.getDisplayName() != null){
                username.setText(user.getDisplayName());
            }
            else{
                Toast.makeText(TestActivity.this, "No display name",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(TestActivity.this, "No user",Toast.LENGTH_SHORT).show();
        }
    }
}
