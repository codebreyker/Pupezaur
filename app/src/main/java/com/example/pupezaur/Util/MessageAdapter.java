package com.example.pupezaur.Util;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pupezaur.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.jar.Attributes;

import static androidx.constraintlayout.widget.ConstraintSet.LEFT;
import static androidx.constraintlayout.widget.ConstraintSet.RIGHT;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    Context context;
    List<Message> messageList;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    List <User> userList;
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
//        android.view.View view= LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
//        return new ViewHolder(view);
        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = messageList.get(position);
//        User user = userList.get(position);
//        holder.show_message.setText(message.getName() + message.getMessage());
        if (message.getName()==AllMethods.name) {
            holder.show_message.setText("You" + "\n" + message.getMessage());
        } else {
            holder.show_message.setText(message.getName() + "\n" + message.getMessage());
            }
    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView show_message;
        RecyclerView recyclerView;
        LinearLayout llMessage;

        public ViewHolder(android.view.View itemView) {
            super(itemView);

            recyclerView = itemView.findViewById(R.id.recycler_view);
//            llMessage=itemView.findViewById(R.id.llMessage);
            show_message=itemView.findViewById(R.id.show_message);
//            show_message_left=itemView.findViewById(R.id.show_message_left);


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

