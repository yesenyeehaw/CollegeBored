package com.codepath.collegebored.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.collegebored.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "SignUpActivtiy";
    EditText etName;
    EditText etPass;
    EditText etHighSchool;
    Button btnCreateAccount;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etName = findViewById(R.id.etName);
        etPass = findViewById(R.id.etPass);
        etHighSchool = findViewById(R.id.etHighSchool);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Sign Up pressed!");
                String username = etName.getText().toString();
                String password = etPass.getText().toString();
                String highSchool = etHighSchool.getText().toString();
                signUpUser(username, password, highSchool);
                goMainActivity();
            }
        });

    }

    private void signUpUser (String username, String password, String highSchool){
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        // Invoke signUpInBackground
        user.put("HIGH_SCHOOL", highSchool);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(SignUpActivity.this, "Issue with SignUp!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(SignUpActivity.this, "Successfully Signed Up! You may now log in.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void goMainActivity () {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
