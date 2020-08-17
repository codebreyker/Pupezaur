package com.example.pupezaur.MainActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pupezaur.Fragment.MessageActivity;
import com.example.pupezaur.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference ;

    boolean isAdmin;

    TextView textView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        auth=FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        databaseReference=FirebaseDatabase.getInstance().getReference();

        textView = findViewById(R.id.textView);


        //Posibil (SIGUR) sa crape daca nu exista in baza de date un user cu proprietatea isAdmin


        databaseReference.child("Users").child(firebaseUser.getUid()).child("isAdmin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String admin = snapshot.getValue().toString();
                System.err.println(admin + "+++++++++++++++++++++++++");
                if(admin.equals("1")){
                    isAdmin = true;
                }
                else {
                    isAdmin = false;
                }
                adminCheck();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    void adminCheck(){
        if (isAdmin){
            //daca e admin, ecranul va afista casute pentru a creea programul propriu
            //de asemenea o lista cu programerile pe care le are
            System.out.println("ADMIN !!!!!!!!!!!!!!!!!!!!");
            textView.append("ADMIN");
        }else {
            //altfel e un user oarecare, care isi poate creea o programare
            // si va vedea o lista cu propriile programari.
            System.out.println("USER !!!!!!!!!!!!!!!!!!!!");
            textView.append("USER");
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

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

