package com.example.pupezaur.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pupezaur.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    Context context;
    List<Message> messageList;
    private static MessageAdapter instance;
    private HashMap<String, MessageAdapter> MessageAdapterHashMap;

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public MessageAdapter(Context context, List<Message> messageList, DatabaseReference databaseReference) {
        this.context = context;
        this.messageList = messageList;
        this.databaseReference = databaseReference;
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

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Message message = messageList.get(position);
        if (message.getName().equals(AllMethods.name)) {
            holder.txt_you.setText("You");
            holder.show_message.setText(message.getMessage());
            holder.time_box.setText(DateFormat.format("HH:mm - MMM.dd.yyyy", message.getMessageTime()));
                        holder.show_message.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    CharSequence options[] = new CharSequence[]{
                            "Yes",
                            "Cancel"
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.show_message.getContext());
                    builder.setTitle("Delete message?");

                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (i == 0) {
                                deleteMessage(position, holder);
                            } else if (i == 1) { }
                        }
                    });
                    builder.show();
                    return false;
                }
            });
        } else {
            holder.txt_sender.setText(message.getName());
            holder.show_message.setText(message.getMessage());
            holder.time_box.setText(DateFormat.format("HH:mm - MMM.dd.yyyy", message.getMessageTime()));
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView show_message, time_box, txt_you, txt_sender;
        RecyclerView recyclerView;

        public ViewHolder(android.view.View itemView) {
            super(itemView);

            recyclerView = itemView.findViewById(R.id.recycler_view);
            show_message = itemView.findViewById(R.id.show_message);
            time_box = itemView.findViewById(R.id.time_box);
            txt_you = itemView.findViewById(R.id.txt_you);
            txt_sender = itemView.findViewById(R.id.txt_sender);

            show_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (time_box.getVisibility() == View.VISIBLE) {
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
        Message message = messageList.get(position);
        if (message.getName().equals(AllMethods.name)) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    public void deleteMessage (final int position, final ViewHolder holder){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Chats").child(firebaseUser.getPhoneNumber()).child(messageList.get(position).getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(holder.show_message.getContext(), "Message deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(holder.show_message.getContext(), "Message could not be deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

