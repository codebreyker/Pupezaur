package com.example.pupezaur.MainActivities;

import android.app.TimePickerDialog;
import android.content.Intent;;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parentsapp.MainActivities.UserChatActivity;
import com.example.pupezaur.Fragments.FragmentMonday;
import com.example.pupezaur.Fragments.FragmentTuesday;
import com.example.pupezaur.Fragments.TimePickerFragment;
import com.example.parentsapp.PhoneConnections.UserPhoneRegister;
import com.example.pupezaur.PhoneConnection.AdminPhoneRegister;
import com.example.pupezaur.R;
import com.example.pupezaur.Utils.Admin;
import com.example.pupezaur.Utils.AllMethods;
import com.example.pupezaur.Utils.ScheduleAdapter;
import com.example.pupezaur.Utils.ScheduleUtil;
import com.example.pupezaur.Utils.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    Admin admin;
    boolean isAdmin;

    TextView start_timer, end_timer, textView;
    FloatingActionButton btn_add;
    Spinner dropdown_weekdays;

    FrameLayout fragmentContainer, monday_frame;
    LinearLayout placeHolder;
    RecyclerView mondayRecycleView;

    String startTimeString, endTimeString, day;
    View view, view2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        admin = new Admin();

        start_timer = findViewById(R.id.start_timer);
        end_timer = findViewById(R.id.end_timer);
        btn_add = findViewById(R.id.btn_add);

        dropdown_weekdays = findViewById(R.id.dropdown_weekdays);
        fragmentContainer = findViewById(R.id.main_frame);
        monday_frame = findViewById(R.id.monday_frame);
        mondayRecycleView = findViewById(R.id.mondayRecycleView);

        view = getLayoutInflater().inflate(R.layout.schedule_item, monday_frame, true);
        view2 = getLayoutInflater().inflate(R.layout.schedule_item, monday_frame, true);
        placeHolder = view.findViewById(R.id.layoutToAdd);
        placeHolder.removeView(view);

        startTimeString="";
        endTimeString="";

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.weekdays_array, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown_weekdays.setAdapter(adapter);
        dropdown_weekdays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        day = "Monday";
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FragmentMonday(), "FragmentMonday").commit();
                        break;
                    case 1:
                        day = "Tuesday";
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FragmentTuesday(), "FragmentTuesday").commit();
//                        view = getLayoutInflater().inflate(R.layout.schedule_item, mondayRecycleView, true);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LinearLayout placeHolder;
//                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                getLayoutInflater().inflate(R.layout.schedule_item, placeHolder, false);
                if(startTimeString.isEmpty() || endTimeString.isEmpty()){
//
                }else {
                    placeHolder.addView(getLayoutInflater().inflate(R.layout.schedule_item, null, false));
                    startTimeString = "";
                    endTimeString = "";
                    callback = "for_start_time";
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                }
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
        final FirebaseUser currentUser = auth.getCurrentUser();
        admin.setUid(currentUser.getPhoneNumber());

//        database.getReference("Users").orderByChild(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener(){
        database.getReference("Admin").child(currentUser.getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                admin = dataSnapshot.getValue(Admin.class);
                admin.setUid(currentUser.getPhoneNumber());
                AllMethods.name = admin.getName();
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
                startActivity(new Intent (MainActivity.this, AdminPhoneRegister.class));
                finish();
            }
            return super.onOptionsItemSelected(item);
        }

    String callback = "";
    @Override
    public void onTimeSet(TimePicker timePicker, final int hour, final int minute) {
        FragmentMonday fragmentMonday = (FragmentMonday) getSupportFragmentManager().findFragmentByTag("FragmentMonday");
        if (TextUtils.isEmpty(callback))
            return;

            if (callback.equalsIgnoreCase("for_start_time")) {
                // set in mTimePicker
                SimpleDateFormat f24Hour = new SimpleDateFormat("HH:mm");
                try {
                    Date date = f24Hour.parse(hour + ":" + minute);
                    startTimeString = f24Hour.format(date);
                    fragmentMonday.startHour(startTimeString, placeHolder);
//                start_timer.setText(f24Hour.format(date) + " - ");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//            start_timer.setText(hour + ":" + minute + " - ");
                DialogFragment timePicker1 = new TimePickerFragment();
                timePicker1.show(getSupportFragmentManager(), "time picker");
            } else if (callback.equalsIgnoreCase("for_end_time")) {
                SimpleDateFormat f24Hour = new SimpleDateFormat("HH:mm");
                try {
                    Date date = f24Hour.parse(hour + ":" + minute);
                    endTimeString = f24Hour.format(date);
                    fragmentMonday.endHour(f24Hour.format(date), placeHolder);
//                end_timer.setText(f24Hour.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // set in mTimePicker1
//            end_timer.setText(hour + ":" + minute);
            }
            //Dont forgot to reset callback
            callback = "for_end_time";
    }

    public void onClick(View view) {
        callback = "for_start_time";
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }
}


