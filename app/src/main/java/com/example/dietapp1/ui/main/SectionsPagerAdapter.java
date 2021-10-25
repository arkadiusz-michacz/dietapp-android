package com.example.dietapp1.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.dietapp1.R;
import com.example.dietapp1.TrendActivity;
import com.trendfragment.fragmentCarb;
import com.trendfragment.fragmentCkal;
import com.trendfragment.fragmentFat;
import com.trendfragment.fragmentProt;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_kcal, R.string.tab_prot,R.string.tab_fat,R.string.tab_carb};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //return PlaceholderFragment.newInstance(position + 1);

        Fragment fragment = null;

        switch (position) {
            case 0:
                //System.out.println("Jesteśmy w sections pager adapter");
                fragment = fragmentCkal.newInstance(TrendActivity.arrayDaty,TrendActivity.arrayCkal);//new com.trendfragment.fragmentCkal();
                break;
            case 1:
                fragment = fragmentProt.newInstance(TrendActivity.arrayDaty,TrendActivity.arrayProt);
                break;
            case 2:
                fragment = fragmentFat.newInstance(TrendActivity.arrayDaty,TrendActivity.arrayFat);
                break;
            case 3:
                fragment = fragmentCarb.newInstance(TrendActivity.arrayDaty,TrendActivity.arrayCarb);
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
        // Show 4 total pages.
        return 4;
    }
}