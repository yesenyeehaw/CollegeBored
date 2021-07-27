/*
 * This class handles all of a user's attributes and displays it on a fragment
 */
package com.codepath.collegebored.fragments;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.collegebored.R;
import com.codepath.collegebored.activities.LoginActivity;
import com.parse.ParseUser;


public class ProfileFragment extends Fragment {
    public static final String TAG = "ProfileFragment";
    Button btnLogOut;
    TextView tvUsername;
    TextView tvHighSchool;
    TextView tvGPA;
    TextView tvSAT;
    TextView tvACT;
    TextView tvExtracurriculars;

    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(false);
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ParseUser currentUser = ParseUser.getCurrentUser();
        tvUsername = view.findViewById(R.id.tvUsername);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        tvHighSchool = view.findViewById(R.id.tvHighSchool);
        tvGPA = view.findViewById(R.id.tvGPA);
        tvSAT = view.findViewById(R.id.tvSAT);
        tvACT = view.findViewById(R.id.tvACT);
        tvExtracurriculars = view.findViewById(R.id.tvExtracurriculars);

        tvUsername.setText((CharSequence) currentUser.get("username"));
        tvHighSchool.setText("Highschool: " + currentUser.get("HIGH_SCHOOL"));
        tvGPA.setText("GPA: " + currentUser.get("GPA").toString());
        tvSAT.setText("SAT: " + currentUser.get("SAT"));
        tvACT.setText("ACT: " + currentUser.get("ACT"));
        tvExtracurriculars.setText("Extracurriculars: " + currentUser.get("Extracurriculars"));



        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                goLoginActivity();
            }
        });

    }
    private void goLoginActivity(){
        Intent i = new Intent(getContext(), LoginActivity.class);
        startActivity(i);
    }

}