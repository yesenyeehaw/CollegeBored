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
    EditText etGPA;
    EditText etSAT;
    EditText etACT;
    EditText etExtracurriculars;
    Button btnCreateAccount;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etName = findViewById(R.id.etName);
        etPass = findViewById(R.id.etPass);
        etGPA = findViewById(R.id.etGPA);
        etSAT = findViewById(R.id.etSAT);
        etACT = findViewById(R.id.etACT);
        etExtracurriculars = findViewById(R.id.etExtracurriculars);
        etHighSchool = findViewById(R.id.etHighSchool);

        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Sign Up pressed!");
                String username = etName.getText().toString();
                String password = etPass.getText().toString();
                String highSchool = etHighSchool.getText().toString();
                String strGPA = etGPA.getText().toString();
                double GPA = Double.parseDouble(strGPA);
                String strSAT = etSAT.getText().toString();
                int SAT = Integer.parseInt(strSAT);
                String strACT = etACT.getText().toString();
                int ACT = Integer.parseInt(strACT);
                String extracurriculars = etExtracurriculars.getText().toString();
                signUpUser(username, password, highSchool, GPA, SAT, ACT, extracurriculars);
                goMainActivity();
            }
        });

    }

    private void signUpUser (String username, String password, String highSchool, double GPA, int SAT, int ACT, String extracurriculars){
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        // Invoke signUpInBackground
        user.put("HIGH_SCHOOL", highSchool);
        user.put("GPA", GPA);
        user.put("SAT", SAT);
        user.put("ACT", ACT);
        user.put("Extracurriculars", extracurriculars);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    //Toast.makeText(SignUpActivity.this, "Issue with SignUp!" + e, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.getMessage());
                    return;
                }
                Toast.makeText(SignUpActivity.this, "Successfully Signed Up! You may now log in.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void goMainActivity () {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
