package com.example.pupezaur.Days;

import android.app.AlertDialog;
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

import com.example.pupezaur.Fragments.FragmentTime;
import com.example.pupezaur.Fragments.TimePickerFragmentEnd;
import com.example.pupezaur.Fragments.TimePickerFragmentStart;
import com.example.pupezaur.MainActivities.AdminChatActivity;
import com.example.pupezaur.MainActivities.SettingsActivity;
import com.example.pupezaur.PhoneConnection.AdminPhoneRegister;
import com.example.pupezaur.R;
import com.example.pupezaur.Utils.Admin;
import com.example.pupezaur.Utils.AllMethods;
import com.example.pupezaur.Fragments.OnSwipeTouchListener;
import com.example.pupezaur.Utils.Schedule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ThursdayAdmin extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseDatabase database;

    Admin admin;
    boolean isAdmin, isEditingSchedule;

    FloatingActionButton btn_add;
    TextView btn_monday, btn_tuesday, btn_wednesday, btn_thursday, btn_friday, btn_saturday, btn_sunday;

    FrameLayout fragmentContainer;
    LinearLayout placeHolder, linearLayout_delete, linearLayout_edit;
    FragmentTime fragmentTime;

    String startTimeString, endTimeString;
    List<Schedule> scheduleList;
    View v;
    int idToEdit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseDatabase.getInstance().getReference("Schedule").child(firebaseUser.getPhoneNumber()).child("Thursday").keepSynced(true);

        admin = new Admin();
        scheduleList = new ArrayList<Schedule>();

        btn_add = findViewById(R.id.btn_add);
        btn_monday = findViewById(R.id.btn_monday);
        btn_tuesday = findViewById(R.id.btn_tuesday);
        btn_wednesday = findViewById(R.id.btn_wednesday);
        btn_thursday = findViewById(R.id.btn_thursday);
        btn_friday = findViewById(R.id.btn_friday);
        btn_saturday = findViewById(R.id.btn_saturday);
        btn_sunday = findViewById(R.id.btn_sunday);

        linearLayout_edit = findViewById(R.id.linearLayout_edit);
        linearLayout_delete = findViewById(R.id.linearLayout_delete);

        placeHolder = findViewById(R.id.layoutToAdd);
        fragmentContainer = findViewById(R.id.main_frame);
        fragmentTime = new FragmentTime();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FragmentTime(), "FragmentTime").commit();

        v = getLayoutInflater().inflate(R.layout.admin_schedule_item, fragmentContainer, true);
        placeHolder = v.findViewById(R.id.layoutToAdd);
        placeHolder.removeView(v);

        startTimeString = "";
        endTimeString = "";
        isEditingSchedule = false;

        v.setOnTouchListener(new OnSwipeTouchListener(ThursdayAdmin.this){
            @Override
            public void onSwipeLeft(){
                super.onSwipeLeft();
                startActivity(new Intent(ThursdayAdmin.this, FridayAdmin.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                startActivity(new Intent(ThursdayAdmin.this, WednesdayAdmin.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        }) ;

//        buton adaugat cursuri
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((startTimeString.isEmpty() || endTimeString.isEmpty()) && !scheduleList.isEmpty()) {
                    Toast.makeText(ThursdayAdmin.this, "Please select start hour", Toast.LENGTH_SHORT).show();
                } else {
                    placeHolder.addView(getLayoutInflater().inflate(R.layout.admin_schedule_item, null, false));
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

        //        butoanele cu zile
        btn_thursday.setBackgroundResource(R.drawable.btns_days_blue);
        btn_thursday.setTextColor(getResources().getColor(R.color.Black));
        btn_monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ThursdayAdmin.this, MondayAdmin.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        btn_tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ThursdayAdmin.this, TuesdayAdmin.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        btn_wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ThursdayAdmin.this, WednesdayAdmin.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        btn_friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ThursdayAdmin.this, FridayAdmin.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        btn_saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ThursdayAdmin.this, SaturdayAdmin.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        btn_sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ThursdayAdmin.this, SundayAdmin.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        //Posibil (SIGUR) sa crape daca nu exista in baza de date un user cu proprietatea isAdmin
        databaseReference.child("Admin").child(firebaseUser.getPhoneNumber()).child("isAdmin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String admin = snapshot.getValue().toString();
                System.err.println(admin + "+++++++++++++++++++++++++");
                if (admin.equals("1")) {
                    isAdmin = true;
                } else {
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

        //        adauga lista cu programe din baza de date
        databaseReference = database.getReference("Schedule").child(firebaseUser.getPhoneNumber()).child("Thursday");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scheduleList.removeAll(scheduleList);

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Schedule schedule = dataSnapshot.getValue(Schedule.class);
                    schedule.setId(dataSnapshot.getKey());
                    schedule.setNamesList(new ArrayList<String>());

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        if (dataSnapshot1.getKey().equals("names")){
                            for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                                schedule.getNamesList().add(dataSnapshot2.getValue().toString());
                            }
                        }
                    }
                    scheduleList.add(schedule);
                }
                Collections.sort(scheduleList, new Comparator<Schedule>() {
                    @Override
                    public int compare(Schedule schedule, Schedule t1) {
                        return schedule.getStartHour().compareTo(t1.getStartHour());
                    }
                });
                placeHolder.removeAllViews();
                for (int i=0; i<snapshot.getChildrenCount(); i++ ) {
                    placeHolder.addView(getLayoutInflater().inflate(R.layout.admin_schedule_item, null, false));
                    View view = placeHolder.getChildAt(placeHolder.getChildCount()-1);

                    LinearLayout linearLayout_edit = view.findViewById(R.id.linearLayout_edit);
                    linearLayout_edit.setId(i);
                    linearLayout_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            isEditingSchedule = true;
                            for (int i=0; i < scheduleList.size(); i++){
                                if (view.getId() == i){
                                    idToEdit = view.getId();
                                }
                            }
                            callback = "for_start_time";
                            DialogFragment timePicker = new TimePickerFragmentStart();
                            timePicker.show(getSupportFragmentManager(), "time picker");
                        }
                    });

                    LinearLayout linearLayout_delete = view.findViewById(R.id.linearLayout_delete);
                    linearLayout_delete.setId(i);
                    linearLayout_delete.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            for (int i = 0; i < scheduleList.size(); i++) {
                                if (view.getId() == i) {
                                    String yes = getResources().getString(R.string.yes);
                                    String cancel = getResources().getString(R.string.cancel);
                                    CharSequence options[] = new CharSequence[]{
                                            yes,
                                            cancel
                                    };
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ThursdayAdmin.this);
                                    String delete = getResources().getString(R.string.alerDialog_deleteHour);
                                    final String isDeleted = getResources().getString(R.string.courseDeleted);
                                    final String notDeleted = getResources().getString(R.string.courseNotDeleted);
                                    builder.setTitle(delete);

                                    final int finalI = i;
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int j) {
                                            if (j == 0) {
                                                databaseReference = FirebaseDatabase.getInstance().getReference("Schedule").child(firebaseUser.getPhoneNumber()).child("Thursday");
                                                databaseReference.child(scheduleList.get(finalI).getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(ThursdayAdmin.this, isDeleted, Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(ThursdayAdmin.this, notDeleted, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                                return;
                                            }
                                        }
                                    });
                                    builder.show();
                                    return false;
                                }
                            }
                            return true;
                        }
                    });

                    TextView startTimer = view.findViewById(R.id.start_timer);
                    startTimeString = scheduleList.get(i).getStartHour();
                    startTimer.setText(startTimeString + " -");

                    TextView endTimer = view.findViewById(R.id.end_timer);
                    endTimeString = scheduleList.get(i).getEndHour();
                    endTimer.setText("- " +  endTimeString);

                    TextView txt_addName = view.findViewById(R.id.txt_nameToAdd);
                    for (int j = 0; j < scheduleList.get(i).getNamesList().size(); j++) {
                        if (j == 0) {
                            txt_addName.append(scheduleList.get(i).getNamesList().get(j));
                        } else {
                            txt_addName.append(", " + scheduleList.get(i).getNamesList().get(j));
                        }
                    }

                    TextView txt_hours = view.findViewById(R.id.txt_hours);
                    TextView txt_students = view.findViewById(R.id.txt_students);
                    TextView txt_nameToAdd = view.findViewById(R.id.txt_nameToAdd);
                    TextView start_timer = view.findViewById(R.id.start_timer);
                    TextView end_timer = view.findViewById(R.id.end_timer);
                    Calendar calendar = Calendar.getInstance();
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm" + dayOfWeek, Locale.getDefault());
                    String currentTime = simpleDateFormat.format(calendar.getTime());
                    if (currentTime.compareTo(endTimeString) < 0 && currentTime.compareTo(startTimeString) > 0 && dayOfWeek == Calendar.THURSDAY) {
                        txt_hours.setTextColor(getResources().getColor(R.color.AnotherBlue));
                        txt_students.setTextColor(getResources().getColor(R.color.AnotherBlue));
                        txt_nameToAdd.setTextColor(getResources().getColor(R.color.AnotherBlue));
                        start_timer.setTextColor(getResources().getColor(R.color.AnotherBlue));
                        end_timer.setTextColor(getResources().getColor(R.color.AnotherBlue));
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        if (id == R.id.chatButton) {
            Intent intent = new Intent(ThursdayAdmin.this, AdminChatActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return false;
        }
//            buton setari
        if (id == R.id.btn_settings){
            Intent intent = new Intent (ThursdayAdmin.this, SettingsActivity.class);
            startActivity(intent);
            return false;
        }
//            buton logout
        if (id == R.id.btn_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent (ThursdayAdmin.this, AdminPhoneRegister.class));
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
                if (!isEditingSchedule) {
                    fragmentTime.startHour(startTimeString + " -", placeHolder);
                }
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
                if (!isEditingSchedule) {
                    fragmentTime.endHour("- " + endTimeString, placeHolder);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            // set in mTimePicker1
        }
        if (!isEditingSchedule) {
            if (!startTimeString.isEmpty() && !endTimeString.isEmpty()) {
                System.out.println("pus in baza");
                Schedule schedule = new Schedule(startTimeString, endTimeString);
                databaseReference.push().setValue(schedule);
            }
        }

        if (isEditingSchedule && callback.equals("for_end_time")){
            databaseReference = FirebaseDatabase.getInstance().getReference("Schedule").child(firebaseUser.getPhoneNumber()).child("Thursday");
            databaseReference.child(scheduleList.get(idToEdit).getId()).child("startHour").setValue(startTimeString);
            databaseReference.child(scheduleList.get(idToEdit).getId()).child("endHour").setValue(endTimeString);
            isEditingSchedule = false;
        }

        //Dont forgot to reset callback
        callback = "for_end_time";

        if (endTimeString.isEmpty() && !scheduleList.isEmpty()){
            placeHolder.removeViewAt(placeHolder.getChildCount()-1);
            return;
        }

        if (startTimeString.isEmpty() && !scheduleList.isEmpty()) {
            placeHolder.removeViewAt(placeHolder.getChildCount()-1);
            return;
        }

    }

}


