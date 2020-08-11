package com.example.pupezaur.Util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;

import com.example.pupezaur.MessageActivity;
import com.example.pupezaur.R;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<UserUtil> mUsers;
    private Context mContext;

    public UserAdapter(Context mContext, List<UserUtil> mUsers) {
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UserUtil user = mUsers.get(position);
        holder.username.setText(user.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
               Intent intent = new Intent (mContext, MessageActivity.class);
               intent.putExtra("userid", user.getId());
               mContext.startActivity(intent);
            }
        });
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
