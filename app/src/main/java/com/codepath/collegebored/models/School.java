package com.codepath.collegebored.models;

import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@ParseClassName("School")
public class School extends ParseObject {
    public static final String TAG = "School";
    public String SAT_ACT_Scores;
    public String INSTITUTION_NAME;

    public School() {
    }

    public static School fromJson(JSONObject jsonObject) throws JSONException {
        School school = new School();
        return school;
    }
}
