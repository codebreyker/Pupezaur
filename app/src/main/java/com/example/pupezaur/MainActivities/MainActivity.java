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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference ;

    boolean isAdmin;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth=FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        databaseReference=FirebaseDatabase.getInstance().getReference();


        //Posibil (SIGUR) sa crape daca nu exista in baza de date un user cu proprietatea isAdmin

//        String admin=  databaseReference.child("Users").child(firebaseUser.getUid()).child("isAdmin").toString();
//        if(admin.equals("1")){
//            isAdmin = true;
//        }
//        else {
//            isAdmin = false;
//        }

        if (isAdmin){
            //daca e admin, ecranul va afista casute pentru a creea programul propriu
            //de asemenea o lista cu programerile pe care le are
        }else {
            //altfel e un user oarecare, care isi poate creea o programare
            // si va vedea o lista cu propriile programari.
        }

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
