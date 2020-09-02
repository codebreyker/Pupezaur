//package com.example.pupezaur.Days;
//
//import android.app.TimePickerDialog;
//import android.content.Intent;;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.TimePicker;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.DialogFragment;
//
//import com.example.pupezaur.Fragments.FragmentTime;
//import com.example.pupezaur.Fragments.TimePickerFragment;
//import com.example.pupezaur.MainActivities.ChatActivity;
//import com.example.pupezaur.MainActivities.SettingsActivity;
//import com.example.pupezaur.PhoneConnection.AdminPhoneRegister;
//import com.example.pupezaur.R;
//import com.example.pupezaur.Utils.Admin;
//import com.example.pupezaur.Utils.AllMethods;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class Saturday extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
//    FirebaseAuth auth;
//    FirebaseUser firebaseUser;
//    DatabaseReference databaseReference;
//    FirebaseDatabase database;
//    Admin admin;
//    boolean isAdmin;
//
//    TextView start_timer, end_timer, textView;
//    FloatingActionButton btn_add, btn_monday, btn_tuesday, btn_wednesday, btn_thursday, btn_friday, btn_saturday, btn_sunday;
//
//    FrameLayout fragmentContainer;
//    LinearLayout placeHolder;
//
//    String startTimeString, endTimeString;
//    View v;
//    FragmentTime fragmentTime;
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        auth = FirebaseAuth.getInstance();
//        firebaseUser = auth.getCurrentUser();
//        database = FirebaseDatabase.getInstance();
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//
//        admin = new Admin();
//
//        start_timer = findViewById(R.id.start_timer);
//        end_timer = findViewById(R.id.end_timer);
//        btn_add = findViewById(R.id.btn_add);
//        btn_monday = findViewById(R.id.btn_monday);
//        btn_tuesday = findViewById(R.id.btn_tuesday);
//        btn_wednesday = findViewById(R.id.btn_wednesday);
//        btn_thursday = findViewById(R.id.btn_thursday);
//        btn_friday = findViewById(R.id.btn_friday);
//        btn_saturday = findViewById(R.id.btn_saturday);
//        btn_sunday = findViewById(R.id.btn_sunday);
//
//        fragmentContainer = findViewById(R.id.main_frame);
//
//        fragmentTime = new FragmentTime();
//        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FragmentTime(), "FragmentTime").commit();
//        View child = getLayoutInflater().inflate(R.layout.schedule_item, null);
//        fragmentContainer.addView(child);
//
//        v = getLayoutInflater().inflate(R.layout.schedule_item, fragmentContainer, true);
//        placeHolder = v.findViewById(R.id.layoutToAdd);
//        placeHolder.removeView(v);
//
//        startTimeString="";
//        endTimeString="";
//
////        butoanele cu zile
//        btn_saturday.setSupportBackgroundTintList(ContextCompat.getColorStateList(Saturday.this, R.color.BaseTurquoise));
//
//        btn_monday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Saturday.this, Monday.class));
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                finish();
//            }
//        });
//        btn_tuesday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Saturday.this, Tuesday.class));
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                finish();
//            }
//        });
//        btn_wednesday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Saturday.this, Wednesday.class));
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                finish();
//            }
//        });
//        btn_thursday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Saturday.this, Thursday.class));
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                finish();
//            }
//        });
//        btn_friday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Saturday.this, Friday.class));
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                finish();
//            }
//        });
//        btn_sunday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Saturday.this, Sunday.class));
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                finish();
//            }
//        });
//
//
//        btn_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(startTimeString.isEmpty() || endTimeString.isEmpty()){
//
//                }else {
//                    placeHolder.addView(getLayoutInflater().inflate(R.layout.schedule_item, fragmentContainer, false));
//                    startTimeString = "";
//                    endTimeString = "";
//                    callback = "for_start_time";
//                    DialogFragment timePicker = new TimePickerFragment();
//                    timePicker.show(getSupportFragmentManager(), "time picker");
//                }
//            }
//        });
//
//        //Posibil (SIGUR) sa crape daca nu exista in baza de date un user cu proprietatea isAdmin
//
////        databaseReference.child("Admin").child(firebaseUser.getPhoneNumber()).child("isAdmin").addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                String admin = snapshot.getValue().toString();
////                System.err.println(admin + "+++++++++++++++++++++++++");
////                if (admin.equals("1")) {
////                    isAdmin = true;
////                } else {
////                    isAdmin = false;
////                }
////                adminCheck();
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////            }
////        });
//    }
//
//    void adminCheck(){
//        if (isAdmin){
//            //daca e admin, ecranul va afista casute pentru a creea programul propriu
//            //de asemenea o lista cu programerile pe care le are
//            System.out.println("ADMIN !!!!!!!!!!!!!!!!!!!!");
//            textView.append("ADMIN");
//        }else {
//            //altfel e un user oarecare, care isi poate creea o programare
//            // si va vedea o lista cu propriile programari.
//            System.out.println("USER !!!!!!!!!!!!!!!!!!!!");
//            textView.append("USER");
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        final FirebaseUser currentUser = auth.getCurrentUser();
//        admin.setUid(currentUser.getPhoneNumber());
//
////        database.getReference("Users").orderByChild(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener(){
//        database.getReference("Admin").child(currentUser.getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                admin = dataSnapshot.getValue(Admin.class);
//                admin.setUid(currentUser.getPhoneNumber());
//                AllMethods.name = admin.getName();
////                Log.e(TAG, "onDataChange: "+ AllMethods.name );
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    //    meniul de setari
//    @Override
//    public boolean onCreateOptionsMenu (Menu menu){
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.settingsmenu, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected (MenuItem item){
//        int id = item.getItemId();
////            buton chat
//        if (id == R.id.ChatButton) {
//            Intent intent = new Intent(Saturday.this, ChatActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            return false;
//        }
////            buton setari
//        if (id == R.id.btn_settings){
//            Intent intent = new Intent (Saturday.this, SettingsActivity.class);
//            startActivity(intent);
//            return false;
//        }
////            buton logout
//        if (id == R.id.btn_logout) {
//            FirebaseAuth.getInstance().signOut();
//            startActivity(new Intent (Saturday.this, AdminPhoneRegister.class));
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    String callback = "";
//    @Override
//    public void onTimeSet(TimePicker timePicker, final int hour, final int minute) {
//        fragmentTime = (FragmentTime) getSupportFragmentManager().findFragmentByTag("FragmentTime");
//        if (TextUtils.isEmpty(callback))
//            return;
//
//        if (callback.equalsIgnoreCase("for_start_time")) {
//            // set in mTimePicker
//            SimpleDateFormat f24Hour = new SimpleDateFormat("HH:mm");
//            try {
//                Date date = f24Hour.parse(hour + ":" + minute);
//                startTimeString = f24Hour.format(date);
//                fragmentTime.startHour(startTimeString + " -", placeHolder);
////                start_timer.setText(f24Hour.format(date) + " - ");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            DialogFragment timePicker1 = new TimePickerFragment();
//            timePicker1.show(getSupportFragmentManager(), "time picker");
//        } else if (callback.equalsIgnoreCase("for_end_time")) {
//            SimpleDateFormat f24Hour = new SimpleDateFormat("HH:mm");
//            try {
//                Date date = f24Hour.parse(hour + ":" + minute);
//                endTimeString = f24Hour.format(date);
//                fragmentTime.endHour("- " + endTimeString, placeHolder);
////                end_timer.setText(f24Hour.format(date));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            // set in mTimePicker1
//        }
//        //Dont forgot to reset callback
//        callback = "for_end_time";
//
//    }
//
//    public void onClick(View view) {
//        callback = "for_start_time";
//        DialogFragment timePicker = new TimePickerFragment();
//        timePicker.show(getSupportFragmentManager(), "time picker");
//    }
//}
//
//
