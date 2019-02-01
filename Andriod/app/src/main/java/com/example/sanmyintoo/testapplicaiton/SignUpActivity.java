package com.example.sanmyintoo.testapplicaiton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText passwordTextConfirm, emailText, passwordText;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;


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

    public void toLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_button:
                sendUserData();
                break;
        }
    }

    private void sendUserData() {
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String paaswordConfirm = passwordTextConfirm.getText().toString().trim();

        if (email.isEmpty()) {
            emailText.setError("Email is required");
            emailText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordText.setError("Password is required");
            passwordText.requestFocus();
            return;
        }
        if(!password.equals(paaswordConfirm)){
            passwordTextConfirm.setError("Password do not match!");
            passwordTextConfirm.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Enter a valid email");
            emailText.requestFocus();
            return;
        }

        Intent intent = new Intent(this, ProfileUploadActivity.class);

        intent.putExtra("EMAIL", email);
        intent.putExtra("PASSWORD", password);

        startActivity(intent);
        finish();

    }
}
