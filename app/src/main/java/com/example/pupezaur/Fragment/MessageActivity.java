package com.example.pupezaur.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pupezaur.R;
import com.example.pupezaur.ChatUtil.AllMethods;
import com.example.pupezaur.ChatUtil.Message;
import com.example.pupezaur.ChatUtil.MessageAdapter;
import com.example.pupezaur.ChatUtil.User;
import com.google.android.material.bottomappbar.BottomAppBar;
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

public class MessageActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MessageActivity";
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    MessageAdapter mAdapter;
    User u;
    List<Message> messageList;

    LinearLayoutManager mActivity;

    RecyclerView recyclerView;
    EditText textSend;
    ImageButton btnSend;
    private String currentUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        auth=FirebaseAuth.getInstance();
        currentUserName = auth.getCurrentUser().getUid();
        database=FirebaseDatabase.getInstance();
        u=new User();

        recyclerView=findViewById(R.id.recycler_view);
        textSend=findViewById(R.id.text_send);
        btnSend=findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
        messageList=new ArrayList<>();
        
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser=auth.getCurrentUser();
        u.setUid(currentUser.getUid());
        u.setEmail(currentUser.getEmail());
//        u.setName(currentUser.getDisplayName());
        database.getReference("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u=dataSnapshot.getValue(User.class);
                u.setUid(currentUser.getUid());
                AllMethods.name=u.getName();
//                Log.e(TAG, "onDataChange: "+ AllMethods.name );
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference=database.getReference("message");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message=dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
                messageList.add(message);
                displayMessages(messageList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Message message=dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
//                Toast.makeText(DashboardActivity.this, "Message changed...", Toast.LENGTH_SHORT).show();
                List<Message> newMessages=new ArrayList<>();
                for (Message m:messageList){
                    if(m.getKey().equals(message.getKey())){
                        newMessages.add(message);
                    }
                    else{
                        newMessages.add(m);
                    }
                }
                messageList=newMessages;
                displayMessages(messageList);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Message message=dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
//                Toast.makeText(DashboardActivity.this, "Message deleted...", Toast.LENGTH_SHORT).show();
                List<Message> newMessages=new ArrayList<>();
                for (Message m:messageList){
                    if(!m.getKey().equals(message.getKey())){
                        newMessages.add(m);
                    }
                }
                messageList=newMessages;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
        recyclerView.setHasFixedSize(true);
        mAdapter=new MessageAdapter(MessageActivity.this,messages,databaseReference);
        this.recyclerView.scrollTo(0, this.recyclerView.getBottom());
        mAdapter.notifyItemInserted(messageList.size()-1);
        this.recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
        ((LinearLayoutManager)recyclerView.getLayoutManager()).setStackFromEnd(true);
        this.recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View view) {
        if(!TextUtils.isEmpty(textSend.getText().toString())) {
            Message message = new Message(textSend.getText().toString(), u.getName());
            textSend.setText("");
            databaseReference.push().setValue(message);
        }
        else {
            Toast.makeText(this, "Please write message.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        messageList=new ArrayList<>();
    }
}