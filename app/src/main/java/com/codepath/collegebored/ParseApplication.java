package com.codepath.collegebored;

import android.app.Application;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.collegebored.models.School;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(School.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("AYkgveoMTsykzttb2OgJRRV4zM7Y5GK5gwVuaUbn")
                .clientKey("7UrI5cMOVL4m7SbsnHZwVrUCq20mohju4jd50Rbp")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
