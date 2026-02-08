package com.aykon.badmintontracker;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

    private List<Match> matchList;
    private OnItemLongClickListener listener;

    // Interface om de klik door te sturen naar MainActivity
    public interface OnItemLongClickListener {
        void onItemLongClick(Match match);
    }

    // Constructor accepteert nu ook de listener
    public MatchAdapter(List<Match> matchList, OnItemLongClickListener listener) {
        this.matchList = matchList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match currentMatch = matchList.get(position);

        holder.tvOpponent.setText("Tegenstander: " + currentMatch.getOpponentName());
        holder.tvScore.setText(currentMatch.getScore());
        holder.tvDate.setText(currentMatch.getDate());

        if (currentMatch.isGewonnen()) {
            holder.ivStatus.setImageResource(R.drawable.ic_gewonnen);
            holder.ivStatus.setColorFilter(Color.parseColor("#2196F3"));
        } else {
            holder.ivStatus.setImageResource(R.drawable.ic_verloren);
            holder.ivStatus.setColorFilter(Color.parseColor("#FF5252"));
        }

        // Hier stellen we de actie in voor lang klikken
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(currentMatch);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public static class MatchViewHolder extends RecyclerView.ViewHolder {
        TextView tvOpponent, tvScore, tvDate;
        ImageView ivStatus;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOpponent = itemView.findViewById(R.id.tv_item_opponent);
            tvScore = itemView.findViewById(R.id.tv_item_score);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            ivStatus = itemView.findViewById(R.id.iv_match_status);
        }
    }
}