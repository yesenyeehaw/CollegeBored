package com.codepath.collegebored;
/*
 * This class handles the SearchFragment and the search bar
 */
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import okhttp3.Headers;


public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener{
    public static final String URL = "https://api.data.gov/ed/collegescorecard/v1/schools?api_key=";
    public static final String TAG = "SearchFragment";
    public static final String API_KEY = BuildConfig.API_KEY;
    AsyncHttpClient client = new AsyncHttpClient();

    public SearchFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_searchbar, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(SearchFragment.this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, query);
        searchAction(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public String searchAction (String query){
        final String testURL = "https://api.data.gov/ed/collegescorecard/v1/schools.json?school.name="+ query + "&fields=school.name&per_page=50&api_key=";
        final String[] searchSchool = new String[1];
        client.get(testURL + API_KEY, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, json.toString());
                searchSchool[0] = json.toString();
                Log.d(TAG, searchSchool[0]);
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "OnFailure");
            }
        });
        return searchSchool[0];
    }
}