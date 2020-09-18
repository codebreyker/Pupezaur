package com.example.parentsapp.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.fragment.app.Fragment;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class WednesdayUser extends Fragment {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseDatabase database;

    String startTimeString, endTimeString;
    String adminId;
    User u;

    List<Schedule> scheduleList;
    ViewGroup mContainer;
    FrameLayout frameLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

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

        mContainer = (ViewGroup) getLayoutInflater().inflate(R.layout.user_schedule_item, frameLayout, true);
        final LinearLayout linearLayout = mContainer.findViewById(R.id.schedule_layout);

        ((ViewGroup) viewGroup).removeView(linearLayout);

        final FirebaseUser currentUser = auth.getCurrentUser();
        database.getReference("Users").child(currentUser.getUid()).child("adminPhoneNumber").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                adminId = snapshot.getValue().toString();

                databaseReference = database.getReference("Schedule").child(adminId).child("Wednesday");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot snapshot) {
                        scheduleList.removeAll(scheduleList);
                        linearLayout.removeAllViews();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
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

                        for (int i = 0; i < scheduleList.size(); i++) {
                            linearLayout.addView(inflater.inflate(R.layout.user_schedule_item, null, false));
                            View view = linearLayout.getChildAt(linearLayout.getChildCount() - 1);

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
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                        builder.setTitle(scheduleHour);

                                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int j) {
                                                                if (j == 0) {
                                                                    databaseReference.child(scheduleList.get(finalI).getId()).child("names").child(u.getUid()).setValue(u.getFirstName() + u.getLastName());
                                                                    Toast.makeText(getActivity(), scheduled, Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        builder.show();
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

                                            databaseReference.child(scheduleList.get(i).getId()).child("names").child(u.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()){
                                                        CharSequence options[] = new CharSequence[]{
                                                                yes,
                                                                cancel
                                                        };
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                        builder.setTitle(unscheduleHour);

                                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int j) {
                                                                if (j == 0) {
                                                                    databaseReference.child(scheduleList.get(finalI1).getId()).child("names").child(u.getUid()).removeValue();
                                                                    Toast.makeText(getActivity(), unscheduled, Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        builder.show();
                                                    } else if (!snapshot.exists()){
                                                        Toast.makeText(getActivity(), nothingDelete, Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }
                                    return false;
                                }
                            });

                            for (int j = 0; j < scheduleList.get(i).getNamesList().size(); j++) {
                                if (j == 0) {
                                    txt_addName.append(scheduleList.get(i).getNamesList().get(j));
                                } else {
                                    txt_addName.append(", " + scheduleList.get(i).getNamesList().get(j));
                                }
                            }

                            TextView startTimer = view.findViewById(R.id.start_timer);
                            startTimeString = scheduleList.get(i).getStartHour();
                            startTimer.setText(startTimeString + " -");

                            TextView endTimer = view.findViewById(R.id.end_timer);
                            endTimeString = scheduleList.get(i).getEndHour();
                            endTimer.setText(" " + endTimeString);

                            Calendar calendar = Calendar.getInstance();
                            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm" + dayOfWeek, Locale.getDefault());
                            String currentTime = simpleDateFormat.format(calendar.getTime());
                            if (currentTime.compareTo(endTimeString) > 0 && dayOfWeek == Calendar.WEDNESDAY){
                                databaseReference.child(scheduleList.get(i).getId()).child("names").removeValue();
                            }

                            TextView txt_hours = view.findViewById(R.id.txt_hours);
                            txt_hours.setId(i);
                            TextView txt_students = view.findViewById(R.id.txt_students);
                            txt_students.setId(i);
                            startTimer.setId(i);
                            endTimer.setId(i);

                            if (currentTime.compareTo(endTimeString) < 0 && currentTime.compareTo(startTimeString) > 0 && dayOfWeek == Calendar.WEDNESDAY){
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

        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String currentDate = simpleDateFormat.format(calendar.getTime());
        TextView textViewDate = viewGroup.findViewById(R.id.txt_time);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.WEDNESDAY) {
            textViewDate.setText(currentDate);
        } else if (dayOfWeek == Calendar.MONDAY){
            calendar.add(Calendar.DATE, 2);
            currentDate = simpleDateFormat.format(calendar.getTime());
            textViewDate.setText(currentDate);
        } else if (dayOfWeek == Calendar.TUESDAY){
            calendar.add(Calendar.DATE, 1);
            currentDate = simpleDateFormat.format(calendar.getTime());
            textViewDate.setText(currentDate);
        } else if (dayOfWeek == Calendar.THURSDAY){
            calendar.add(Calendar.DATE, 6);
            currentDate = simpleDateFormat.format(calendar.getTime());
            textViewDate.setText(currentDate);
        }else if (dayOfWeek == Calendar.FRIDAY) {
            calendar.add(Calendar.DATE, 5);
            currentDate = simpleDateFormat.format(calendar.getTime());
            textViewDate.setText(currentDate);
        }else if (dayOfWeek == Calendar.SATURDAY) {
            calendar.add(Calendar.DATE, 4);
            currentDate = simpleDateFormat.format(calendar.getTime());
            textViewDate.setText(currentDate);
        }else if (dayOfWeek == Calendar.SUNDAY){
            calendar.add(Calendar.DATE, 3);
            currentDate = simpleDateFormat.format(calendar.getTime());
            textViewDate.setText(currentDate);
        }

        return viewGroup;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}

