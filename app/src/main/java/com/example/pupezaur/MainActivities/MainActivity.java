package com.example.pupezaur.MainActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pupezaur.Fragment.MessageActivity;
import com.example.pupezaur.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

        //    meniul de setari
        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.settingsmenu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            int id = item.getItemId();
//            buton chat
            if (id == R.id.ChatButton) {
                Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(intent);
                return false;
            }
//            buton setari
            if (id == R.id.btn_settings){
                Intent intent = new Intent (MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return false;
            }
//            buton logout
            if (id == R.id.btn_logout) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent (MainActivity.this, LoginActivity.class));
                finish();
            }

            return super.onOptionsItemSelected(item);
        }

}

