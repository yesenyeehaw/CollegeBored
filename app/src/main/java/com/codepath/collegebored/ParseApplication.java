package com.codepath.collegebored;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.collegebored.models.Favorite;
import com.codepath.collegebored.models.School;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ParseApplication extends Application {
    public static final String TAG = "ParseApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(School.class);
        ParseObject.registerSubclass(Favorite.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("AYkgveoMTsykzttb2OgJRRV4zM7Y5GK5gwVuaUbn")
                .clientKey("7UrI5cMOVL4m7SbsnHZwVrUCq20mohju4jd50Rbp")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }

    public static void changeFavStatus(School school, boolean status){
        ParseQuery<School> query = ParseQuery.getQuery(School.class);

        query.getInBackground(school.getObjectId(), (object, e) -> {
            if (e != null){
                Log.e(TAG, "Error: " + e.getMessage());
            }
         else {
                object.put(School.KEY_FAV_STATUS, status);
                object.saveInBackground();
        }
    });
    }
}

