package com.codepath.collegebored;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.collegebored.models.School;

import java.util.List;


public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.ViewHolder>{
    Context context;
    List<School> schools;

    public SchoolAdapter(Context context, List<School> schools){
        this.context = context;
        this.schools = schools;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_school, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolAdapter.ViewHolder holder, int position) {
        School school = schools.get(position);
        holder.bind(school);
    }

    @Override
    public int getItemCount() {
        return schools.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(School school) {
        }
    }
}
