package com.example.pupezaur.ChatUtil;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pupezaur.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    Context context;
    List<Message> messageList;

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public MessageAdapter(Context context, List<Message> messageList, DatabaseReference databaseReference) {
        this.context = context;
        this.messageList = messageList;
        this.databaseReference=databaseReference;
    }

    public void addMessage(Message message){
        this.messageList.add(message);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
            return new ViewHolder(view);
        }
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (message.getName().equals(AllMethods.name)) {
            holder.show_message.setText("You\n" + message.getMessage());
            holder.time_box.setText(DateFormat.format("HH:mm \ndd.MM.yyyy", message.getMessageTime()));
        } else {
            holder.show_message.setText(message.getName() + "\n" + message.getMessage());
            holder.time_box.setText(DateFormat.format("HH:mm \n dd.MM.yyyy", message.getMessageTime()));
            }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView show_message, time_box;
        RecyclerView recyclerView;

        public ViewHolder(android.view.View itemView) {
            super(itemView);

            recyclerView = itemView.findViewById(R.id.recycler_view);
            show_message = itemView.findViewById(R.id.show_message);
            time_box = itemView.findViewById(R.id.time_box);

            show_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (time_box.getVisibility() == View.VISIBLE){
                        time_box.setVisibility(View.GONE);
                    } else {
                        time_box.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Message message=messageList.get(position);
        if (message.getName().equals(AllMethods.name)) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}

