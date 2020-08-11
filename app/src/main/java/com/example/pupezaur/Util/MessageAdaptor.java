package com.example.pupezaur.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pupezaur.MainActivity;
import com.example.pupezaur.R;
import com.example.pupezaur.ui.main.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

    public class MessageAdaptor extends RecyclerView.Adapter<MessageAdaptor.ViewHolder> {
        public static final int MSG_TYPE_LEFT = 0;
        public static final int MSG_TYPE_RIGHT = 1;

        private List<Chat> mChat;
        private Context mContext;
        FirebaseUser firebaseUser;

    public MessageAdaptor(MainActivity mContext, List<Chat> mChat) {
        this.mChat = mChat;
        this.mContext = mContext;
        }

    @NonNull
    @Override
    public MessageAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdaptor.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdaptor.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdaptor.ViewHolder holder, int position) {

        Chat chat = mChat.get(position);
        holder.chat.setText(chat.getMessage());

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView chat;

        public ViewHolder (View itemView) {
            super (itemView);
            chat = itemView.findViewById(R.id.recycler_view);
        }
    }

    @Override
        public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (mChat.get(position).getSender().equals(firebaseUser.getUid())){
                return MSG_TYPE_RIGHT;
            } else {
                return MSG_TYPE_LEFT;
            }
    }

}