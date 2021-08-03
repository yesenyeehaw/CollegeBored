package com.codepath.collegebored.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.collegebored.R;


public class MatchFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    Spinner spinner;
    Button btnMatchMe;
    ArrayAdapter<String> myAdapter;
    String state;
    public static final String TAG = "MatchFragment";

    public MatchFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(false);
        return inflater.inflate(R.layout.fragment_match, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = view.findViewById(R.id.spinner);
        myAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.states));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);
        spinner.setOnItemSelectedListener(this);

        btnMatchMe = view.findViewById(R.id.btnMatchMe);
        btnMatchMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key", state);
                Fragment fragment = new ResultMatchFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                // Configure the "in" and "out" animation files
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                // Perform the fragment replacement
                ft.replace(R.id.flContainer, fragment, "fragment");
                // Start the animated transition.
                ft.commit();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        state = parent.getItemAtPosition(pos).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
