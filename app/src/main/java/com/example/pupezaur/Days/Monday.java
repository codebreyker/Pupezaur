package com.example.pupezaur.Days;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pupezaur.Fragments.FragmentTime;
import com.example.pupezaur.Fragments.TimePickerFragmentEnd;
import com.example.pupezaur.Fragments.TimePickerFragmentStart;
import com.example.pupezaur.MainActivities.ChatActivity;
import com.example.pupezaur.MainActivities.SettingsActivity;
import com.example.pupezaur.PhoneConnection.AdminPhoneRegister;
import com.example.pupezaur.R;
import com.example.pupezaur.Utils.Admin;
import com.example.pupezaur.Utils.AllMethods;
import com.example.pupezaur.Utils.ScheduleAdapter;
import com.example.pupezaur.Utils.Schedule;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Monday extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    Admin admin;
    boolean isAdmin;

    TextView start_timer, end_timer;
    FloatingActionButton btn_add, btn_monday, btn_tuesday, btn_wednesday, btn_thursday, btn_friday, btn_saturday, btn_sunday;

    FrameLayout fragmentContainer;
    LinearLayout placeHolder;
    FragmentTime fragmentTime;
    RecyclerView schedule_recyclerview;

    String startTimeString, endTimeString;
    List<Schedule> scheduleList;
    ScheduleAdapter sAdapter;
    View v;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        admin = new Admin();

//        start_timer = findViewById(R.id.start_timer);
//        end_timer = findViewById(R.id.end_timer);
        placeHolder = findViewById(R.id.layoutToAdd);

        btn_add = findViewById(R.id.btn_add);
        btn_monday = findViewById(R.id.btn_monday);
        btn_tuesday = findViewById(R.id.btn_tuesday);
        btn_wednesday = findViewById(R.id.btn_wednesday);
        btn_thursday = findViewById(R.id.btn_thursday);
        btn_friday = findViewById(R.id.btn_friday);
        btn_saturday = findViewById(R.id.btn_saturday);
        btn_sunday = findViewById(R.id.btn_sunday);

        scheduleList = new ArrayList<Schedule>();

//        schedule_recyclerview = findViewById(R.id.schedule_recyclerview);

        fragmentContainer = findViewById(R.id.main_frame);

        fragmentTime = new FragmentTime();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FragmentTime(), "FragmentTime").commit();
//        View child = getLayoutInflater().inflate(R.layout.schedule_item, null);
//        fragmentContainer.addView(child);

        v = getLayoutInflater().inflate(R.layout.schedule_item, fragmentContainer, true);
        placeHolder = v.findViewById(R.id.layoutToAdd);
        placeHolder.removeView(v);

        startTimeString = "";
        endTimeString = "";

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((startTimeString.isEmpty() || endTimeString.isEmpty()) && !scheduleList.isEmpty()) {
                    Toast.makeText(Monday.this, "Please select start hour", Toast.LENGTH_SHORT).show();
                } else {
                    placeHolder.addView(getLayoutInflater().inflate(R.layout.schedule_item, null, false));
                    startTimeString = "";
                    endTimeString = "";
                    callback = "for_start_time";
                    DialogFragment timePicker = new TimePickerFragmentStart();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                }
                if (startTimeString.isEmpty() || endTimeString.isEmpty()){

                }
            }
        });

        //Posibil (SIGUR) sa crape daca nu exista in baza de date un user cu proprietatea isAdmin

//    databaseReference.child("Admin").child(firebaseUser.getPhoneNumber()).child("isAdmin").addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot snapshot) {
//            String admin = snapshot.getValue().toString();
//            System.err.println(admin + "+++++++++++++++++++++++++");
//            if (admin.equals("1")) {
//                isAdmin = true;
//            } else {
//                isAdmin = false;
//            }
//            adminCheck();
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//        }
//    });

//        //        butoanele cu zile
//        btn_monday.setSupportBackgroundTintList(ContextCompat.getColorStateList(Monday.this, R.color.BaseTurquoise));
//        btn_tuesday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Monday.this, Tuesday.class));
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                finish();
//            }
//        });
//        btn_wednesday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Monday.this, Wednesday.class));
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                finish();
//            }
//        });
//        btn_thursday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Monday.this, Thursday.class));
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                finish();
//            }
//        });
//        btn_friday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Monday.this, Friday.class));
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                finish();
//            }
//        });
//        btn_saturday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Monday.this, Saturday.class));
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                finish();
//            }
//        });
//        btn_sunday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Monday.this, Sunday.class));
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                finish();
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
        admin.setUid(currentUser.getPhoneNumber());

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

        databaseReference = database.getReference("Schedule").child(firebaseUser.getPhoneNumber()).child("Monday");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Schedule schedule = dataSnapshot.getValue(Schedule.class);
                    schedule.setId(dataSnapshot.getKey());
                    scheduleList.add(schedule);
                    System.err.println(schedule);

                }
                Collections.sort(scheduleList, new Comparator<Schedule>() {
                    @Override
                    public int compare(Schedule schedule, Schedule t1) {
                        return schedule.getStartHour().compareTo(t1.getStartHour());
                    }
                });
                placeHolder.removeAllViews();
                for (int i=0; i<snapshot.getChildrenCount(); i++ ) {
                    placeHolder.addView(getLayoutInflater().inflate(R.layout.schedule_item, null, false));
                    System.out.println(i);
                    View view = placeHolder.getChildAt(placeHolder.getChildCount()-1);
                    TextView startTimer = view.findViewById(R.id.start_timer);
                    startTimeString = scheduleList.get(i).getStartHour();
                    startTimer.setText(startTimeString + " -");
                    TextView endTimer = view.findViewById(R.id.end_timer);
                    endTimeString = scheduleList.get(i).getEndHour();
                    endTimer.setText("- " +  endTimeString);
                }
