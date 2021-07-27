package com.codepath.collegebored.fragments;

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

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.collegebored.R;
import com.codepath.collegebored.adapters.MatchingAdapter;
import com.codepath.collegebored.models.School;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class ResultMatchFragment extends Fragment {
    public static final String TAG = "ResultMatchFragment";
    private RecyclerView rvMatchedSchools;
    protected MatchingAdapter adapter;
    protected List<School> matchedSchools;
    AsyncHttpClient client = new AsyncHttpClient();
    String state;
    ParseUser user;


    public ResultMatchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result_match, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = ParseUser.getCurrentUser();
        rvMatchedSchools = view.findViewById(R.id.rvMatchedSchools);
        matchedSchools = new ArrayList<>();
        adapter = new MatchingAdapter(getContext(), matchedSchools);
        rvMatchedSchools.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvMatchedSchools.setLayoutManager(linearLayoutManager);
        Bundle bundle = this.getArguments();
        state = bundle.getString("key");
        matchedSchoolsByState(state);

    }

    public void matchedSchoolsByState(String state){
        final String matchURL = School.BASE_URL + "school.state=" + state + "&fields=school.name,latest.admissions.sat_scores.average.overall&per_page=80" + School.API_KEY;
        client.get(matchURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try{
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    for (int i = 0; i < results.length(); i ++){
                        School mSchool = new School();
                        JSONObject obj = results.getJSONObject(i);
                        String school = obj.getString("school.name");
                        String sat = obj.getString("latest.admissions.sat_scores.average.overall");
                        mSchool.setINSTITUTION_NAME(school);
                        //if SAT score is unavailable
                        if (obj.isNull("latest.admissions.sat_scores.average.overall")){
                            Log.d(TAG, "No Match!");
                        }else{
                            mSchool.setSAT_Score(Integer.valueOf(sat));
                            Log.d(TAG, String.valueOf(mSchool.getSATScore()));
                        }
                        matchedSchools.add(mSchool);
                        adapter.notifyDataSetChanged();
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "OnFailure" + response);
            }
        });
    }
}