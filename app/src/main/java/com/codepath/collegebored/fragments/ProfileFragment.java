package com.codepath.collegebored.fragments;
/*
 * This class handles all of a user's attributes and displays it on a fragment
 */
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.collegebored.R;
import com.codepath.collegebored.activities.LoginActivity;
import com.parse.ParseUser;


public class ProfileFragment extends Fragment {
    Button btnLogOut;
    TextView tvHighSchool;

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
        btnLogOut = view.findViewById(R.id.btnLogOut);
        tvHighSchool = view.findViewById(R.id.tvHighSchool);
        tvHighSchool.setText((CharSequence) currentUser.get("HIGH_SCHOOL"));

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