//                System.err.println(scheduleList);
                scheduleList.removeAll(scheduleList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        databaseReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Schedule schedule = dataSnapshot.getValue(Schedule.class);
////                schedule.setKey(dataSnapshot.getKey());
//                scheduleList.add(schedule);
////                displaySchedule(scheduleList);
//                System.err.println(scheduleList);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
////                Schedule schedule = dataSnapshot.getValue(Schedule.class);
//////                schedule.setKey(dataSnapshot.getKey());
//////                Toast.makeText(DashboardActivity.this, "Message changed...", Toast.LENGTH_SHORT).show();
////                List<Schedule> newSchedule = new ArrayList<>();
////                for (Schedule c : scheduleList) {
////                    if (c.getKey().equals(schedule.getKey())) {
////                        newSchedule.add(schedule);
////                    } else {
////                        newSchedule.add(c);
////                    }
////                }
////                scheduleList = newSchedule;
//////                displaySchedule(scheduleList);
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
////                Schedule schedule = dataSnapshot.getValue(Schedule.class);
//////                schedule.setKey(dataSnapshot.getKey());
//////                Toast.makeText(DashboardActivity.this, "Message deleted...", Toast.LENGTH_SHORT).show();
////                List<Schedule> newSchedule = new ArrayList<>();
////                for (Schedule c : scheduleList) {
////                    if (!c.getKey().equals(schedule.getKey())) {
////                        newSchedule.add(schedule);
////                    }
////                }
////                scheduleList = newSchedule;
//////                displaySchedule(scheduleList);
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
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
                Intent intent = new Intent(Monday.this, ChatActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return false;
            }
//            buton setari
            if (id == R.id.btn_settings){
                Intent intent = new Intent (Monday.this, SettingsActivity.class);
                startActivity(intent);
                return false;
            }
//            buton logout
            if (id == R.id.btn_logout) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent (Monday.this, AdminPhoneRegister.class));
                finish();
            }
            return super.onOptionsItemSelected(item);
        }

    String callback = "";
    @Override
    public void onTimeSet(TimePicker timePicker, final int hour, final int minute) {
        fragmentTime = (FragmentTime) getSupportFragmentManager().findFragmentByTag("FragmentTime");
        if (TextUtils.isEmpty(callback))
            return;

        if (callback.equalsIgnoreCase("for_start_time")) {
            // set in mTimePicker
            SimpleDateFormat f24Hour = new SimpleDateFormat("HH:mm");
            try {
                Date date = f24Hour.parse(hour + ":" + minute);
                startTimeString = f24Hour.format(date);
                fragmentTime.startHour(startTimeString + " -", placeHolder);
//                start_timer.setText(f24Hour.format(date) + " - ");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            DialogFragment timePicker1 = new TimePickerFragmentEnd();
            timePicker1.show(getSupportFragmentManager(), "time picker");
        } else if (callback.equalsIgnoreCase("for_end_time")) {
            SimpleDateFormat f24Hour = new SimpleDateFormat("HH:mm");
            try {
                Date date = f24Hour.parse(hour + ":" + minute);
                endTimeString = f24Hour.format(date);
                fragmentTime.endHour("- " + endTimeString, placeHolder);
//                end_timer.setText(f24Hour.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // set in mTimePicker1
        }
        if (!startTimeString.isEmpty() && !endTimeString.isEmpty()){
            Schedule schedule = new Schedule(startTimeString, endTimeString);
            databaseReference.push().setValue(schedule);
        }

        //Dont forgot to reset callback
        callback = "for_end_time";

        if (endTimeString.isEmpty() && !scheduleList.isEmpty()){
            placeHolder.removeViewAt(placeHolder.getChildCount()-1);
            return;
//            startTimeString = "1";
//            endTimeString = "1";
        }
//
        if (startTimeString.isEmpty() && !scheduleList.isEmpty()) {
            placeHolder.removeViewAt(placeHolder.getChildCount()-1);
            return;
//            startTimeString = "1";
//            endTimeString = "1";
        }
    }

    public void onClick(View view) {
//        callback = "for_start_time";
//        DialogFragment timePicker = new TimePickerFragmentStart();
//        timePicker.show(getSupportFragmentManager(), "time picker");
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        scheduleList = new ArrayList<>();
//    }

//    public void displaySchedule (List<Schedule> schedule) {
//        schedule_recyclerview.setLayoutManager(new LinearLayoutManager(Monday.this));
//        schedule_recyclerview.setHasFixedSize(true);
//        sAdapter = new ScheduleAdapter(Monday.this, schedule, databaseReference);
////        this.recyclerView.scrollTo(0, this.recyclerView.getBottom());
//        sAdapter.notifyItemInserted(scheduleList.size() - 1);
//        this.schedule_recyclerview.scrollToPosition(sAdapter.getItemCount() - 1);
//        ((LinearLayoutManager) schedule_recyclerview.getLayoutManager()).setStackFromEnd(true);
//        this.schedule_recyclerview.setAdapter(sAdapter);
//    }
}


