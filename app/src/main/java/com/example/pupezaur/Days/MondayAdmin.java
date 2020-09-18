package com.example.pupezaur.Days;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.pupezaur.Fragments.FragmentTime;
import com.example.pupezaur.Fragments.TimePickerFragmentEnd;
import com.example.pupezaur.Fragments.TimePickerFragmentStart;
import com.example.pupezaur.MainActivities.AdminChatActivity;
import com.example.pupezaur.MainActivities.SettingsActivity;
import com.example.pupezaur.Notifications.SendNotification;
import com.example.pupezaur.PhoneConnection.AdminPhoneRegister;
import com.example.pupezaur.R;
import com.example.pupezaur.Utils.Admin;
import com.example.pupezaur.Utils.AllMethods;
import com.example.pupezaur.Fragments.OnSwipeTouchListener;
import com.example.pupezaur.Utils.Message;
import com.example.pupezaur.Utils.Schedule;
import com.example.pupezaur.Utils.User;
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
import com.onesignal.OneSignal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MondayAdmin extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseDatabase database;

    FloatingActionButton btn_add;
    TextView btn_monday, btn_tuesday, btn_wednesday, btn_thursday, btn_friday, btn_saturday, btn_sunday;
    TextView badgeCounter;

    FrameLayout fragmentContainer;
    LinearLayout placeHolder, linearLayout_edit, linearLayout_delete;
    FragmentTime fragmentTime;

    List<User> userList;
    List<Schedule> scheduleList;
    List<String> notiKeyList;
    List<Message> messageList;

    MenuItem menuItem;
    String startTimeString, endTimeString, notiKey;
    View v;

    Admin admin;
    boolean isAdmin, isEditingSchedule;
    int idToEdit;
    int pendingNotif;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OneSignal.startInit(getApplication()).init();
        OneSignal.setSubscription(true);
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(final String userId, String registrationId) {
                FirebaseDatabase.getInstance().getReference("Admin").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child("notificationKey").setValue(userId);
            }
        });
        OneSignal.setInFocusDisplaying((OneSignal.OSInFocusDisplayOption.Notification));

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        admin = new Admin();
        scheduleList = new ArrayList<Schedule>();
        userList = new ArrayList<User>();
        notiKeyList = new ArrayList<String>();

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

