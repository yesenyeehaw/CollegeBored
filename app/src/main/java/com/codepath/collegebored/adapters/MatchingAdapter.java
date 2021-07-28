package com.codepath.collegebored.adapters;

import android.content.Context;
import android.service.autofill.FieldClassification;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.collegebored.R;
import com.codepath.collegebored.models.Favorite;
import com.codepath.collegebored.models.School;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;

import static android.os.Build.VERSION_CODES.R;

public class MatchingAdapter extends RecyclerView.Adapter<MatchingAdapter.ViewHolder> {
    Context context;
    List<School> schools;

    public MatchingAdapter(Context context, List<School> schools){
        this.context = context;
        this.schools = schools;
    }

    @NonNull
    @Override
    public MatchingAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(com.codepath.collegebored.R.layout.item_matched, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchingAdapter.ViewHolder holder, int position) {
        School school = schools.get(position);
        holder.bind(school);
    }

    @Override
    public int getItemCount() { return schools.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvMatchedSchool;
        TextView tvSATScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMatchedSchool = itemView.findViewById(com.codepath.collegebored.R.id.tvMatchedSchool);
            tvSATScore = itemView.findViewById(com.codepath.collegebored.R.id.tvSATScore);
        }

        public void bind(@NotNull School school) {
            tvMatchedSchool.setText(school.getINSTITUTION_NAME());
            if (school.getSATScore() == 0) {
                tvSATScore.setText("NO SAT SCORE AVAILABLE");
            } else {
                tvSATScore.setText(String.valueOf(school.getSATScore()));
            }
        }
    }
}
