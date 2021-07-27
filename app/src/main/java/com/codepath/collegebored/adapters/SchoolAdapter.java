/* this adapter will link all the liked schools information on user
*  timeline
 */

package com.codepath.collegebored.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.collegebored.R;
import com.codepath.collegebored.models.Favorite;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.ViewHolder>{
    Context context;
    List<Favorite> favorites;

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSchoolName = itemView.findViewById(R.id.tvSchoolName);
        }


        public void bind(@NotNull Favorite favorite) {
           tvSchoolName.setText(favorite.getSchool().get("INSTITUTION_NAME").toString());
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

