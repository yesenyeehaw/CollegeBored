package com.codepath.collegebored.fragments;
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
import androidx.fragment.app.FragmentManager;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.collegebored.BuildConfig;
import com.codepath.collegebored.R;
import com.codepath.collegebored.models.School;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Headers;


public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener{
    public static final String TAG = "SearchFragment";
    //public static final String API_KEY = "&api_key=" + BuildConfig.API_KEY;
    AsyncHttpClient client = new AsyncHttpClient();
    ArrayList<String> schoolsList = new ArrayList<>();
    ListView lvSchools;
    TextView tvStartupS;
    School school = new School();
    ArrayAdapter arrayAdapter;

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
                String pos = lvSchools.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), "You've selected " + pos, Toast.LENGTH_SHORT).show();
                school.setINSTITUTION_NAME(pos);
                //Takes us to SchoolDetails fragment
                Bundle bundle = new Bundle();
                bundle.putString("key", school.getINSTITUTION_NAME());
                Fragment fragment = new SchoolDetailsFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                // Launch the fragment similar to startActivityforResult
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
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
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, searchAction(query));
        lvSchools.setAdapter(arrayAdapter);
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
        final String searchSchool_URL = School.BASE_URL + "school.name="+ query + "&fields=school.name&per_page=25"+ School.API_KEY;
        client.get(searchSchool_URL , new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try{
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    for (int i = 0; i < results.length(); i ++){
                        JSONObject JSON_SCHOOL_OBJECT = results.getJSONObject(i);
                        String FINAL_SCHOOL_NAME = JSON_SCHOOL_OBJECT.getString("school.name");
                        schoolsList.add(FINAL_SCHOOL_NAME);
                        arrayAdapter.notifyDataSetChanged();
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