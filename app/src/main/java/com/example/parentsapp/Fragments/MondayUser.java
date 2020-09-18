package com.example.parentsapp.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.pupezaur.Notifications.SendNotification;
import com.example.pupezaur.R;
import com.example.pupezaur.Utils.AllMethods;
import com.example.pupezaur.Utils.Schedule;
import com.example.pupezaur.Utils.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MondayUser extends Fragment {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseDatabase database;

    String startTimeString, endTimeString;
    String adminId, notiKey, notiKeyAdmin;
    ;
    User u;

    List<String> notiKeyList;
    List<Schedule> scheduleList;
    ViewGroup scheduleItem;
    FrameLayout frameLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        notiKeyList = new ArrayList<String>();
        scheduleList = new ArrayList<Schedule>();
        u = new User();

        startTimeString = "";
        endTimeString = "";

    }

    @Override
    public void onStart() {
        super.onStart();

        final FirebaseUser currentUser = auth.getCurrentUser();
        u.setUid(currentUser.getUid());

        database.getReference("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u = dataSnapshot.getValue(User.class);
                u.setUid(currentUser.getUid());
                AllMethods.name = u.getName();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View viewGroup = getLayoutInflater().inflate(R.layout.fragment_monday, null);
        frameLayout = viewGroup.findViewById(R.id.frameLayout);

        scheduleItem = (ViewGroup) getLayoutInflater().inflate(R.layout.user_schedule_item, frameLayout, true);
        final LinearLayout linearLayout = scheduleItem.findViewById(R.id.schedule_layout);

        ((ViewGroup) viewGroup).removeView(linearLayout);

//        notification key de la admin
        final FirebaseUser currentUser = auth.getCurrentUser();
        database.getReference("Users").child(currentUser.getUid()).child("adminPhoneNumber").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                adminId = snapshot.getValue().toString();

                database.getReference("Admin").child(adminId).child("notificationKey").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            notiKeyAdmin = snapshot.getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//                afisarea programului adminului din baza de date
                databaseReference = database.getReference("Schedule").child(adminId).child("Monday");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot snapshot) {
                        scheduleList.removeAll(scheduleList);
                        linearLayout.removeAllViews();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Schedule schedule = dataSnapshot.getValue(Schedule.class);
                            schedule.setId(dataSnapshot.getKey());
                            schedule.setNamesList(new ArrayList<String>());

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if (dataSnapshot1.getKey().equals("names")) {
                                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
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

                        for (int i = 0; i < scheduleList.size(); i++) {
                            linearLayout.addView(inflater.inflate(R.layout.user_schedule_item, null, false));
                            View view = linearLayout.getChildAt(linearLayout.getChildCount() - 1);

//                            casuta pentru adaugarea userului la program
                            TextView txt_addName = view.findViewById(R.id.txt_addName);
                            txt_addName.setId(i);
                            txt_addName.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    for (int i = 0; i < scheduleList.size(); i++) {
                                        if (view.getId() == i) {
                                            final String yes = getResources().getString(R.string.yes);
                                            final String cancel = getResources().getString(R.string.cancel);
                                            final String scheduled = getResources().getString(R.string.scheduled);
                                            final String scheduleHour = getResources().getString(R.string.scheduleHour);
                                            final String alreadyScheduled = getResources().getString(R.string.alreadyScheduled);
                                            final String tooMany = getResources().getString(R.string.tooMany);

                                            final int finalI = i;
                                            databaseReference.child(scheduleList.get(i).getId()).child("names").child(u.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        Toast.makeText(getActivity(), alreadyScheduled, Toast.LENGTH_SHORT).show();
                                                    } else if (scheduleList.get(finalI).getNamesList().size() < 3 && !snapshot.exists()) {

                                                        CharSequence options[] = new CharSequence[]{
                                                                yes,
                                                                cancel
                                                        };

//                                                        builder confirmare programare
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                        builder.setTitle(scheduleHour);

                                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int j) {
                                                                if (j == 0) {
                                                                    databaseReference.child(scheduleList.get(finalI).getId()).child("names").child(u.getUid()).setValue(u.getName());

//                                                                    daca se programeaza luni pt luni sa arate data curenta
//                                                                    daca se programeaza in alta zi pt luni sa arate data din urmatoarea luni
                                                                    int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                                                                    int dayOfMonday = Calendar.getInstance().get(Calendar.MONDAY);
                                                                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM", Locale.getDefault());

                                                                    String hasScheduled = getResources().getString(R.string.hasSchedule);
                                                                    String monday = getResources().getString(R.string.monday);
                                                                    String currentDate = simpleDateFormat.format(Calendar.getInstance().get(Calendar.DATE));

                                                                    if (dayOfWeek == dayOfMonday) {
                                                                        new SendNotification(u.getName(),
                                                                                hasScheduled + " " + scheduleList.get(finalI).getStartHour() + ", " + monday + " " + currentDate,
                                                                                notiKeyAdmin);
                                                                    } else {
                                                                        LocalDate localDate = LocalDate.now();
                                                                        localDate = localDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

                                                                        new SendNotification(u.getName(),
                                                                                hasScheduled + " " + scheduleList.get(finalI).getStartHour() + ", " + monday + " " + localDate.format(DateTimeFormatter.ofPattern("dd MMMM")),
                                                                                notiKeyAdmin);

                                                                        Toast.makeText(getActivity(), scheduled, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            }
                                                        });

                                                        builder.show();

//                                                        daca sunt deja 3 programati, nu se mai pot programa
                                                    } else if (scheduleList.get(finalI).getNamesList().size() >= 3) {
                                                        Toast.makeText(getActivity(), tooMany, Toast.LENGTH_SHORT).show();
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

//                            textul pentru deprogramat useri
                            txt_addName.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    for (int i = 0; i < scheduleList.size(); i++) {
                                        if (view.getId() == i) {
                                            final String yes = getResources().getString(R.string.yes);
                                            final String cancel = getResources().getString(R.string.cancel);
                                            final String unscheduled = getResources().getString(R.string.unscheduled);
                                            final String unscheduleHour = getResources().getString(R.string.unscheduleHour);
                                            final String nothingDelete = getResources().getString(R.string.nothingDeleted);
                                            final int finalI1 = i;

                                            final int finalI = i;
                                            databaseReference.child(scheduleList.get(i).getId()).child("names").child(u.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        CharSequence options[] = new CharSequence[]{
                                                                yes,
                                                                cancel
                                                        };

//                                                      builder pentru deprogramare
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                        builder.setTitle(unscheduleHour);

                                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int j) {
                                                                if (j == 0) {
                                                                    databaseReference.child(scheduleList.get(finalI1).getId()).child("names").child(u.getUid()).removeValue();

//                                                                    daca se deprogrameaza luni pt luni sa arate data curenta
//                                                                    daca se deprogrameaza in alta zi pt luni sa arate data din urmatoarea luni
                                                                    int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                                                                    int dayOfMonday = Calendar.getInstance().get(Calendar.MONDAY);
                                                                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM", Locale.getDefault());

                                                                    String hasScheduled = getResources().getString(R.string.hasSchedule);
                                                                    String monday = getResources().getString(R.string.monday);
                                                                    String currentDate = simpleDateFormat.format(Calendar.getInstance().get(Calendar.DATE));

                                                                    if (dayOfWeek == dayOfMonday) {
                                                                        new SendNotification(u.getName(),
                                                                                hasScheduled + " " + scheduleList.get(finalI).getStartHour() + ", " + monday + " " + currentDate,
                                                                                notiKeyAdmin);
                                                                    } else {
                                                                        LocalDate localDate = LocalDate.now();
                                                                        localDate = localDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

                                                                        new SendNotification(u.getName(),
                                                                                hasScheduled + " " + scheduleList.get(finalI).getStartHour() + ", " + monday + " " + localDate.format(DateTimeFormatter.ofPattern("dd MMMM")),
                                                                                notiKeyAdmin);

                                                                        Toast.makeText(getActivity(), unscheduled, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            }
                                                        });

                                                        builder.show();

//                                                        daca nu este programat userul nu are ce sa stearga
                                                    } else {
                                                        Toast.makeText(getActivity(), nothingDelete, Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                }
                                            });
                                        }
                                    }
                                    return true;
                                }
                            });

//                            cum sunt afisati userii in casuta lor de programare
                            for (int j = 0; j < scheduleList.get(i).getNamesList().size(); j++) {
                                if (j == 0) {
                                    txt_addName.append(scheduleList.get(i).getNamesList().get(j));
                                } else {
                                    txt_addName.append("\n" + scheduleList.get(i).getNamesList().get(j));
                                }
                            }

//                            afisarea orelor de inceput si sfarsit a programului
                            TextView startTimer = view.findViewById(R.id.start_timer);
                            startTimeString = scheduleList.get(i).getStartHour();
                            startTimer.setText(startTimeString + " -");

                            TextView endTimer = view.findViewById(R.id.end_timer);
                            endTimeString = scheduleList.get(i).getEndHour();
                            endTimer.setText(" " + endTimeString);

//                            deprogramare automata dupa ce a trecut ora de sfarsit a programului
                            Calendar calendar = Calendar.getInstance();
                            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm" + dayOfWeek, Locale.getDefault());
                            String currentTime = simpleDateFormat.format(calendar.getTime());
                            if (currentTime.compareTo(endTimeString) > 0 && dayOfWeek == Calendar.MONDAY) {
                                databaseReference.child(scheduleList.get(i).getId()).child("names").removeValue();
                            }

//                            colorarea textelor pentru ora in curs
                            TextView txt_hours = view.findViewById(R.id.txt_hours);
                            txt_hours.setId(i);
                            TextView txt_students = view.findViewById(R.id.txt_students);
                            txt_students.setId(i);
                            startTimer.setId(i);
                            endTimer.setId(i);

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

//        afisarea datelor pentru fiecare zi in parte
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
        String currentDate = simpleDateFormat.format(calendar.getTime());
        TextView textViewDate = viewGroup.findViewById(R.id.txt_time);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.MONDAY) {
            textViewDate.setText(currentDate);
        } else if (dayOfWeek == Calendar.TUESDAY) {
            calendar.add(Calendar.DATE, 6);
            currentDate = simpleDateFormat.format(calendar.getTime());
            textViewDate.setText(currentDate);
        } else if (dayOfWeek == Calendar.WEDNESDAY) {
            calendar.add(Calendar.DATE, 5);
            currentDate = simpleDateFormat.format(calendar.getTime());
            textViewDate.setText(currentDate);
        } else if (dayOfWeek == Calendar.THURSDAY) {
            calendar.add(Calendar.DATE, 4);
            currentDate = simpleDateFormat.format(calendar.getTime());
            textViewDate.setText(currentDate);
        } else if (dayOfWeek == Calendar.FRIDAY) {
            calendar.add(Calendar.DATE, 3);
            currentDate = simpleDateFormat.format(calendar.getTime());
            textViewDate.setText(currentDate);
        } else if (dayOfWeek == Calendar.SATURDAY) {
            calendar.add(Calendar.DATE, 2);
            currentDate = simpleDateFormat.format(calendar.getTime());
            textViewDate.setText(currentDate);
        } else if (dayOfWeek == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, 1);
            currentDate = simpleDateFormat.format(calendar.getTime());
            textViewDate.setText(currentDate);
        }

        return viewGroup;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}

