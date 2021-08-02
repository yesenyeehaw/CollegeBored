package com.codepath.collegebored.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.collegebored.DoubleTap;
import com.codepath.collegebored.R;
import com.codepath.collegebored.models.School;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Headers;

public class MatchingAdapter extends RecyclerView.Adapter<MatchingAdapter.ViewHolder>{
    public static final String TAG = "MatchingAdapter";
    Context context;
    List<School> schools;
    AsyncHttpClient client = new AsyncHttpClient();

    public MatchingAdapter(Context context, List<School> schools) {
        this.context = context;
        this.schools = schools;
    }

    @NonNull
    @Override
    public MatchingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(com.codepath.collegebored.R.layout.item_matched, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchingAdapter.ViewHolder holder, int position) {
        School school = schools.get(position);
        holder.bind(school);
    }

    @Override
    public int getItemCount() {
        return schools.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMatchedSchool;
        TextView tvSATScore;
        ImageButton btnFavorite;
        ImageView ivMatched;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMatchedSchool = itemView.findViewById(com.codepath.collegebored.R.id.tvMatchedSchool);
            tvSATScore = itemView.findViewById(com.codepath.collegebored.R.id.tvSATScore);
            btnFavorite = itemView.findViewById(com.codepath.collegebored.R.id.btnFavoriteMatch);
            ivMatched = itemView.findViewById(R.id.ivMatched);
        }
        public void bind(@NotNull School school) {
            tvMatchedSchool.setText(school.getINSTITUTION_NAME());
            tvSATScore.setText("Average SAT Score: " + school.getSATScore());
            getImage(school.getINSTITUTION_NAME());
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
                            ivMatched.setImageResource(R.drawable.blank_school);
                        }
                        else {
                            String FINAL_SCHOOL_URL = JSON_SAT_OBJECT.getString("school.school_url");
                            Glide.with(context).load("https://logo.clearbit.com/" + FINAL_SCHOOL_URL).error(R.drawable.blank_school).override(200,200).into(ivMatched);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.d("SchoolAdapter", "OnFailure");
                }
            });
        }

        }
    // Add a list of items -- change to type used
    public void addAll(List<School> list) {
        schools.addAll(list);
        notifyDataSetChanged();
    }

    public void clear(){
        schools.clear();
        notifyDataSetChanged();
    }
}
