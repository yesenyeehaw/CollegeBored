package com.codepath.collegebored.fragments;
/*
 * This class handles all of a user's liked schools and will display them all in
 * a recycler view on the fragment
 */
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.collegebored.R;
import com.codepath.collegebored.SchoolAdapter;
import com.codepath.collegebored.models.School;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TimelineFragment extends Fragment {

    public static final String TAG = "TimelineFragment";
    private RecyclerView rvSchools;
    protected SchoolAdapter adapter;
    protected List<School> allSchools;

    public TimelineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(false);
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvSchools = view.findViewById(R.id.rvSchools);
        allSchools = new ArrayList<>();
        adapter = new SchoolAdapter(getContext(), allSchools);
        rvSchools.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvSchools.setLayoutManager(linearLayoutManager);
        queryPosts();
    }

    private void queryPosts(){
        ParseQuery<School> query = ParseQuery.getQuery(School.class);
        query.include(School.KEY_INSTITUTION_NAME);
        query.setLimit(20);
        query.findInBackground(new FindCallback<School>() {
            @Override
            public void done(List<School> schools, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue loading schools");
                    return;
                }

                for( School school: schools){
                    Log.i(TAG, "School: " + school.getINSTITUTION_NAME());
                }
                allSchools.addAll(schools);
                adapter.notifyDataSetChanged();
            }
        });
    }

}