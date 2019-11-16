package com.example.basketballscorekeeper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {
    Context context;
    ArrayList<Match> matches;

    public MatchAdapter(Context context, ArrayList<Match> list) {
        this.context = context;
        this.matches = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTeamName1, tvTeamName2, tvTeamScore1, tvTeamScore2, tvWinner;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvTeamName1 = itemView.findViewById(R.id.tvTeamName1);
            tvTeamName2 = itemView.findViewById(R.id.tvTeamName2);
            tvTeamScore1 = itemView.findViewById(R.id.tvTeamScore1);
            tvTeamScore2 = itemView.findViewById(R.id.tvTeamScore2);
            tvWinner = itemView.findViewById(R.id.tvWinner);
        }
    }
    @NonNull
    @Override
    public MatchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(matches.get(position));
        holder.tvTeamName1.setText(matches.get(position).getTeam1());
        holder.tvTeamName2.setText(matches.get(position).getTeam2());
        holder.tvTeamScore1.setText(matches.get(position).getScore1());
        holder.tvTeamScore2.setText(matches.get(position).getScore2());
        holder.tvWinner.setText(matches.get(position).getWinner());
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }


}
