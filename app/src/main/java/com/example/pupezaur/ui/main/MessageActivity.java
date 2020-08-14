package com.example.pupezaur.ui.main;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pupezaur.MainActivity;
import com.example.pupezaur.R;
import com.example.pupezaur.Util.AllMethods;
import com.example.pupezaur.Util.Chat;
import com.example.pupezaur.Util.Message;
import com.example.pupezaur.Util.MessageAdapter;
import com.example.pupezaur.Util.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.security.AccessController.getContext;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    FirebaseUser fuser;
    FirebaseDatabase database;
    DatabaseReference reference;
    MessageAdapter messageAdapter;
    User user;
    List<Message> messages;
    List<Chat> mChat;
    Intent intent;
    TextView username;
    ImageButton btn_send;
    EditText text_send;
    RecyclerView recycler_view;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        init();

        recycler_view = (RecyclerView)findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(context));

//        btn_send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String msg = text_send.getText().toString();
//                if (!msg.equals("")){
//                    sendMessage(fuser.getUid(), fuser.getDisplayName(), msg );
//                } else {
//                    Toast.makeText(getApplicationContext(), "You can not send blank message", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        intent = getIntent();
        final String userid = intent.getStringExtra("id");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
//        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
//        reference.child("Users").child(userid);
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User user = snapshot.getValue(User.class);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    private void init() {

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = new User();

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        username = (TextView) findViewById(R.id.username);
        btn_send = (ImageButton) findViewById(R.id.btn_send);
        text_send = (EditText) findViewById(R.id.text_send);
        messages = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {
        if (!TextUtils.isEmpty(text_send.getText().toString())) {
            Message message = new Message (text_send.getText().toString(), user.getName());
            text_send.setText("");
            reference.push().setValue(message);
        } else {
            Toast.makeText(getApplicationContext(), "You can not send blank message", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = auth.getCurrentUser();
        user.setUid(currentUser.getUid());
        user.setEmail(currentUser.getEmail());

        database.getReference("Users").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                user.setUid(currentUser.getUid());
                AllMethods.name = user.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference = database.getReference("Chats");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());
                messages.add(message);
                displayMessage (messages);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());
                List<Message> newMessages = new ArrayList<Message>();

                for (Message m: messages) {
                    if (m.getKey().equals(message.getKey())){
                        newMessages.add(message);
                    } else {
                        newMessages.add(m);
                    }
                }

                messages = newMessages;
                displayMessage(messages);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());
                List<Message> newMessage = new ArrayList<Message>();

                for (Message m:messages) {
                    if (!m.getKey().equals(message.getKey())) {
                        newMessage.add(m);
                    }
                }

                messages = newMessage;
                displayMessage(messages);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        messages = new ArrayList<>();
    }

    private void displayMessage(List<Message> messages) {
        recycler_view.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
        messageAdapter = new MessageAdapter(MessageActivity.this, messages, mChat, reference);
        recycler_view .setAdapter(messageAdapter);
    }

    public void sendMessage (String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);
    }

}