//        swipe pentru ziua urmatoare sau precedenta
        v.setOnTouchListener(new OnSwipeTouchListener(MondayAdmin.this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                startActivity(new Intent(MondayAdmin.this, TuesdayAdmin.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

//        buton adaugat cursuri
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if ((startTimeString.isEmpty() || endTimeString.isEmpty()) && !scheduleList.isEmpty()) {
//                    String selectHour = getResources().getString(R.string.selectHour);
//                    Toast.makeText(MondayAdmin.this, selectHour, Toast.LENGTH_SHORT).show();
//                } else {
                placeHolder.addView(getLayoutInflater().inflate(R.layout.admin_schedule_item, null, false));
                startTimeString = "";
                endTimeString = "";
                callback = "for_start_time";
                DialogFragment timePicker = new TimePickerFragmentStart();
                timePicker.setCancelable(true);
                timePicker.show(getSupportFragmentManager(), "time picker");
                if (startTimeString.isEmpty() && endTimeString.isEmpty()) {
                    placeHolder.removeViewAt(placeHolder.getChildCount() - 1);
                }
//                }
            }
        });

//        butoanele cu zile
        btn_monday.setBackgroundResource(R.drawable.btns_days_blue);
        btn_monday.setTextColor(getResources().getColor(R.color.Black));
        btn_tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MondayAdmin.this, TuesdayAdmin.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        btn_wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MondayAdmin.this, WednesdayAdmin.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        btn_thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MondayAdmin.this, ThursdayAdmin.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        btn_friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MondayAdmin.this, FridayAdmin.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        btn_saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MondayAdmin.this, SaturdayAdmin.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        btn_sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MondayAdmin.this, SundayAdmin.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

//          Posibil (SIGUR) sa crape daca nu exista in baza de date un user cu proprietatea isAdmin
        FirebaseDatabase.getInstance().getReference("Admin").child(firebaseUser.getPhoneNumber()).child("isAdmin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String adm = snapshot.getValue().toString();
                System.err.println(adm + "+++++++++++++++++++++++++");
                if (adm.equals("1")) {
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

    void adminCheck() {
        if (isAdmin) {
            //daca e admin, ecranul va afista casute pentru a creea programul propriu
            //de asemenea o lista cu programerile pe care le are
            System.out.println("ADMIN !!!!!!!!!!!!!!!!!!!!");
//            textView.append("ADMIN");
        } else {
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

//        notificare la butonul de chat, badge
        database.getReference("Chats").child(currentUser.getPhoneNumber()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

//        notification key de la useri
        database.getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    if (snapshot.hasChildren()) {
                        if (datasnapshot.child("adminPhoneNumber").getValue().toString().equals(admin.getPhoneNumber())) {
                            notiKey = datasnapshot.child("notificationKey").getValue().toString();
                            notiKeyList.add(notiKey);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

//        lista de useri pt adaugat si sters
        databaseReference = database.getReference("Users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("adminPhoneNumber").getValue().toString().equals(firebaseUser.getPhoneNumber())) {
                        userList.add(dataSnapshot.getValue(User.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

//        adauga lista cu programe din baza de date
        databaseReference = database.getReference("Schedule").child(firebaseUser.getPhoneNumber()).child("Monday");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                scheduleList.removeAll(scheduleList);

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Schedule schedule = dataSnapshot.getValue(Schedule.class);
                    schedule.setId(dataSnapshot.getKey());
                    schedule.setNamesList(new ArrayList<String>());
                    schedule.setUserIds(new ArrayList<String>());

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (dataSnapshot1.getKey().equals("names")) {
                            for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                schedule.getNamesList().add(dataSnapshot2.getValue().toString());
                                schedule.getUserIds().add(dataSnapshot2.getKey());
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
                for (int i = 0; i < snapshot.getChildrenCount(); i++) {
                    placeHolder.addView(getLayoutInflater().inflate(R.layout.admin_schedule_item, null, false));
                    View view = placeHolder.getChildAt(placeHolder.getChildCount() - 1);

//                    editare program ore
                    LinearLayout linearLayout_edit = view.findViewById(R.id.linearLayout_edit);
                    linearLayout_edit.setId(i);
                    linearLayout_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            isEditingSchedule = true;
                            for (int i = 0; i < scheduleList.size(); i++) {
                                if (view.getId() == i) {
                                    idToEdit = view.getId();
                                }
                            }
                            callback = "for_start_time";
                            DialogFragment timePicker = new TimePickerFragmentStart();
                            timePicker.show(getSupportFragmentManager(), "time picker");
                        }
                    });

//                    stergere program ora
                    LinearLayout linearLayout_delete = view.findViewById(R.id.linearLayout_delete);
                    linearLayout_delete.setId(i);
                    linearLayout_delete.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {

                            for (int i = 0; i < scheduleList.size(); i++) {
                                if (view.getId() == i) {
                                    String yes = getResources().getString(R.string.yes);
                                    String cancel = getResources().getString(R.string.cancel);
                                    final String delete = getResources().getString(R.string.alerDialog_deleteHour);
                                    final String isDeleted = getResources().getString(R.string.courseDeleted);
                                    final String notDeleted = getResources().getString(R.string.courseNotDeleted);

                                    CharSequence options[] = new CharSequence[]{
                                            yes,
                                            cancel
                                    };

//                                    builder sa stergi ora
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MondayAdmin.this);
                                    builder.setTitle(delete);

                                    final int finalI = i;
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int j) {
                                            if (j == 0) {
                                                databaseReference = FirebaseDatabase.getInstance().getReference("Schedule").child(firebaseUser.getPhoneNumber()).child("Monday");
                                                databaseReference.child(scheduleList.get(finalI).getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(MondayAdmin.this, isDeleted, Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(MondayAdmin.this, notDeleted, Toast.LENGTH_SHORT).show();
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

//                    casuta unde vezi userii programati
                    final TextView txt_addName = view.findViewById(R.id.txt_nameToAdd);
                    for (int j = 0; j < scheduleList.get(i).getNamesList().size(); j++) {
                        if (j == 0) {
                            txt_addName.append(scheduleList.get(i).getNamesList().get(j));
                        } else {
                            txt_addName.append("\n" + scheduleList.get(i).getNamesList().get(j));
                        }
                    }

                    final int finalI = i;
                    txt_addName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {

                            final String deleteName = getResources().getString(R.string.deleteName);
                            final String addName = getResources().getString(R.string.addName);
                            String question = getResources().getString(R.string.whatToDo);

                            CharSequence items[] = new CharSequence[]{
                                    addName,
                                    deleteName
                            };

//                            builder pentru a programa sau deprograma useri
                            final AlertDialog.Builder builder = new AlertDialog.Builder(MondayAdmin.this);
                            builder.setTitle(question);

                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int j) {

//                                    builder pentru programat useri
                                    if (j == 0) {
                                        final AlertDialog.Builder builderList = new AlertDialog.Builder(MondayAdmin.this);
                                        final List<String> stringArray = new ArrayList<String>();
                                        String clearAll = getResources().getString(R.string.clearAll);

                                        for (User user : userList) {
                                            stringArray.add(user.getName());
                                        }

                                        final boolean[] chekedStringArray = new boolean[stringArray.size()];
                                        for (int k = 0; k < stringArray.size(); k++) {
                                            chekedStringArray[k] = false;
                                        }

                                        builderList.setMultiChoiceItems(stringArray.toArray(new CharSequence[stringArray.size()]), chekedStringArray, new DialogInterface.OnMultiChoiceClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                                chekedStringArray[i] = b;
                                            }
                                        });

//                                        buton OK pt programat useri
                                        builderList.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                for (int l = 0; l < chekedStringArray.length; l++) {
                                                    if (chekedStringArray[l]) {
                                                        final int finalL = l;
                                                        FirebaseDatabase.getInstance().getReference("Schedule")
                                                                .child(firebaseUser.getPhoneNumber())
                                                                .child("Monday")
                                                                .child(scheduleList.get(finalI).getId())
                                                                .orderByChild("names")
                                                                .equalTo(userList.get(l).getUid())
                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        if (!snapshot.exists()) {
                                                                            FirebaseDatabase.getInstance().getReference("Schedule")
                                                                                    .child(firebaseUser.getPhoneNumber())
                                                                                    .child("Monday")
                                                                                    .child(scheduleList.get(finalI).getId())
                                                                                    .child("names")
                                                                                    .child(userList.get(finalL).getUid())
                                                                                    .setValue(stringArray.get(finalL));

//                                                                            daca il programez luni pt luni sa-mi dea ziua curenta la notificare
//                                                                            daca il programez in alta zi pt luni, sa-mi dea data urmatoarei zi de luni
                                                                            int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                                                                            int dayOfMonday = Calendar.getInstance().get(Calendar.MONDAY);
                                                                            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM", Locale.getDefault());

                                                                            String hasScheduled = getResources().getString(R.string.hasBeenScheduled);
                                                                            String monday = getResources().getString(R.string.monday);
                                                                            String currentDate = simpleDateFormat.format(Calendar.getInstance().get(Calendar.DATE));

                                                                            if (dayOfMonday == dayOfWeek) {
                                                                                new SendNotification(admin.getName(),
                                                                                        hasScheduled + " " + scheduleList.get(finalI).getStartHour() + ", " + monday + " " + currentDate,
                                                                                        userList.get(finalL).getNotificationKey());
                                                                            } else {
                                                                                LocalDate localDate = LocalDate.now();
                                                                                localDate = localDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

                                                                                new SendNotification(admin.getName(),
                                                                                        hasScheduled + " " + scheduleList.get(finalI).getStartHour() + ", " + monday + " " + localDate.format(DateTimeFormatter.ofPattern("dd MMMM")),
                                                                                        userList.get(finalL).getNotificationKey());
                                                                            }

                                                                        } else if (snapshot.exists()) {
                                                                            String alreadySchedule = getResources().getString(R.string.alreadyScheduled);
                                                                            Toast.makeText(MondayAdmin.this, alreadySchedule, Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        });

//                                        buton de cancel
                                        builderList.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        });

//                                        buton de deselectat
                                        builderList.setNeutralButton(R.string.clearAll, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        });

                                        final AlertDialog dialog = builderList.create();
                                        dialog.show();
                                        dialog.setTitle(addName);

//                                        implementare buton deselectat tot ce e selectat
                                        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                for (int k = 0; k < chekedStringArray.length; k++) {
                                                    chekedStringArray[k] = false;
                                                    ((AlertDialog) dialog).getListView().setItemChecked(k, false);
                                                }
                                            }
                                        });

//                                        builderul pentru deprogramare useri din program
                                    } else if (j == 1) {
                                        if (!scheduleList.get(finalI).getNamesList().isEmpty()) {

                                            final AlertDialog.Builder builderList = new AlertDialog.Builder(MondayAdmin.this);
                                            final List<String> stringArray = new ArrayList<String>(scheduleList.get(finalI).getNamesList());
                                            final String clearAll = getResources().getString(R.string.clearAll);
                                            final String addAll = getResources().getString(R.string.addAll);

                                            final boolean[] chekedStringArray = new boolean[stringArray.size()];
                                            for (int k = 0; k < stringArray.size(); k++) {
                                                chekedStringArray[k] = false;
                                            }

                                            builderList.setMultiChoiceItems(stringArray.toArray(new CharSequence[stringArray.size()]), chekedStringArray, new DialogInterface.OnMultiChoiceClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                                    chekedStringArray[i] = b;
                                                }
                                            });

//                                            buton OK sters nume din lista
                                            builderList.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @RequiresApi(api = Build.VERSION_CODES.O)
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    for (int l = 0; l < chekedStringArray.length; l++) {
                                                        if (chekedStringArray[l]) {
                                                            FirebaseDatabase.getInstance().getReference("Schedule")
                                                                    .child(firebaseUser.getPhoneNumber())
                                                                    .child("Monday")
                                                                    .child(scheduleList.get(finalI).getId())
                                                                    .child("names")
                                                                    .child(scheduleList.get(finalI).getUserIds().get(l))
                                                                    .removeValue();

//                                                            daca il deprogramez luni pt luni sa-mi dea ziua curenta pt notificare
//                                                            daca il deprogramez in alta zi pt luni sa-mi dea urmatoare zi de luni
                                                            int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                                                            int dayOfMonday = Calendar.getInstance().get(Calendar.MONDAY);
                                                            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM", Locale.getDefault());

                                                            String hasBeenUnScheduled = getResources().getString(R.string.hasBeenUnscheduled);
                                                            String monday = getResources().getString(R.string.monday);
                                                            String currentDate = simpleDateFormat.format(Calendar.getInstance().get(Calendar.DATE));

                                                            if (dayOfWeek == dayOfMonday) {
                                                                new SendNotification(admin.getName(),
                                                                        hasBeenUnScheduled + " " + scheduleList.get(finalI).getStartHour() + ", " + monday + " " + currentDate,
                                                                        userList.get(l).getNotificationKey());
                                                            } else {
                                                                LocalDate localDate = LocalDate.now();
                                                                localDate = localDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

                                                                new SendNotification(admin.getName(),
                                                                        hasBeenUnScheduled + " " + scheduleList.get(finalI).getStartHour() + ", " + monday + " " + localDate.format(DateTimeFormatter.ofPattern("dd MMMM")),
                                                                        userList.get(l).getNotificationKey());
                                                            }
                                                        }
                                                    }
                                                }
                                            });

//                                        buton de deselectat toate numele selectate
                                            builderList.setNegativeButton(R.string.clearAll, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                }
                                            });

//                                        buton selectat toate numele
                                            builderList.setNeutralButton(R.string.addAll, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int witch) {
                                                }
                                            });

                                            final AlertDialog dialog = builderList.create();
                                            dialog.setTitle(deleteName);
                                            dialog.show();

//                                        implementare buton deselectare toate numele selectate
                                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    for (int k = 0; k < chekedStringArray.length; k++) {
                                                        chekedStringArray[k] = false;
                                                        ((AlertDialog) dialog).getListView().setItemChecked(k, false);
                                                    }
                                                }
                                            });

//                                        implementare buton selectare toate numele
                                            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    for (int k = 0; k < chekedStringArray.length; k++) {
                                                        chekedStringArray[k] = true;
                                                        ((AlertDialog) dialog).getListView().setItemChecked(k, true);
                                                    }
                                                }
                                            });

                                        } else {
                                            Toast.makeText(MondayAdmin.this, "nu exista nimic", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                            builder.show();
                        }
                    });

