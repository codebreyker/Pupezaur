package com.example.pupezaur.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pupezaur.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FragmentTime extends Fragment {

    TextView start_timer, end_timer;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        return null;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        start_timer = getView().findViewById(R.id.start_timer);
        end_timer = getView().findViewById(R.id.end_timer);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void startHour(String s, LinearLayout layout) {
        View view  = layout.getChildAt(layout.getChildCount()-1);
        start_timer = view.findViewById(R.id.start_timer);
        start_timer.setText(s);
    }

    public void endHour(String s, LinearLayout layout) {
        View view  = layout.getChildAt(layout.getChildCount()-1);
        end_timer = view.findViewById(R.id.end_timer);
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

