package com.example.pupezaur;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.pupezaur.Util.Chat;
import com.example.pupezaur.Util.MessageAdaptor;
import com.example.pupezaur.Util.UserUtil;
import com.example.pupezaur.connections.ConnectionHandler;
import com.example.pupezaur.connections.SocketEventHandler;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pupezaur.ui.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    ConnectionHandler connectionHandler;
    static SocketEventHandler socketEventHandler;


    Intent intent;
    MessageAdaptor messageAdaptor;
    List<Chat> mChat;
    RecyclerView recyclerView;
    FirebaseUser firebaseUser;
    DatabaseReference dbreference;
    FirebaseDatabase database;
    EditText username;
    FirebaseAuth auth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        connectionHandler = new ConnectionHandler();
        connectionHandler.connectSocket();
        socketEventHandler = new SocketEventHandler(connectionHandler, this);
        socketEventHandler.doSocketEvents();

//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        linearLayoutManager.setStackFromEnd(true);
//        recyclerView.setLayoutManager(linearLayoutManager);

}

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settingsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.SettingsButton) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return false;
        }

        if(item.getItemId() == R.id.logout) {
            auth.signOut();
            finish();
            startActivity((new Intent(MainActivity.this,LoginActivity.class)));
        }
        return super.onOptionsItemSelected(item);
    }

//    public void updateMessage(){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                TextView chat = findViewById(R.id.show_message);
//                chat.append(socketEventHandler.getPersonName() + ": "+socketEventHandler.getMessage() + "\n");
//            }
//        });
//    }

//    private void readMessages (final String myid, final String userid) {
//        mChat = new ArrayList<>();
//        dbreference = FirebaseDatabase.getInstance().getReference("Chats");
//        dbreference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                mChat.clear();
//                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                    Chat chat = snapshot1.getValue(Chat.class);
//                    if (chat.getReceiver().equals(myid) || chat.getSender().equals(userid) ||
//                            chat.getReceiver().equals(userid) || chat.getSender().equals(myid)) {
//                        mChat.add(chat);
//                    }
//                }
//                messageAdaptor = new MessageAdaptor(MainActivity.this, mChat);
//                recyclerView.setAdapter(messageAdaptor);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }

}
