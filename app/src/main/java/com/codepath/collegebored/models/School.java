package com.codepath.collegebored.models;
/*
* This class handles all of a school's attributes from Parse
 */
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
    public String SAT_Scores;
    public String INSTITUTION_NAME;
    public String HighSchoolName;

    public School() {
    }

    public String getINSTITUTION_NAME(){
        return getString("INSTITUTION_NAME");
    }

    public void setINSTITUTION_NAME(String name) {
        put(INSTITUTION_NAME, name);
    }



}
