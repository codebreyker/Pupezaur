package com.example.pupezaur.Util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pupezaur.MessageActivity;
import com.example.pupezaur.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

import java.util.List;

    public class MessageAdaptor extends RecyclerView.Adapter<MessageAdaptor.MessageAdapterViewHolder> {

        Context context;
        List<Message> messages;
        DatabaseReference dbreference;

        public MessageAdaptor(Context context, List<Message> messages, DatabaseReference dbreference){
            this.context = context;
            this.dbreference = dbreference;
            this.messages = messages;
        }

        public static final int MSG_TYPE_LEFT = 0;
        public static final int MSG_TYPE_RIGHT = 1;

        private List<Chat> mChat;
        private Context mContext;
        private String username;
        FirebaseUser fuser;

    public MessageAdaptor(MessageActivity mContext, List<Chat> mChat, String username) {
        this.mChat = mChat;
        this.mContext = mContext;
        this.username = username;
        }

    @NonNull
    @Override
    public MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapterViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapterViewHolder(view);
        }

    }

        @Override
        public void onBindViewHolder(@NonNull MessageAdapterViewHolder holder, int position) {
            Message message = messages.get(position);
            if (message.getName().equals(AllMethods.name)){
                holder.show_message.setText("You: " + message.getMessage());
                holder.show_message.setGravity(Gravity.START);
            } else {
                holder.show_message.setText(message.getName() + ":" + message.getMessage());
            }
        }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;

        public ViewHolder (View itemView) {
            super (itemView);
            show_message = itemView.findViewById(R.id.show_message);
        }
    }

    @Override
        public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
            if (messages.get(position).getName().equals(fuser.getUid())){
                return MSG_TYPE_RIGHT;
            } else {
                return MSG_TYPE_LEFT;
            }
    }

        public class MessageAdapterViewHolder extends RecyclerView.ViewHolder {
                TextView show_message;

            public MessageAdapterViewHolder(@NonNull View itemView) {
                super(itemView);
                show_message = (TextView)itemView.findViewById(R.id.show_message);
            }
        }
    }