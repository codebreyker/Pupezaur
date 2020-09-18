package com.example.pupezaur.MainActivities;

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
import com.example.pupezaur.Utils.Admin;
import com.example.pupezaur.Utils.AllMethods;
import com.example.pupezaur.Utils.Message;
import com.example.pupezaur.Utils.AdminMessageAdaptor;
import com.example.pupezaur.Utils.User;
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

public class AdminChatActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    RecyclerView recyclerView;
    EditText textSend;
    ImageButton btnSend;

    List<String> notiKeyList;
    List<Message> messageList;

    Admin admin;
    User user;

    String currentUserPhone, notiKey;
    boolean notify = false;
    int pendingNotif;

    AdminMessageAdaptor messageAdaptor;
    ValueEventListener seenListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        currentUserPhone = auth.getCurrentUser().getPhoneNumber();
        database = FirebaseDatabase.getInstance();

        admin = new Admin();
        user = new User();

        pendingNotif = 0;

        notiKeyList = new ArrayList<String>();
        messageList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        textSend = findViewById(R.id.text_send);
        btnSend = findViewById(R.id.btn_send);

        btnSend.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseUser currentUser = auth.getCurrentUser();
        admin.setUid(currentUser.getPhoneNumber());

        database.getReference("Admin").child(currentUser.getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                admin = dataSnapshot.getValue(Admin.class);
                admin.setUid(currentUser.getPhoneNumber());
                AllMethods.name = admin.getName();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

//        notification key de la useri
        database.getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    if (snapshot.hasChildren()) {
                        if (datasnapshot.child("adminPhoneNumber").getValue().toString().equals(admin.getPhoneNumber())) {
                            notiKey = datasnapshot.child("notificationKey").getValue().toString();
                            notiKeyList.add(notiKey);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

//        adaugarea mesajelor in baza de date
        databaseReference = database.getReference("Chats").child(currentUser.getPhoneNumber());
        databaseReference.keepSynced(true);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
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

        seenMessage();
    }

    //    afisarea mesajelor
    private void displayMessages(List<Message> messages) {
//        recyclerView.setHasFixedSize(true);
//        this.recyclerView.scrollTo(0, this.recyclerView.getBottom());

        recyclerView.setLayoutManager(new LinearLayoutManager(AdminChatActivity.this));
        messageAdaptor = new AdminMessageAdaptor(AdminChatActivity.this, messages, databaseReference);

        messageAdaptor.notifyItemInserted(messageList.size() - 1);
        this.recyclerView.scrollToPosition(messageAdaptor.getItemCount() - 1);
        ((LinearLayoutManager) recyclerView.getLayoutManager()).setStackFromEnd(true);

        this.recyclerView.setAdapter(messageAdaptor);
    }

    //    butonul de trimitere a mesajelor
    @Override
    public void onClick(View view) {
        String emptyMsg = getResources().getString(R.string.emptyMsg);
        notify = true;
        if (!TextUtils.isEmpty(textSend.getText().toString())) {
            Message message = new Message(textSend.getText().toString(), admin.getName(), admin.getUid(), false);
            textSend.setText("");
            databaseReference.push().setValue(message);

            for (String notKey : notiKeyList) {
                if (!notKey.equals(admin.getNotificationKey()))
                    new SendNotification(message.getName(), message.getMessage(), notKey);
            }
        } else {
            Toast.makeText(this, emptyMsg, Toast.LENGTH_SHORT).show();
        }
    }

    //    seen delivered
    private void seenMessage() {
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Chats").child(currentUserPhone);
        seenListener = db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if (!message.getUserId().equals(currentUserPhone)) {
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