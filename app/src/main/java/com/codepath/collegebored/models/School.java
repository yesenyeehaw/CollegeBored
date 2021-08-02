package com.codepath.collegebored.models;
/*
* This class handles all of a school's attributes from Parse
 */

import com.codepath.collegebored.BuildConfig;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("School")
public class School extends ParseObject {
    public static final String TAG = "School";
    public static final String SAT_Score = "SAT_SCORE";
    public static final String SCHOOL_URL = "School_url";
    public static final String KEY_INSTITUTION_NAME = "INSTITUTION_NAME";
    public static final String KEY_FAV_STATUS = "FavStatus";
    public static final String BASE_URL = "https://api.data.gov/ed/collegescorecard/v1/schools.json?";
    public static final String API_KEY = "&api_key=" + BuildConfig.API_KEY;


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

    public int getSATScore(){ return getInt(SAT_Score); }

    public void setSAT_Score(int score){ put(SAT_Score, score); }

    public void setSchoolUrl(String url) { put(SCHOOL_URL, url); }

    public String getSchoolUrl() { return getString(SCHOOL_URL); };

}
