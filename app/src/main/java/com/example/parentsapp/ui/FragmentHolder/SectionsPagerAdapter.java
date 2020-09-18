package com.example.parentsapp.ui.FragmentHolder;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.parentsapp.Fragments.FridayUser;
import com.example.parentsapp.Fragments.MondayUser;
import com.example.parentsapp.Fragments.SaturdayUser;
import com.example.parentsapp.Fragments.SundayUser;
import com.example.parentsapp.Fragments.ThursdayUser;
import com.example.parentsapp.Fragments.TuesdayUser;
import com.example.parentsapp.Fragments.WednesdayUser;

import com.example.pupezaur.R;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.monday, R.string.tuesday,  R.string.wednesday,  R.string.thursday,  R.string.friday,  R.string.saturday,  R.string.sunday};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
//        return PlaceholderFragment.newInstance(position + 1);

        Fragment fragment = null;

            switch (position) {
                case 0:
                    fragment = new MondayUser();
                    break;
                case 1:
                    fragment = new TuesdayUser();
                    break;
                case 2:
                    fragment = new WednesdayUser();
                    break;
                case 3:
                    fragment = new ThursdayUser();
                    break;
                case 4:
                    fragment = new FridayUser();
                    break;
                case 5:
                    fragment = new SaturdayUser();
                    break;
                case 6:
                    fragment = new SundayUser();
                    break;
            }
            return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 7;
    }
}