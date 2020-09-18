package com.example.pupezaur.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.pupezaur.R;

public class TimePickerFragmentStart extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new TimePickerDialog(getActivity(), 3, (TimePickerDialog.OnTimeSetListener) getActivity(), 12, 0, true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String startTime = getResources().getString(R.string.startTime);
        getDialog().setTitle(startTime);
        return super.onCreateView(inflater, container, savedInstanceState);

    }
}
