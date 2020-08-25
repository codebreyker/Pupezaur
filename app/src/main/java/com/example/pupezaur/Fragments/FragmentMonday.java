package com.example.pupezaur.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pupezaur.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FragmentMonday extends Fragment {

    TextView start_timer, end_timer;
    ListView listView;
    private ArrayList<String> stringArrayList;
    FloatingActionButton btn_add;
    ViewGroup placeHolder, v;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.schedule_item, container, false);


        // Inflate the layout for this fragment
            return v;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        start_timer = getView().findViewById(R.id.start_timer);
        end_timer = getView().findViewById(R.id.end_timer);
//        listView = getView().findViewById(R.id.listView);

//        btn_add = view.findViewById(R.id.btn_add);
//        btn_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                    callback = "for_start_time";
////                    DialogFragment timePicker = new TimePickerFragment();
////                    timePicker.show(getSupportFragmentManager(), "time picker");
//                ViewGroup placeHolder;
//                placeHolder = getView().findViewById(R.id.layoutToAdd);
//                getLayoutInflater().inflate(R.layout.schedule_item, placeHolder, false);
//            }
//        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void startHour(String s, LinearLayout layout) {
//        if (schedule!=null) {
        start_timer = layout.getChildAt(layout.getChildCount()).findViewById(R.id.linear_text).findViewById(R.id.start_timer);
//        }
        start_timer.setText(s);
    }

    public void endHour(String s, LinearLayout layout) {
//        if (schedule!=null) {
            end_timer = layout.getChildAt(layout.getChildCount()).findViewById(R.id.linear_text).findViewById(R.id.end_timer);
//        }
        end_timer.setText(s);
    }




//        Calendar calendar = Calendar.getInstance();
//        //ziua curenta + skip duminica
//        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//        if (dayOfWeek == Calendar.SUNDAY) {
//            calendar.add(Calendar.DATE, 1);}
//        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
//        TextView textViewDate = (TextView) v.findViewById(R.id.Day1);
//        textViewDate.setText(currentDate);
//
//        //ziua curenta
//            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//        if (dayOfWeek == Calendar.SATURDAY) {
//            calendar.add(Calendar.DATE, 2);
//        } else {
//            calendar.add(Calendar.DATE, 1);
//        }
//        String currentDatePlusOne = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
//        TextView textViewDateOne = (TextView) v.findViewById(R.id.Day2);
//        textViewDateOne.setText(currentDatePlusOne);
//
//        //ziua 3
//            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//        if (dayOfWeek == Calendar.SATURDAY) {
//            calendar.add(Calendar.DATE, 2);
//        } else {
//            calendar.add(Calendar.DATE, 1);
//        }
//        String currentDatePlusTwo = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
//        TextView textViewDateTwo = (TextView) v.findViewById(R.id.Day3);
//        textViewDateTwo.setText(currentDatePlusTwo);
//
//        //ziua 4
//            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//        if (dayOfWeek == Calendar.SATURDAY) {
//            calendar.add(Calendar.DATE, 2);
//        } else {
//            calendar.add(Calendar.DATE, 1);
//        }
//        String currentDatePlusThree = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
//        TextView textViewDateThree = (TextView) v.findViewById(R.id.Day4);
//        textViewDateThree.setText(currentDatePlusThree);
//
//        //ziua 5
//            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//        if (dayOfWeek == Calendar.SATURDAY) {
//            calendar.add(Calendar.DATE, 2);
//        } else {
//            calendar.add(Calendar.DATE, 1);
//        }
//        String currentDatePlusFour = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
//        TextView textViewDateFour = (TextView) v.findViewById(R.id.Day5);
//        textViewDateFour.setText(currentDatePlusFour);
//
//        //ziua 6
//            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//        if (dayOfWeek == Calendar.SATURDAY) {
//            calendar.add(Calendar.DATE, 2);
//        } else {
//            calendar.add(Calendar.DATE, 1);
//        }
//        String currentDatePlusFive = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
//        TextView textViewDateFive = (TextView) v.findViewById(R.id.Day6);
//        textViewDateFive.setText(currentDatePlusFive);
//
//        return v;
//    }
}

