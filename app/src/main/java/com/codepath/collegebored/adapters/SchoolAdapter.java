/* this adapter will link all the liked schools information on user
*  timeline
 */

package com.codepath.collegebored.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.method.LinkMovementMethod;
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
import com.codepath.collegebored.ParseApplication;
import com.codepath.collegebored.R;
import com.codepath.collegebored.models.Favorite;
import com.codepath.collegebored.models.School;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
        TextView tvSchoolWebsite;
        ImageView ivFavSchool;
        ImageButton btnFavoriteMatch2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSchoolName = itemView.findViewById(R.id.tvSchoolName);
            ivFavSchool = itemView.findViewById(R.id.ivFavSchool);
            tvSchoolWebsite = itemView.findViewById(R.id.tvSchoolWebsite);
            btnFavoriteMatch2 = itemView.findViewById(R.id.btnFavoriteMatch2);
        }


        public void bind(@NotNull Favorite favorite) {
           tvSchoolName.setText(favorite.getSchool().get("INSTITUTION_NAME").toString());
           getImage(favorite.getSchool().get("INSTITUTION_NAME").toString());
            if (favoriteSchoolExists(favorite.getSchoolName(), ParseUser.getCurrentUser())) {
                changebtnFav(true);
            }
           btnFavoriteMatch2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "UNLIKE THIS SCHOOL", Toast.LENGTH_LONG).show();
                }
            });
           }
        public boolean favoriteSchoolExists(String name, ParseUser user){
            ParseQuery<Favorite> query = ParseQuery.getQuery(Favorite.class);
            query.whereEqualTo(Favorite.KEY_SCHOOL_NAME, name);
            query.whereEqualTo(Favorite.KEY_USER, user);
            try{
                List<Favorite> result = query.find();
                if((result == null || result.isEmpty())){
                    return false;
                } else {
                    return true;
                }
            } catch (ParseException e) {
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
                            ivFavSchool.setImageResource(R.drawable.blank_school);
                            tvSchoolWebsite.setText("No website found");
                        }
                        else {
                            String FINAL_SCHOOL_URL = JSON_SAT_OBJECT.getString("school.school_url");
                            tvSchoolWebsite.setText(FINAL_SCHOOL_URL);
                            tvSchoolWebsite.setMovementMethod(LinkMovementMethod.getInstance());
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
        public void changebtnFav(boolean status){
            if(status == true){
                Drawable unwrapped = AppCompatResources.getDrawable(context, R.drawable.ufi_heart_active);
                Drawable wrapped = DrawableCompat.wrap(unwrapped);
                DrawableCompat.setTint(wrapped, Color.RED);
                btnFavoriteMatch2.setImageDrawable(wrapped);
            }
            else {
                Drawable unwrapped = AppCompatResources.getDrawable(context, R.drawable.ufi_heart);
                Drawable wrapped = DrawableCompat.wrap(unwrapped);
                DrawableCompat.setTint(wrapped, Color.BLACK);
                btnFavoriteMatch2.setImageDrawable(wrapped);
            }
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

