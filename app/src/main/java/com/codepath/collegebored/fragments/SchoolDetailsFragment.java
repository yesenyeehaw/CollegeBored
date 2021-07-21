package com.codepath.collegebored.fragments;
/*
* When user clicks on list item in Searchfragment it takes them to this fragment to show school details
 */
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
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
import com.codepath.collegebored.ParseApplication;
import com.codepath.collegebored.R;
import com.codepath.collegebored.models.Favorite;
import com.codepath.collegebored.models.School;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class SchoolDetailsFragment extends Fragment {
    AsyncHttpClient client = new AsyncHttpClient();
    TextView tvSchoolNameDetails;
    TextView tvSATscore;
    School currentSchool;
    ParseUser currentUser;
    ImageButton btnFavorite;
    Favorite favorite;

    //TODO: find a way to make these variables easilly accessible (from the same place)
    public static final String URL = "https://api.data.gov/ed/collegescorecard/v1/schools.json?";
    public static final String API_KEY = "&api_key=" + BuildConfig.API_KEY;
    public static final String TAG = "SchoolDetailsFragment";

    public SchoolDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_school_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        currentSchool = new School();
        currentUser = ParseUser.getCurrentUser();
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
                if (currentSchool.getFavStatus() == true){
                    currentSchool.setFavStatus(false);
                    changebtnFav(false);
                    ParseApplication.changeFavStatus(currentSchool, false);
                }
                else{
                    currentSchool.setFavStatus(true);
                    changebtnFav(true);
                    ParseApplication.changeFavStatus(currentSchool, true);
                }
                currentSchool.setINSTITUTION_NAME(DATA_FROM_SEARCH_FRAGMENT);
                favoriteSchool(currentUser, currentSchool);
                //status(currentSchool, currentUser);
            }
        });
        SAT_SCORE(DATA_FROM_SEARCH_FRAGMENT);
        getImage(DATA_FROM_SEARCH_FRAGMENT);
    }

//    public void status(School school, ParseUser user){
//        ParseQuery<School> query = ParseQuery.getQuery("FavStatus");
//        query.include(school.KEY_INSTITUTION_NAME);
//        query.findInBackground(new FindCallback<School>() {
//            @Override
//            public void done(List<School> object, ParseException e) {
//                if (e != null ){
//                    Log.e(TAG, e.getMessage());
//                } else {
//                    Log.d(TAG, object.get(0).toString());
//                }
//            }
//        });

//    }
    // TODO This will return an image of the school
    public String getImage(String INSTITUTION_NAME) {
        return "";
    }

    public void changebtnFav(boolean status){
        if(status == true){
            Drawable unwrapped = AppCompatResources.getDrawable(getContext(), R.drawable.ufi_heart_active);
            Drawable wrapped = DrawableCompat.wrap(unwrapped);
            DrawableCompat.setTint(wrapped, Color.RED);
            btnFavorite.setImageDrawable(wrapped);
        }
        else {
            Drawable unwrapped = AppCompatResources.getDrawable(getContext(), R.drawable.ufi_heart);
            Drawable wrapped = DrawableCompat.wrap(unwrapped);
            DrawableCompat.setTint(wrapped, Color.BLACK);
            btnFavorite.setImageDrawable(wrapped);
        }
    }
    private void favoriteSchool(ParseUser user, School school) {
        favorite = new Favorite();
        favorite.setSchool(school);
        favorite.setUser(user);
        favorite.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if ( e != null){
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(getContext(), "Error while saving.", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getContext(), "School was successfully saved!", Toast.LENGTH_SHORT).show();
            }
        });
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