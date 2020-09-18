package com.example.parentsapp.MainActivities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pupezaur.Notifications.SendNotification;
import com.example.pupezaur.R;
import com.example.pupezaur.Utils.AllMethods;
import com.example.pupezaur.Utils.Message;
import com.example.pupezaur.Utils.User;
import com.example.pupezaur.Utils.UserMessageAdapter;
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
import java.util.Locale;

public class UserChatActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    UserMessageAdapter messageAdapter;
    User u;

    List<String> notiKeyList;
    List<Message> messageList;

    RecyclerView recyclerView;
    EditText textSend;
    ImageButton btnSend;

    String adminId, notiKey, notiKeyAdmin;
    private String currentUserName;

    ValueEventListener seenListener;
    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        currentUserName = auth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();

        u = new User();

        messageList = new ArrayList<>();
        notiKeyList = new ArrayList<String>();

        recyclerView = findViewById(R.id.recycler_view);
        textSend = findViewById(R.id.text_send);
        btnSend = findViewById(R.id.btn_send);

        btnSend.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = auth.getCurrentUser();
        u.setUid(currentUser.getUid());

        database.getReference("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                u = dataSnapshot.getValue(User.class);
                u.setUid(currentUser.getUid());
                AllMethods.name = u.getName();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

//        notification key admin
        database.getReference("Users").child(currentUser.getUid()).child("adminPhoneNumber").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    adminId = snapshot.getValue().toString();
                    database.getReference("Admin").child(adminId).child("notificationKey").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                notiKeyAdmin = snapshot.getValue().toString();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

//        notification key ceilalti useri
        database.getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    if (datasnapshot.child("adminPhoneNumber").getValue().toString().equals(adminId)) {
                        notiKey = datasnapshot.child("notificationKey").getValue().toString();
                        notiKeyList.add(notiKey);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

//        adaugarea mesajelor in baza de date
        database.getReference("Users").child(currentUser.getUid()).child("adminPhoneNumber").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adminId = snapshot.getValue().toString();

                databaseReference = database.getReference("Chats").child(adminId);
                databaseReference.keepSynced(true);

                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final Message message = dataSnapshot.getValue(Message.class);
                        message.setKey(dataSnapshot.getKey());
                        messageList.add(message);
                        displayMessages(messageList);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Message message = dataSnapshot.getValue(Message.class);
                        message.setKey(dataSnapshot.getKey());
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        seenMessage();
    }

    //    butonul de trimitere a mesajelor
    @Override
    public void onClick(View view) {
        String emptyMsg = getResources().getString(R.string.emptyMsg);
        if (!TextUtils.isEmpty(textSend.getText().toString())) {
            Message message = new Message(textSend.getText().toString(), u.getName(), u.getUid(), false);
            textSend.setText("");
            databaseReference.push().setValue(message);

            new SendNotification(message.getName(), message.getMessage(), notiKeyAdmin);

            for (String notKey : notiKeyList) {
                if (!notKey.equals(u.getNotificationKey())) {
                    new SendNotification(message.getName(), message.getMessage(), notKey);
                }
            }
        } else {
            Toast.makeText(this, emptyMsg, Toast.LENGTH_SHORT).show();
        }
    }

    //    afisarea mesajelor
    private void displayMessages(List<Message> messages) {
//        recyclerView.setHasFixedSize(true);
//        this.recyclerView.scrollTo(0, this.recyclerView.getBottom());

        recyclerView.setLayoutManager(new LinearLayoutManager(UserChatActivity.this));
        messageAdapter = new UserMessageAdapter(UserChatActivity.this, messages, databaseReference);

        messageAdapter.notifyItemInserted(messageList.size() - 1);
        this.recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
        ((LinearLayoutManager) recyclerView.getLayoutManager()).setStackFromEnd(true);

        this.recyclerView.setAdapter(messageAdapter);
    }

    //    seen delivered
    private void seenMessage() {
        database.getReference("Users").child(u.getUid()).child("adminPhoneNumber").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adminId = snapshot.getValue().toString();
                final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Chats").child(adminId);
                seenListener = db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Message message = dataSnapshot.getValue(Message.class);
                            if (!message.getUserId().equals(u.getUid())) {
                                message.setSeen(true);
                                db.child(dataSnapshot.getKey()).child("seen").setValue(message.isSeen());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        messageList = new ArrayList<>();
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.removeEventListener(seenListener);
    }

    //    butonul de back de sus, din toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return super.onOptionsItemSelected(item);
    }

    //    butonul de back de jos, al telefonului
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}