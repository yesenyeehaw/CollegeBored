/* this adapter will link all the liked schools information on user
*  timeline
 */

package com.codepath.collegebored.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.collegebored.R;
import com.codepath.collegebored.models.Favorite;
import com.codepath.collegebored.models.School;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Headers;


public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.ViewHolder>{
    Context context;
    List<Favorite> favorites;
    AsyncHttpClient client = new AsyncHttpClient();

    public SchoolAdapter(Context context, List<Favorite> favorites){
        this.context = context;
        this.favorites = favorites;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_school, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolAdapter.ViewHolder holder, int position) {
        Favorite favorite = favorites.get(position);
        holder.bind(favorite);
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvSchoolName;
        ImageView ivFavSchool;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSchoolName = itemView.findViewById(R.id.tvSchoolName);
            ivFavSchool = itemView.findViewById(R.id.ivFavSchool);
        }


        public void bind(@NotNull Favorite favorite) {
           tvSchoolName.setText(favorite.getSchool().get("INSTITUTION_NAME").toString());
           getImage(favorite.getSchool().get("INSTITUTION_NAME").toString());
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
                            ivFavSchool.setImageResource(R.drawable.blank_school);
                        }
                        else {
                            String FINAL_SCHOOL_URL = JSON_SAT_OBJECT.getString("school.school_url");
                            Glide.with(context).load("https://logo.clearbit.com/" + FINAL_SCHOOL_URL).error(R.drawable.blank_school).override(200,200).into(ivFavSchool);
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

    public void clear() {
        favorites.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Favorite> list){
        favorites.addAll(list);
        notifyDataSetChanged();
    }
}

