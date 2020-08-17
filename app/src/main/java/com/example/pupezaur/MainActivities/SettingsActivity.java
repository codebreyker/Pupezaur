package com.example.pupezaur.MainActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//import com.example.pupezaur.Fragment.MessageActivity;
import com.example.pupezaur.Fragment.MessageActivity;
import com.example.pupezaur.R;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

//        btn_logout = findViewById(R.id.btn_logout);
//        btn_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent (SettingsActivity.this, LoginActivity.class));
//                finish();
//            }
//        });
//
//        groupchat = findViewById(R.id.groupchat);
//        groupchat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent (SettingsActivity.this, MessageActivity.class));
//                finish();
//            }
//        });
    }

}