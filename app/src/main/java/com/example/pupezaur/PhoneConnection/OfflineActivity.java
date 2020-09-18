package com.example.pupezaur.PhoneConnection;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class OfflineActivity extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}