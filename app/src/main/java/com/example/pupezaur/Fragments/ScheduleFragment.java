package com.example.pupezaur.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pupezaur.R;

import java.text.DateFormat;
import java.util.Calendar;

public class ScheduleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.frag1_layout, container, false);
        return v;
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

