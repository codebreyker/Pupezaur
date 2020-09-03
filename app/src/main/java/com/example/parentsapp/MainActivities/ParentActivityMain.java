package com.example.parentsapp.MainActivities;

import android.content.Intent;
import android.os.Bundle;

import com.example.parentsapp.PhoneConnections.UserPhoneRegister;
import com.example.pupezaur.MainActivities.SettingsActivity;
import com.example.pupezaur.R;
import com.example.pupezaur.Utils.AllMethods;
import com.example.pupezaur.Utils.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.parentsapp.ui.FragmentHolder.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParentActivityMain extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    User user;
    boolean isAdmin;

    TextView start_timer, end_timer;
    FloatingActionButton btn_add;
    FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = new User();

        //Posibil (SIGUR) sa crape daca nu exista in baza de date un user cu proprietatea isAdmin

//        databaseReference.child("Users").child(firebaseUser.getUid()).child("isAdmin").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String admin = snapshot.getValue().toString();
//                System.err.println(user + "+++++++++++++++++++++++++");
//                if (user.equals("1")) {
//                    isAdmin = true;
//                } else {
//                    isAdmin = false;
//                }
//                adminCheck();
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
    }

    void adminCheck(){
        if (isAdmin){
            //daca e admin, ecranul va afista casute pentru a creea programul propriu
            //de asemenea o lista cu programerile pe care le are
            System.out.println("ADMIN !!!!!!!!!!!!!!!!!!!!");
//            textView.append("ADMIN");
        }else {
            //altfel e un user oarecare, care isi poate creea o programare
            // si va vedea o lista cu propriile programari.
            System.out.println("USER !!!!!!!!!!!!!!!!!!!!");
//            textView.append("USER");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = auth.getCurrentUser();
        user.setUid(currentUser.getUid());

        database.getReference("Users").orderByChild(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                user.setUid(currentUser.getUid());
                AllMethods.name = user.getName();
//                Log.e(TAG, "onDataChange: "+ AllMethods.name );
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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
            Intent intent = new Intent(ParentActivityMain.this, UserChatActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return false;
        }

//            buton setari
        if (id == R.id.btn_settings){
            Intent intent = new Intent (ParentActivityMain.this, SettingsActivity.class);
            startActivity(intent);
            return false;
        }
//            buton logout
        if (id == R.id.btn_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent (ParentActivityMain.this, UserPhoneRegister.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}