package com.codepath.collegebored.fragments;
/*
* When user clicks on list item in Searchfragment it takes them to this fragment to show school details
 */
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.collegebored.DoubleTap;
import com.codepath.collegebored.ParseApplication;
import com.codepath.collegebored.R;
import com.codepath.collegebored.models.Favorite;
import com.codepath.collegebored.models.School;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Headers;

public class SchoolDetailsFragment extends Fragment {
    AsyncHttpClient client = new AsyncHttpClient();
    TextView tvSchoolNameDetails;
    TextView tvSATscore;
    School currentSchool;
    ParseUser currentUser;
    ImageButton btnFavorite;
    ImageView ivSchoolPhoto;
    Favorite favorite;
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
        ivSchoolPhoto = view.findViewById(R.id.ivSchoolPhoto);
        tvSchoolNameDetails.setText(DATA_FROM_SEARCH_FRAGMENT);
        tvSATscore = view.findViewById(R.id.tvSATscore);
        btnFavorite = view.findViewById(R.id.btnFavoriteMatch);
        if (favoriteSchoolExists(DATA_FROM_SEARCH_FRAGMENT, currentUser)){
            changebtnFav(true);
        }
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSchool.getFavStatus() == true){
                    currentSchool.setFavStatus(false);
                    changebtnFav(false);
                    ParseApplication.changeFavStatus(currentSchool, false);
                }
                else{
                    changebtnFav(true);
                    currentSchool.setFavStatus(true);
                    ParseApplication.changeFavStatus(currentSchool, true);
                }
                if (!favoriteSchoolExists(DATA_FROM_SEARCH_FRAGMENT, currentUser)){
                    currentSchool.setINSTITUTION_NAME(DATA_FROM_SEARCH_FRAGMENT);
                    favoriteSchool(currentUser, newSchool(currentSchool));
                }
            }
        });
        //Double Tap Gesture (Like)
        view.setOnClickListener(new DoubleTap() {
            @Override
            public void onSingleClick(View v) {}
            @Override
            public void onDoubleClick(View v) {
                if (currentSchool.getFavStatus() == true){
                    currentSchool.setFavStatus(false);
                    changebtnFav(false);
                    ParseApplication.changeFavStatus(currentSchool, false);
                }
                else{
                    changebtnFav(true);
                    currentSchool.setFavStatus(true);
                    ParseApplication.changeFavStatus(currentSchool, true);
                }

                // if the school already exists then get the school by finding it by name and then
                // set the currentschool to what we get back
                if (favoriteSchoolExists(DATA_FROM_SEARCH_FRAGMENT, currentUser)){
                    Toast.makeText(getContext(), "School Already Exists", Toast.LENGTH_LONG).show();
                    changebtnFav(true);
                }
                else{
                    currentSchool.setINSTITUTION_NAME(DATA_FROM_SEARCH_FRAGMENT);
                    favoriteSchool(currentUser, newSchool(currentSchool));
                }
            }
        });
        SAT_SCORE(DATA_FROM_SEARCH_FRAGMENT);
        getImage(DATA_FROM_SEARCH_FRAGMENT);
    }


    public boolean favoriteSchoolExists(String name, ParseUser user){
        ParseQuery<Favorite> query = ParseQuery.getQuery(Favorite.class);
        query.whereEqualTo(Favorite.KEY_SCHOOL_NAME, name);
        query.whereEqualTo(Favorite.KEY_USER, user);
        try{
            List<Favorite> result = query.find();
            if((result == null || result.isEmpty())){
                return false;
            }
            else{
                currentSchool = (School) result.get(0).getSchool();
                return true;
            }
        } catch (ParseException e) {
            Log.d(TAG, "Exception: " + e.getMessage());

            e.printStackTrace();
        }
        return false;
    }

    public void getImage(String INSTITUTION_NAME) {
        final String GET_SCHOOL_URL = School.BASE_URL + "school.name=" + INSTITUTION_NAME + "&fields=school.school_url&per_page=1" + School.API_KEY;
        client.get(GET_SCHOOL_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    JSONObject JSON_SAT_OBJECT = results.getJSONObject(0);
                    if (JSON_SAT_OBJECT.isNull("school.school_url")){
                        ivSchoolPhoto.setImageResource(R.drawable.blank_school);
                    }
                    else {
                        String FINAL_SCHOOL_URL = JSON_SAT_OBJECT.getString("school.school_url");
                        Glide.with(getContext()).load("https://logo.clearbit.com/" + FINAL_SCHOOL_URL).error(R.drawable.blank_school).override(200,200).into(ivSchoolPhoto);
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
    private School newSchool(School school){
        school.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if ( e != null){
                    Log.e(TAG, e.getMessage());
                }
                school.setINSTITUTION_NAME(school.getINSTITUTION_NAME());
                Toast.makeText(getContext(), "New School added!", Toast.LENGTH_SHORT).show();
            }
        });
        return school;
    }
    private void favoriteSchool(ParseUser user, School school) {
        favorite = new Favorite();
        favorite.setSchool(school);
        favorite.setUser(user);
        favorite.setSchoolName(school.getINSTITUTION_NAME());
        favorite.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if ( e != null){
                    Log.e(TAG, e.getMessage());
                }
                Toast.makeText(getContext(), "School was successfully saved!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SAT_SCORE(String INSTITUTION_NAME) {
        final String GET_SAT_URL = School.BASE_URL + "school.name=" + INSTITUTION_NAME + "&fields=latest.admissions.sat_scores.average.overall&per_page=1" + School.API_KEY;
        client.get(GET_SAT_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    JSONObject JSON_SAT_OBJECT = results.getJSONObject(0);
                    if (JSON_SAT_OBJECT.isNull("latest.admissions.sat_scores.average.overall")){
                        tvSATscore.setText("Average SAT information not found");
                    }
                    else {
                        int FINAL_SCHOOL_SAT = JSON_SAT_OBJECT.getInt("latest.admissions.sat_scores.average.overall");
                        tvSATscore.setText("Average SAT Score: " + FINAL_SCHOOL_SAT);
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
