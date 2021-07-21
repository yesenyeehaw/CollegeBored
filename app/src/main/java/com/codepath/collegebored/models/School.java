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
    public static final String SAT_Score = "SAT_SCORE";
    public static final String KEY_INSTITUTION_NAME = "INSTITUTION_NAME";
    public static final String KEY_FAV_STATUS = "FavStatus";

    public School() {
    }

    public String getINSTITUTION_NAME(){
        return getString(KEY_INSTITUTION_NAME);
    }

    public void setINSTITUTION_NAME(String name) {
        put(KEY_INSTITUTION_NAME, name);
    }

    public Boolean getFavStatus(){ return getBoolean(KEY_FAV_STATUS); }

    public void setFavStatus(boolean status) { put(KEY_FAV_STATUS, status); }
}
