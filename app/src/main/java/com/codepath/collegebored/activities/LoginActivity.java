package com.codepath.collegebored.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.codepath.collegebored.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";
    Context context;
    EditText etUsername;
    EditText etPassword;
    Button btnLogin;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = LoginActivity.this;
        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        // check to see if theres a user already logged in
        if(ParseUser.getCurrentUser() != null){
            goMainActivity();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"Login pressed!" );
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SignUpActivity.class);
                context.startActivity(i);
            }
        });


    }
    private void loginUser(String username, String password) {
        Log.i(TAG, "Logging in..." + username);
        //navigate to the main activity if user has signed in properly
        ParseUser.logInInBackground(username, password, new LogInCallback() { //logInInBackground happens in the background thread
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) { // means something went wrong
                    Log.e(TAG, "!!!Issue with login!!!", e);
                    //TODO: create a toast based on if the user is missing username or password
                    Toast.makeText(LoginActivity.this, "Issue with login!", Toast.LENGTH_SHORT).show();
                    return;
                }
                goMainActivity();
                Toast.makeText(LoginActivity.this, "Successfully Logged in!", Toast.LENGTH_SHORT).show();
            }
        });
    }

        private void goMainActivity () {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
}
