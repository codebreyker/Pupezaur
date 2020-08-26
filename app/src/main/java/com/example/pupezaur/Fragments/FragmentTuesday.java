package com.example.pupezaur.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pupezaur.R;

public class FragmentTuesday extends Fragment {

    TextView start_timer, end_timer;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tuesday, container, false);
        return v;
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
        System.err.println(layout.getChildCount() + " aAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        View v  = layout.getChildAt(layout.getChildCount()-1);
        start_timer = v.findViewById(R.id.start_timer);
        start_timer.setText(s);
    }

    public void endHour(String s, LinearLayout layout) {
        View v  = layout.getChildAt(layout.getChildCount()-1);
        end_timer = v.findViewById(R.id.end_timer);
        end_timer.setText(s);
    }
}
