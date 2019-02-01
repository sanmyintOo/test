package com.example.sanmyintoo.testapplicaiton;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class setting extends Fragment {

    View FragmentView;
    EditText username;
    CircleImageView profilepic;
    Uri uriProfileImage;
    FirebaseAuth mAuth;
    ProgressBar progressbar;
    String profileImageUrl;
    Button savechange;

    private static final int CHOOSE_IMAGE = 101;
    private static final int PICK_FROM_GALLERY = 2;

    public setting() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentView =  inflater.inflate(R.layout.fragment_setting, container, false);

        username = (EditText) FragmentView.findViewById(R.id.UserName);
        profilepic = (CircleImageView) FragmentView.findViewById(R.id.pic);
        mAuth = FirebaseAuth.getInstance();
        progressbar = (ProgressBar) FragmentView.findViewById(R.id.Progress_Bar);
        savechange = (Button) FragmentView.findViewById(R.id.savechange);

        profileImageUrl = mAuth.getCurrentUser().getPhotoUrl().toString();
        return FragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        username.setText(mAuth.getCurrentUser().getDisplayName());
        progressbar.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(mAuth.getCurrentUser().getPhotoUrl().toString())
                .into(profilepic);
        progressbar.setVisibility(View.GONE);

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        savechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), uriProfileImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            profilepic.setImageBitmap(bitmap);
            uploadImagetoFirebaseStorage();
        }

    }

    public void check(){
        String username_check = username.getText().toString();
        if (username_check.isEmpty()){
            username.setError("Enter username");
            username.requestFocus();
            return;
        }
        updateprofile(username_check);
    }

    private void uploadImagetoFirebaseStorage() {

        final StorageReference profileimageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");

        if (uriProfileImage != null) {
            progressbar.setVisibility(View.VISIBLE);
            profileimageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressbar.setVisibility(View.GONE);
                    profileimageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profileImageUrl = uri.toString();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(setting.this.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressbar.setVisibility(View.GONE);
                            Toast.makeText(setting.this.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void updateprofile(String displayname) {
        final String DisplayName = displayname;

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && profileImageUrl != null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(DisplayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    final String profileurl =  profileImageUrl;
                    final String username = DisplayName;
                    final String userID = mAuth.getCurrentUser().getUid();
                    final User user = new User(username,profileurl, userID);

                    FirebaseDatabase.getInstance().getReference("UserDataForSearch/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("UserData")
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(setting.this.getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(setting.this.getActivity(), Index.class);
                                    startActivity(i);
                                }
                            });
                        }
                    });
                }
            });
        }


    }
}
