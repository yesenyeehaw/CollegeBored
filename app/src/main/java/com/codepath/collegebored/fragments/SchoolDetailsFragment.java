package com.codepath.collegebored.fragments;
/*
* When user clicks on list item in Searchfragment it takes them to this fragment to show school details
 */
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.collegebored.BuildConfig;
import com.codepath.collegebored.R;
import com.codepath.collegebored.models.School;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


import java.io.File;
import java.util.ArrayList;

import okhttp3.Headers;

public class SchoolDetailsFragment extends Fragment {
    AsyncHttpClient client = new AsyncHttpClient();
    TextView tvSchoolNameDetails;
    TextView tvSATscore;
    School currentSchool;
    ImageButton btnFavorite;

    //TODO: find a way to make these variables easilly accessible (from the same place)
    public static final String URL = "https://api.data.gov/ed/collegescorecard/v1/schools.json?";
    public static final String API_KEY = "&api_key=" + BuildConfig.API_KEY;
    public static final String TAG = "SchoolDetailsFragment";

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
        currentSchool = new School();
        super.onViewCreated(view, savedInstanceState);
        tvSchoolNameDetails = view.findViewById(R.id.tvSchoolNameDetails);
        Bundle bundle = this.getArguments();
        String DATA_FROM_SEARCH_FRAGMENT = bundle.getString("key");
        tvSchoolNameDetails.setText(DATA_FROM_SEARCH_FRAGMENT);
        tvSATscore = view.findViewById(R.id.tvSATscore);
        btnFavorite = view.findViewById(R.id.btnFavorite);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFavorite.setImageResource(R.drawable.ufi_heart_active);
            }
        });
        SAT_SCORE(DATA_FROM_SEARCH_FRAGMENT);
        getImage(DATA_FROM_SEARCH_FRAGMENT);
    }


    public String getImage(String INSTITUTION_NAME) {
        return "";
    }

    public void SAT_SCORE(String INSTITUTION_NAME) {
        final String GET_SAT_URL = URL + "school.name=" + INSTITUTION_NAME + "&fields=latest.admissions.sat_scores.average.overall&per_page=1" + API_KEY;
        client.get(GET_SAT_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    JSONObject JSON_SAT_OBJECT = results.getJSONObject(0);
                    int FINAL_SCHOOL_SAT = JSON_SAT_OBJECT.getInt("latest.admissions.sat_scores.average.overall");
                    tvSATscore.setText(Integer.toString(FINAL_SCHOOL_SAT));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "OnFailure");
            }
        });
    }
}