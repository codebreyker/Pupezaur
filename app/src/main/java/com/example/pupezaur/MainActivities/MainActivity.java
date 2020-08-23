package com.example.pupezaur.MainActivities;

import android.app.TimePickerDialog;
import android.content.Intent;;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.pupezaur.Fragments.FragmentMonday;
import com.example.pupezaur.Fragments.FragmentTuesday;
import com.example.pupezaur.Fragments.TimePickerFragment;
import com.example.pupezaur.PhoneConnection.PhoneSignin;
import com.example.pupezaur.PhoneConnection.UserPhoneRegister;
import com.example.pupezaur.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    boolean isAdmin;

    TextView start_timer, end_timer, textView;
    FloatingActionButton btn_add;
    Spinner dropdown_weekdays;

    FragmentMonday fragmentOne;
    FragmentTuesday fragmentTwo;
    FrameLayout fragmentContainer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        start_timer = findViewById(R.id.start_timer);
        end_timer = findViewById(R.id.end_timer);
        btn_add = findViewById(R.id.btn_add);
        textView = findViewById(R.id.textView);
        dropdown_weekdays = findViewById(R.id.dropdown_weekdays);

        fragmentOne = new FragmentMonday();
        fragmentTwo = new FragmentTuesday();
        fragmentContainer = findViewById(R.id.main_frame);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.weekdays_array, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown_weekdays.setAdapter(adapter);
        dropdown_weekdays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FragmentMonday()).commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FragmentTuesday()).commit();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback = "for_start_time";
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        //Posibil (SIGUR) sa crape daca nu exista in baza de date un user cu proprietatea isAdmin

//        databaseReference.child("Admin").child(firebaseUser.getPhoneNumber()).child("isAdmin").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String admin = snapshot.getValue().toString();
//                System.err.println(admin + "+++++++++++++++++++++++++");
//                if (admin.equals("1")) {
//                    isAdmin = true;
//                } else {
//                    isAdmin = false;
//                }
//                adminCheck();
//            }
//
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
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                startActivity(new Intent (MainActivity.this, UserPhoneRegister.class));
                finish();
            }

            return super.onOptionsItemSelected(item);
        }

    String callback = "";
    @Override
    public void onTimeSet(TimePicker timePicker, final int hour, final int minute) {
        if (TextUtils.isEmpty(callback))
            return;

        if (callback.equalsIgnoreCase("for_start_time")){
            // set in mTimePicker
            SimpleDateFormat f24Hour = new SimpleDateFormat("HH:mm");
            try {
                Date date = f24Hour.parse(hour + ":" + minute);
                start_timer.setText(f24Hour.format(date) + " - ");
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            start_timer.setText(hour + ":" + minute + " - ");
            DialogFragment timePicker1 = new TimePickerFragment();
            timePicker1.show(getSupportFragmentManager(), "time picker");
        }
        else if (callback.equalsIgnoreCase("for_end_time")){
            SimpleDateFormat f24Hour = new SimpleDateFormat("HH:mm");
            try {
                Date date = f24Hour.parse(hour + ":" + minute);
                end_timer.setText(f24Hour.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // set in mTimePicker1
//            end_timer.setText(hour + ":" + minute);
        }
        //Dont forgot to reset callback
        callback = "for_end_time";
        }
}


