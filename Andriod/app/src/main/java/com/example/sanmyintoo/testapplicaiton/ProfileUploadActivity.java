package com.example.sanmyintoo.testapplicaiton;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfileUploadActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int CHOOSE_IMAGE = 101;
    private static final int PICK_FROM_GALLERY = 2;
    ImageView profileImage;
    EditText username;
    Uri uriProfileImage;
    ProgressBar progressBar, progressforsignup;
    String profileImageUrl;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_upload);

        mAuth = FirebaseAuth.getInstance();
        profileImage = (ImageView) findViewById(R.id.profileImageView);
        username = (EditText) findViewById(R.id.usernameText);
        progressBar = findViewById(R.id.progressbar);
        progressforsignup = findViewById(R.id.progressbarfosignup);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });


//        findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveUserInformation();
//            }
//        });

        findViewById(R.id.save_btn).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if(mAuth.getCurrentUser() == null ){
//            finish();
//            startActivity(new Intent(this, LoginActivity.class));
//        }
    }

//    Saving profile pic and user name

    private void saveUserInformation() {
        String displayName = username.getText().toString();
        if (displayName == null) {
            username.setError("Enter username");
            username.requestFocus();
            return;
        }
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null && profileImageUrl != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();
            progressforsignup.setVisibility(View.VISIBLE);
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressforsignup.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileUploadActivity.this, "Registered sucessfully and profile saved", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ProfileUploadActivity.this, Index.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(ProfileUploadActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (user == null) {
            Toast.makeText(ProfileUploadActivity.this, "No User", Toast.LENGTH_SHORT).show();
        } else if (profileImageUrl == null) {
            Toast.makeText(ProfileUploadActivity.this, "Profile Url is null", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ProfileUploadActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            profileImage.setImageBitmap(bitmap);

            uploadImagetoFirebaseStorage();

        }

    }


    private void uploadImagetoFirebaseStorage() {
        final StorageReference profileimageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");

        if (uriProfileImage != null) {
            progressBar.setVisibility(View.VISIBLE);
            profileimageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
//                    profileImageUrl = taskSnapshot.getStorage().getDownloadUrl().toString();
                    profileimageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profileImageUrl = uri.toString();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileUploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ProfileUploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }
    }

    private void showImageChooser() {
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //******code for crop image
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);

        try {
            intent.putExtra("return-data", true);
            startActivityForResult(
                    Intent.createChooser(intent, "Complete action using"),
                    PICK_FROM_GALLERY);
        } catch (ActivityNotFoundException e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_btn:
                Signup();
                break;
        }
    }

    private void Signup() {
        Intent intent = getIntent();
        String email = intent.getStringExtra("EMAIL");
        String password = intent.getStringExtra("PASSWORD");


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    final User user = new User(username.getText().toString());
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("Group")
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            finish();
                                            saveUserInformation();
                                        }
                                    }
                                });
                            }
                        }
                    });
//                    Toast.makeText(ProfileUploadActivity.this, getString(R.string.register_sucess),Toast.LENGTH_LONG).show();
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "This email is already registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
