package com.codepath.collegebored;
/*
* When user clicks on list item in Searchfragment it takes them to this fragment to show school details
 */
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.collegebored.models.School;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

public class SchoolDetailsFragment extends Fragment {

    TextView tvSchoolNameDetails;
    School currentSchool;

    public SchoolDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_school_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvSchoolNameDetails = view.findViewById(R.id.tvSchoolNameDetails);
        Bundle bundle = this.getArguments();
        String data = bundle.getString("key");
        tvSchoolNameDetails.setText(data);
    }
}