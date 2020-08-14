package com.example.pupezaur.Util;

import android.content.Context;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pupezaur.R;
import com.example.pupezaur.ui.main.MessageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder> {

    public static final int MSG_TYPE_RIGHT = 1;
    public static final int MSG_TYPE_LEFT = 0;
    public List<Chat> mChat;
    Context context;
    List<Message> messageList;
    DatabaseReference reference;
    FirebaseUser fuser;


    public MessageAdapter(Context context, List<Message> messageList, List<Chat> mChat, DatabaseReference reference) {
        this.context = context;
        this.messageList = messageList;
        this.reference = reference;
        this.mChat = mChat;
    }
    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView show_message_right, show_message_left;
        RelativeLayout ll;

        public MessageAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message_right = (TextView)itemView.findViewById(R.id.show_message_right);
            show_message_left = (TextView)itemView.findViewById(R.id.show_message_left);
            ll = (RelativeLayout)itemView.findViewById(R.id.llMessage);
        }
    }

    @NonNull
    @Override
    public MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType == MSG_TYPE_RIGHT) {
//            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
//            return new MessageAdapterViewHolder(view);
//        } else {
//            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
//            return new MessageAdapterViewHolder(view);
//        }
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
        return new MessageAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapterViewHolder holder, int position) {

        Message message = messageList.get(position);
        if (message.getName().equals(User.username)){
            holder.show_message_right.setText("You: " + message.getMessage());
            holder.show_message_right.setGravity(Gravity.START);
        } else {
            holder.show_message_left.setText(message.getName() + ":" + message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


//    @Override
//    public int getItemViewType(int position) {
//        fuser = FirebaseAuth.getInstance().getCurrentUser();
//        if (messageList.get(position).getName().equals(fuser.getUid())){
//            return MSG_TYPE_RIGHT;
//        } else {
//            return MSG_TYPE_LEFT;
//        }
//    }


}
