package com.codepath.collegebored;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.asynchttpclient.callback.TextHttpResponseHandler;
import com.codepath.collegebored.models.School;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivty";
    public static final String API_KEY = BuildConfig.API_KEY;
    private BottomNavigationView bottomNavigationView;
    AsyncHttpClient client = new AsyncHttpClient();
    School school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        school = new School();
        client.get("https://api.data.gov/ed/collegescorecard/v1/schools?api_key="+ API_KEY, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, json.toString());
                try {
//                    String results = json.jsonObject.get("results").toString();
//                    Log.d(TAG, results );

                    //this gets each school in results and prints all of their info
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    //Log.d(TAG, results.toString());
                    for (int i = 0; i < 3; i++){
                        JSONObject obj1 = results.getJSONObject(i);
                        //gets first list latest
                        Log.d(TAG, obj1.toString());
                        String strLatest = obj1.getString("latest");
                        //turns list into json to look for admissions
                        JSONObject latest = new JSONObject(strLatest);
                        Log.d(TAG, latest.getString("admissions"));
                        String act_scoresStr = latest.getString("admissions");
                        JSONObject act_scores = new JSONObject(act_scoresStr);
                        Log.d(TAG, act_scores.getString("act_scores"));
                    }

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