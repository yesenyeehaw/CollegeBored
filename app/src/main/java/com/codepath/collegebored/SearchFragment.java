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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Headers;


public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener{
    public static final String URL = "https://api.data.gov/ed/collegescorecard/v1/schools.json?";
    public static final String TAG = "SearchFragment";
    public static final String API_KEY = "&api_key=" + BuildConfig.API_KEY;
    AsyncHttpClient client = new AsyncHttpClient();

    ArrayList<String> schoolsList = new ArrayList<>();
    ListView lvSchools;
    TextView tvStartupS;

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
        lvSchools = view.findViewById(R.id.lvSchools);
        tvStartupS = view.findViewById(R.id.tvStartupS);
        lvSchools.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "You've selected " + lvSchools.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });
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
        tvStartupS.setHint("");
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, searchAction(query));
        arrayAdapter.notifyDataSetChanged();
        lvSchools.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //ArrayList needs to be cleared before new query begins
        schoolsList.clear();
        return false;
    }

    //Search for schools and returns the list (MAX 50 SCHOOLS)
    public ArrayList<String> searchAction (String query){
        final String searchSchool_URL = URL + "school.name="+ query + "&fields=school.name&per_page=50";
        client.get(searchSchool_URL + API_KEY, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try{
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    for (int i = 0; i < results.length(); i ++){
                        JSONObject JSON_SCHOOL_OBJECT = results.getJSONObject(i);
                        String FINAL_SCHOOL_NAME = JSON_SCHOOL_OBJECT.getString("school.name");
                        //Log.d(TAG, FINAL_SCHOOL_NAME);
                        schoolsList.add(FINAL_SCHOOL_NAME);
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "OnFailure");
            }
        });
        return schoolsList;
    }
}