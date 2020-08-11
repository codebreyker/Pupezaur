package com.example.pupezaur.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;
import com.example.pupezaur.R;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter {
    private List<UserUtil> mUsers;
    private Context mContext;

    public UserAdapter(Context mContext, List<UserUtil> mUsers) {
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;

        public ViewHolder (View itemView) {
            super (itemView);
            username = itemView.findViewById(R.id.username);
        }
    }
}
