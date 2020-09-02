package com.example.pupezaur.MainActivities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pupezaur.R;
import com.example.pupezaur.Utils.Admin;
import com.example.pupezaur.Utils.AllMethods;
import com.example.pupezaur.Utils.Message;
import com.example.pupezaur.Utils.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    MessageAdapter mAdapter;
    Admin u;
    List<Message> messageList;

    RecyclerView recyclerView;
    EditText textSend;
    ImageButton btnSend;
    private String currentUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        currentUserName = auth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        u = new Admin();

        recyclerView = findViewById(R.id.recycler_view);
        textSend = findViewById(R.id.text_send);
        btnSend = findViewById(R.id.btn_send);

        btnSend.setOnClickListener(this);
        messageList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = auth.getCurrentUser();
        u.setUid(currentUser.getPhoneNumber());

        database.getReference("Admin").child(currentUser.getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u = dataSnapshot.getValue(Admin.class);
                u.setUid(currentUser.getPhoneNumber());
                AllMethods.name = u.getName();
//                Log.e(TAG, "onDataChange: "+ AllMethods.name );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference = database.getReference("Chats").child(currentUser.getPhoneNumber());
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
                messageList.add(message);
                displayMessages(messageList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
//                Toast.makeText(DashboardActivity.this, "Message changed...", Toast.LENGTH_SHORT).show();
                List<Message> newMessages = new ArrayList<>();
                for (Message m : messageList) {
                    if (m.getKey().equals(message.getKey())) {
                        newMessages.add(message);
                    } else {
                        newMessages.add(m);
                    }
                }
                messageList = newMessages;
                displayMessages(messageList);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
//                Toast.makeText(DashboardActivity.this, "Message deleted...", Toast.LENGTH_SHORT).show();
                List<Message> newMessages = new ArrayList<>();
                for (Message m : messageList) {
                    if (!m.getKey().equals(message.getKey())) {
                        newMessages.add(m);
                    }
                }
                messageList = newMessages;
                displayMessages(messageList);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void displayMessages(List<Message> messages) {
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new MessageAdapter(ChatActivity.this, messages, databaseReference);
//        this.recyclerView.scrollTo(0, this.recyclerView.getBottom());
        mAdapter.notifyItemInserted(messageList.size() - 1);
        this.recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
        ((LinearLayoutManager) recyclerView.getLayoutManager()).setStackFromEnd(true);
        this.recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View view) {
        if (!TextUtils.isEmpty(textSend.getText().toString())) {
            Message message = new Message(textSend.getText().toString(), u.getName());
            textSend.setText("");
            databaseReference.push().setValue(message);
        } else {
            Toast.makeText(this, "You cannot send an empty message", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        messageList = new ArrayList<>();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}