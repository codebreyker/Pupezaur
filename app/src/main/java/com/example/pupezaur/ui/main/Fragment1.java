package com.example.pupezaur.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pupezaur.R;

import java.text.DateFormat;
import java.util.Calendar;

import static java.util.Calendar.DAY_OF_WEEK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment1 newInstance(String param1, String param2) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.frag1_layout, container, false);


        Calendar calendar = Calendar.getInstance();
        //ziua curenta + skip duminica
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, 1);}
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView textViewDate = (TextView) v.findViewById(R.id.Day1);
        textViewDate.setText(currentDate);
        
        //ziua curenta
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY) {
            calendar.add(Calendar.DATE, 2);
        } else {
            calendar.add(Calendar.DATE, 1);
        }
        String currentDatePlusOne = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView textViewDateOne = (TextView) v.findViewById(R.id.Day2);
        textViewDateOne.setText(currentDatePlusOne);

        //ziua 3
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY) {
            calendar.add(Calendar.DATE, 2);
        } else {
            calendar.add(Calendar.DATE, 1);
        }
        String currentDatePlusTwo = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView textViewDateTwo = (TextView) v.findViewById(R.id.Day3);
        textViewDateTwo.setText(currentDatePlusTwo);

        //ziua 4
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY) {
            calendar.add(Calendar.DATE, 2);
        } else {
            calendar.add(Calendar.DATE, 1);
        }
        String currentDatePlusThree = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView textViewDateThree = (TextView) v.findViewById(R.id.Day4);
        textViewDateThree.setText(currentDatePlusThree);

        //ziua 5
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY) {
            calendar.add(Calendar.DATE, 2);
        } else {
            calendar.add(Calendar.DATE, 1);
        }
        String currentDatePlusFour = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView textViewDateFour = (TextView) v.findViewById(R.id.Day5);
        textViewDateFour.setText(currentDatePlusFour);

        //ziua 6
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY) {
            calendar.add(Calendar.DATE, 2);
        } else {
            calendar.add(Calendar.DATE, 1);
        }
        String currentDatePlusFive = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView textViewDateFive = (TextView) v.findViewById(R.id.Day6);
        textViewDateFive.setText(currentDatePlusFive);

        return v;
    }
}