//                    text cu ora de inceput
                    TextView startTimer = view.findViewById(R.id.start_timer);
                    startTimeString = scheduleList.get(i).getStartHour();
                    startTimer.setText(startTimeString + " ");

//                    text cu ora de sfarsit
                    TextView endTimer = view.findViewById(R.id.end_timer);
                    endTimeString = scheduleList.get(i).getEndHour();
                    endTimer.setText("- " + endTimeString);

                    TextView txt_hours = view.findViewById(R.id.txt_hours);
                    TextView txt_students = view.findViewById(R.id.txt_students);

//                    schimba culoarea la ora in curs
                    Calendar calendar = Calendar.getInstance();
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm" + dayOfWeek, Locale.getDefault());
                    String currentTime = simpleDateFormat.format(calendar.getTime());
                    if (currentTime.compareTo(endTimeString) < 0 && currentTime.compareTo(startTimeString) > 0 && dayOfWeek == Calendar.MONDAY) {
                        txt_hours.setTextColor(getResources().getColor(R.color.AnotherBlue));
                        txt_students.setTextColor(getResources().getColor(R.color.AnotherBlue));
                        txt_addName.setTextColor(getResources().getColor(R.color.AnotherBlue));
                        startTimer.setTextColor(getResources().getColor(R.color.AnotherBlue));
                        endTimer.setTextColor(getResources().getColor(R.color.AnotherBlue));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    //    implementarea orelor din program, inceput si sfarsit
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
                    fragmentTime.startHour(startTimeString + " ", placeHolder);
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
                Schedule schedule = new Schedule(startTimeString, endTimeString);
                databaseReference.push().setValue(schedule);
            }
        }

        if (isEditingSchedule && callback.equals("for_end_time")) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Schedule").child(firebaseUser.getPhoneNumber()).child("Monday");
            databaseReference.child(scheduleList.get(idToEdit).getId()).child("startHour").setValue(startTimeString);
            databaseReference.child(scheduleList.get(idToEdit).getId()).child("endHour").setValue(endTimeString);
            isEditingSchedule = false;
        }

        //Dont forgot to reset callback
        callback = "for_end_time";

