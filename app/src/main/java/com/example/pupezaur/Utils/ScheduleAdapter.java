package com.example.pupezaur.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pupezaur.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    Context context;
    List<Schedule> scheduleList;

    DatabaseReference databaseReference;

    public ScheduleAdapter (Context context, List<Schedule> scheduleList, DatabaseReference databaseReference) {
        this.context = context;
        this.scheduleList = scheduleList;
        this.databaseReference = databaseReference;
    }

    public void addSchedule (Schedule schedule){
        this.scheduleList.add(schedule);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.schedule_item, parent, false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
            holder.start_timer.setText(schedule.getStartHour());
            holder.end_timer.setText(schedule.getEndHour());
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView start_timer, end_timer;
        RecyclerView recyclerView;
        LinearLayout layoutToAdd;

        public ViewHolder(android.view.View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            start_timer = itemView.findViewById(R.id.start_timer);
            end_timer = itemView.findViewById(R.id.end_timer);
            layoutToAdd = itemView.findViewById(R.id.layoutToAdd);
        }
    }

}