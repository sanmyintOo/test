package com.example.sanmyintoo.testapplicaiton;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class Add_EventInformation extends AppCompatActivity {

    EditText eventName, eventDesception;
    ImageView eventImage;
    private static final int PICK_FROM_GALLERY = 2;
    String profileImageUrl;
    Uri uriProfileImage;
    ProgressBar progressBar;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__event_information);

        eventName = (EditText)findViewById(R.id.eventname);
        eventDesception = (EditText) findViewById(R.id.eventdes);
        eventImage = (ImageView) findViewById(R.id.img_chooser);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        saveBtn = (Button) findViewById(R.id.toDate);



        eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String event_name = eventName.getText().toString();
                String event_des = eventDesception.getText().toString();

                if (event_name.isEmpty()){
                    eventName.setError("Event name is empty !!");
                    eventName.requestFocus();
                    return;
                }
                if(event_des.isEmpty()){
                    eventDesception.setError("Event description is empty !! ");
                    eventDesception.requestFocus();
                    return;
                }
                if(profileImageUrl==null){
                    Toast.makeText(Add_EventInformation.this, "Please upload an image for your event", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(Add_EventInformation.this, Add_Date.class);

                intent.putExtra("EventName", event_name);
                intent.putExtra("EventDes", event_des);
                intent.putExtra("ImageUrl", profileImageUrl);

                startActivity(intent);
            }
        });
    }

    private void imageChooser() {
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
            eventImage.setImageBitmap(bitmap);

            uploadImagetoFirebaseStorage();
        }
    }

    private void uploadImagetoFirebaseStorage() {
        final StorageReference profileimageRef =
                FirebaseStorage.getInstance().getReference("EventImages/" + System.currentTimeMillis() + ".jpg");

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
                            Toast.makeText(Add_EventInformation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Add_EventInformation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void toDate(View view) {
        Intent intent = new Intent(this, Add_Date.class);
        startActivity(intent);
    }
}