//        if (endTimeString.isEmpty() && !scheduleList.isEmpty()) {
//            placeHolder.removeViewAt(placeHolder.getChildCount() - 1);
//            return;
//        }
//
//        if (startTimeString.isEmpty() && !scheduleList.isEmpty()) {
//            placeHolder.removeViewAt(placeHolder.getChildCount() - 1);
//            return;
//        }

    }

    //    meniul de setari
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settingsmenu, menu);

        menuItem = menu.findItem(R.id.chatButton);

        if (pendingNotif == 0) {
            menuItem.setActionView(null);
        } else {
            menuItem.setActionView(R.layout.notification_badge);
            View view = menuItem.getActionView();
            badgeCounter = view.findViewById(R.id.badgeCounter);
            badgeCounter.setText(String.valueOf(pendingNotif));
            System.err.println(String.valueOf(pendingNotif));
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//            buton chat
        if (id == R.id.chatButton) {
            Intent intent = new Intent(MondayAdmin.this, AdminChatActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return false;
        }
//            buton setari
        if (id == R.id.btn_settings) {
            Intent intent = new Intent(MondayAdmin.this, SettingsActivity.class);
            startActivity(intent);
            return false;
        }
//            buton logout
        if (id == R.id.btn_logout) {
            OneSignal.setSubscription(false);
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MondayAdmin.this, AdminPhoneRegister.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //    clickul pe butonul de chat atunci cand are notification badge
    public void goChat(View view) {
        Intent intent = new Intent(MondayAdmin.this, AdminChatActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}


