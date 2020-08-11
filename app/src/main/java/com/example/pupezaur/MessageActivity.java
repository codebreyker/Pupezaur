package com.example.pupezaur;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pupezaur.Util.Chat;
import com.example.pupezaur.Util.MessageAdaptor;
import com.example.pupezaur.Util.UserUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    Button btn_send;
    EditText text_send;
    TextView username;
    FirebaseUser fuser;
    DatabaseReference dbreference;
    Intent intent;
    MessageAdaptor messageAdaptor;
    List<Chat> mChat;
    RecyclerView recyclerView;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_message);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        username = findViewById(R.id.username);
        btn_send = (Button) findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        intent = getIntent();
        userid = getIntent().getStringExtra("userid");

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = text_send.getText().toString();
                System.out.println("ajddadskhfksd");
                if (!msg.equals("")) {
                    sendMessage(fuser.getUid(), userid, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        dbreference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        dbreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserUtil user = snapshot.getValue(UserUtil.class);
                username.setText(user.getName());
                readMessages(fuser.getUid(), userid, user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);
    }

    private void readMessages(String uid, final String myid, final String userid) {
        mChat = new ArrayList<>();
        dbreference = FirebaseDatabase.getInstance().getReference("Chats");
        dbreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChat.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Chat chat = snapshot1.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) || chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) || chat.getSender().equals(myid)) {
                        mChat.add(chat);
                    }
                }
                messageAdaptor = new MessageAdaptor(MessageActivity.this, mChat, UserUtil.getName());
                recyclerView.setAdapter(messageAdaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

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
