package com.example.sanmyintoo.testapplicaiton;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText passwordTextConfirm, emailText, passwordText;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;

    private void register(){

        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String password_confirm = passwordTextConfirm.getText().toString().trim();


        if (email.isEmpty()){
            emailText.setError("Email is required");
            emailText.requestFocus();
            return;
        }
        if (password.isEmpty()){
            passwordText.setError("Password is required");
            passwordText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailText.setError("Enter a valid email");
            emailText.requestFocus();
            return;
        }
        if(password.length()<8){
            passwordText.setError("Password should be 8 characters");
            passwordText.requestFocus();
            return;
        }

        progressbar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressbar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    finish();
                    Intent intent = new Intent(SignUpActivity.this, ProfileUploadActivity.class);
                    startActivity(intent);
                    Toast.makeText(SignUpActivity.this, getString(R.string.register_sucess),Toast.LENGTH_LONG).show();
                }else {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "This email is already registered", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        passwordTextConfirm = (EditText) findViewById(R.id.passwordTextConfirm);
        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        progressbar = findViewById(R.id.progressbar);
        progressbar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signup_button).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null ){

        }
    }

    //    public void toLogin(View view) {
//        Intent intent = new Intent(this, LoginActivity.class);
//        register();
//        startActivity(intent);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signup_button:
                register();
                break;

            case R.id.toLogin:
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
}
