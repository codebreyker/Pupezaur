package com.example.pupezaur;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pupezaur.Util.AllMethods;
import com.example.pupezaur.Util.Chat;
import com.example.pupezaur.Util.Message;
import com.example.pupezaur.Util.MessageAdaptor;
import com.example.pupezaur.Util.UserUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton btn_send;
    EditText text_send;
    TextView username;
    FirebaseUser fuser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbreference;
    Intent intent;
    MessageAdaptor messageAdaptor;
    List<Chat> mChat;
    RecyclerView recyclerView;
    String userid;
    FirebaseAuth auth;
    RecyclerView rvMessage;
    UserUtil user;
    List<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_message);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();


        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        intent = getIntent();
        userid = getIntent().getStringExtra("userid");
//
//        btn_send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String msg = text_send.getText().toString();
//                if (!msg.equals("")) {
//                    sendMessage(fuser.getUid(), userid, msg);
//                } else {
//                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
//                }
//                text_send.setText("");
//                dbreference.push().setValue(msg);
//            }
//        });
//
//        fuser = FirebaseAuth.getInstance().getCurrentUser();
//        dbreference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
//
//        dbreference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                UserUtil user = snapshot.getValue(UserUtil.class);
//                username.setText(user.getName());
//                readMessages(fuser.getUid(), userid);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }
//
//    private void sendMessage(String sender, String receiver, String message) {
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("sender", sender);
//        hashMap.put("receiver", receiver);
//        hashMap.put("message", message);
//
//        reference.child("Chats").push().setValue(hashMap);
//    }
//
//    private void readMessages(final String myid, final String userid) {
//        mChat = new ArrayList<>();
//        dbreference = FirebaseDatabase.getInstance().getReference("Chats");
//        dbreference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                mChat.clear();
//                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                    Chat chat = snapshot1.getValue(Chat.class);
//                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
//                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
//                        mChat.add(chat);
//                    }
//                }
//                messageAdaptor = new MessageAdaptor(MessageActivity.this, mChat, UserUtil.getName());
//                recyclerView.setAdapter(messageAdaptor);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

//    public void send(View view) {
//        String msg = text_send.getText().toString();
//        if (!msg.equals("")) {
//            sendMessage(fuser.getUid(), userid, msg);
//        } else {
//            Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
//        }
//        text_send.setText("");
//    }

}

    private void init() {
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = new UserUtil();

        rvMessage = (RecyclerView)findViewById(R.id.recycler_view);
        text_send = (EditText)findViewById(R.id.text_send);
        btn_send = (ImageButton) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        messages = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {
        if (!TextUtils.isEmpty(text_send.getText().toString())){
            Message message = new Message(text_send.getText().toString(),user.getName());
            text_send.setText("");
            dbreference.push().setValue(message);
        } else {
            Toast.makeText(getApplicationContext(), "You can't send blank message", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart () {
        super.onStart();
        final FirebaseUser currentUser = auth.getCurrentUser();
        user.setId(currentUser.getUid());
        user.setEmail(currentUser.getEmail());

        firebaseDatabase.getReference("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(UserUtil.class);
                user.setId(currentUser.getUid());
                AllMethods.name = user.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbreference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());
                messages.add(message);
                displayMessages(messages);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());
                List<Message> newMessages = new ArrayList<Message>();

                for (Message m:messages){
                    if (m.getKey().equals(message.getKey())){
                        newMessages.add(message);
                    } else {
                        newMessages.add(m);
                    }
                }

                messages = newMessages;
                displayMessages(messages);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Message message = snapshot.getValue(Message.class);
                List<Message> newMessages = new ArrayList<Message>();
                for (Message m:messages){
                    if(!m.getKey().equals(message.getKey())){
                        newMessages.add(m);
                    }
                }
                messages = newMessages;
                displayMessages(messages);


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        @Override
//        protected void onRestart() {
//            super.onResume();
//            messages = new ArrayList<>();
//        }

    }

    private void displayMessages(List<Message> messages) {

        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
        messageAdaptor = new MessageAdaptor(MessageActivity.this, messages, dbreference );
        recyclerView.setAdapter(messageAdaptor);
    }
}
