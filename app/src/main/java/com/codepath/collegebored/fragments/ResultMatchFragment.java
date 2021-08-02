package com.codepath.collegebored.fragments;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.collegebored.R;
import com.codepath.collegebored.adapters.MatchingAdapter;
import com.codepath.collegebored.models.School;
import com.parse.Parse;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Headers;

public class ResultMatchFragment extends Fragment {
    public static final String TAG = "ResultMatchFragment";
    RecyclerView rvMatchedSchools;
    MatchingAdapter adapter;
    List<School> matchedSchools;
    //init_schools will hold all the schools in the state selected by user
    List<School> init_schools;
    HashMap<String, Integer> hashSAT;
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
        init_schools = new ArrayList<>();
        matchedSchools = new ArrayList<>();
        hashSAT = new HashMap<>();
        Bundle bundle = this.getArguments();
        state = bundle.getString("key");
        getStateSchool(state);
        adapter = new MatchingAdapter(getContext(), matchedSchools);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvMatchedSchools.setLayoutManager(linearLayoutManager);
        rvMatchedSchools.setAdapter(adapter);

    }

    public void getStateSchool(String state){
        final String matchURL = School.BASE_URL + "school.state=" + state + "&latest.admissions.sat_scores.average.overall__not=null&fields=school.name,latest.admissions.sat_scores.average.overall&per_page=80" + School.API_KEY;
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
                        mSchool.setSAT_Score(Integer.valueOf(sat));
                        init_schools.add(mSchool);
                    }
                   updateHash(init_schools, hashSAT);
                   adapter.addAll(compare(ParseUser.getCurrentUser(), hashSAT));
                   Log.d(TAG, "=========" + (compare(ParseUser.getCurrentUser(), hashSAT).size()));
                   Log.d(TAG, (compare(ParseUser.getCurrentUser(), hashSAT).toString()));
                   adapter.notifyDataSetChanged();
                    if(matchedSchools.size() == 0){
                        Toast.makeText(getContext(), "We are unable to match you with schools in this state!", Toast.LENGTH_SHORT).show();
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

    public void updateHash(List<School> schools, HashMap<String, Integer> hash){
        for (int i = 0; i < schools.size(); i ++){
            hash.put(schools.get(i).getINSTITUTION_NAME(), schools.get(i).getSATScore());
        }
    }

    //This method will get all of the SAT scores and compare it to the user's score
    public ArrayList<School> compare(ParseUser user, HashMap<String, Integer> hash){
        ArrayList<School> start = new ArrayList<>();
        int minSAT = user.getInt("SAT")-200;
        int maxSAT = user.getInt("SAT")+100;
        Log.d(TAG, String.valueOf(minSAT) + " " + String.valueOf(maxSAT) );

        //first we sort the hashmap from lowest SAT score to highest
        Set<Map.Entry<String, Integer>> entrySet = hash.entrySet();
        List<Map.Entry<String, Integer>> list = new ArrayList<>(entrySet);
        sort(list);
        Log.d(TAG, list.toString());

        Range<Integer> myRange = new Range<Integer>(minSAT, maxSAT);
        int first = 0;
        int last = list.size()-1;
        while (first <= last ) {
            int mid = ( first + last ) / 2;
            //if schools SAT is lower than within range of reccomended SAT score
            if (list.get(mid).getValue() < minSAT){
                first = mid + 1;
                //Log.d(TAG, Boolean.toString(list.get(mid).getValue() < minSAT));
            }
            //SAT Score within range
            else if(list.get(mid).getValue() > maxSAT){
                //Log.d(TAG, Boolean.toString(list.get(mid).getValue() > maxSAT));
                last = mid - 1;
            }
            // otherwise avg SAT Score is too high for student (outside of reccomended)
            else if (myRange.contains(list.get(mid).getValue())){
               // Log.d(TAG, list.get(mid).getKey()+ " " + list.get(mid).getValue());
//                Log.d(TAG, Boolean.toString((list.get(mid).getValue()) > maxSAT));
                School matched = new School();
                matched.setINSTITUTION_NAME(list.get(mid).getKey());
                matched.setSAT_Score(list.get(mid).getValue());
                start.add(matched);
                list.remove(mid);
            }
            if(list.size() == mid){
                break;
            }
        }
        return start;
    }
    public void binarySearch(Range<Integer> range, List<Map.Entry<String, Integer>> list, int min, int max){
        //Binary search implementation
        int first = 0;
        int last = list.size()-1;
        for (int i = 0; i < list.size(); i ++) {
            int mid = ( last - first ) / 2;
            //if schools SAT is lower than within range of reccomended SAT score
            if (list.get(mid).getValue() < min){
                first = mid + 1;
            }
            //SAT Score within range
            else if(range.contains(list.get(mid).getValue())){
                School matched = new School();
                matched.setINSTITUTION_NAME(list.get(mid).getKey());
                matched.setSAT_Score(list.get(mid).getValue());
                matchedSchools.add(matched);
                //list.remove(mid);
            }
            // otherwise avg SAT Score is too high for student (outside of reccomended)
            else if (list.get(mid).getValue() > max){
                last = mid - 1;
            }
            adapter.notifyDataSetChanged();
        }
    }

    public void sort(List<Map.Entry<String, Integer>> list){
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
    }
}
