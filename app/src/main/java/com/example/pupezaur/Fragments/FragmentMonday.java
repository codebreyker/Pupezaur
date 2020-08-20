package com.example.pupezaur.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.pupezaur.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FragmentMonday extends Fragment {

    TextView start_timer, end_timer;
    FloatingActionButton btn_add;
    TimePicker timePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monday, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        start_timer = getView().findViewById(R.id.start_timer);
        end_timer = getView().findViewById(R.id.end_timer);
        btn_add = getView().findViewById(R.id.btn_add);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void update (String s) {
        start_timer.setText(s);
    }

}