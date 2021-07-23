package com.codepath.collegebored.fragments;
/*
 * This class handles all of a user's liked schools and will display them all in
 * a recycler view on the fragment
 */
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.collegebored.R;
import com.codepath.collegebored.SchoolAdapter;
import com.codepath.collegebored.models.Favorite;
import com.codepath.collegebored.models.School;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TimelineFragment extends Fragment {

    public static final String TAG = "TimelineFragment";
    private RecyclerView rvSchools;
    protected SchoolAdapter adapter;
    protected List<School> allSchools;
    protected List<Favorite> allFavorites;
    TextView tvStartUp;

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
        tvStartUp = view.findViewById(R.id.tvStartup);
        rvSchools = view.findViewById(R.id.rvSchools);
        allSchools = new ArrayList<>();
        allFavorites = new ArrayList<>();
        adapter = new SchoolAdapter(getContext(), allFavorites);
        rvSchools.setAdapter(adapter);
        if (allFavorites.size() > 0){
            tvStartUp.setHint("");
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvSchools.setLayoutManager(linearLayoutManager);
        //queryPosts();
        testQuery();
    }

    private void testQuery(){
        ParseQuery<Favorite> query = ParseQuery.getQuery(Favorite.class);
        query.include("School");
        query.include("User");
        query.whereEqualTo("User", ParseUser.getCurrentUser());
        Log.d(TAG, "Querying for user: " + ParseUser.getCurrentUser().getObjectId());
        query.setLimit(20);
        query.findInBackground(new FindCallback<Favorite>() {
            @Override
            public void done(List<Favorite> objects, ParseException e) {
                if(e == null){
                   Log.d(TAG, "Favorites for this user: " + objects.size());
                   ArrayList<Favorite> favSchools = new ArrayList<>();
                   for(Favorite f : objects){
                       favSchools.add(f);
                       allSchools.add((School) f.getSchool());
                   }
                   allFavorites.addAll(favSchools);
                   adapter.notifyDataSetChanged();
                    Log.d(TAG, "FavSchools: " + favSchools.size());
                }
            }
        });
    }
    //user == currentuser

    // ONACTIVITYRESULT,
    // if we added a new school, notifyAdapterItemInserted
    // if we unfavorited a school, notifyAdapterItemRemoved
}